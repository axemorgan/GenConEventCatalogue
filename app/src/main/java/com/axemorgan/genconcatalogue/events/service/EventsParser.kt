package com.axemorgan.genconcatalogue.events.service

import com.axemorgan.genconcatalogue.events.Event
import com.axemorgan.genconcatalogue.events.EventBuilder
import com.axemorgan.genconcatalogue.events.EventDao
import com.axemorgan.genconcatalogue.events.EventUpdateService.DATE_TIME_FORMATTER
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException
import org.apache.poi.openxml4j.exceptions.OpenXML4JException
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable
import org.apache.poi.xssf.eventusermodel.XSSFReader
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
import org.apache.poi.xssf.model.SharedStringsTable
import org.apache.poi.xssf.usermodel.XSSFComment
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeParseException
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

class EventsParser @Inject constructor(private val contentHandler: SheetContentHandler, private val batcher: Batcher) {

    fun parseEvents(fileStream: InputStream) {
        try {

            Timber.i("Parsing event list file")
            val pkg = OPCPackage.open(fileStream)
            val r = XSSFReader(pkg)
            val sst = r.sharedStringsTable
            val parser = fetchSheetParser(sst)

            //            // To look up the Sheet Name / Sheet Order / rID,
            //            //  you need to process the core Workbook stream.
            //            // Normally it's of the form rId# or rSheet#
            //            InputStream sheet2 = r.getSheet("rId2");
            //            InputSource sheetSource = new InputSource(sheet2);

            val strings = ReadOnlySharedStringsTable(pkg)
            val xssfReader = XSSFReader(pkg)
            val styles = xssfReader.stylesTable
            val iter = xssfReader.sheetsData as XSSFReader.SheetIterator
            var index = 0
            while (iter.hasNext()) {
                val stream = iter.next()
                val sheetName = iter.sheetName
                val formatter = DataFormatter()
                val handler = XSSFSheetXMLHandler(
                        styles, null, strings, contentHandler, formatter, false)
                parser.contentHandler = handler
                parser.parse(InputSource(stream))
                batcher.finish()
                stream.close()
                ++index
            }
        } catch (e: NotOfficeXmlFileException) {
            Timber.e(e, "Events file seems to not be in xlsx format")
        } catch (e: IOException) {
            Timber.e(e)
        } catch (e: OpenXML4JException) {
            Timber.e(e)
        } catch (e: SAXException) {
            Timber.e(e)
        }
    }

    @Throws(SAXException::class)
    private fun fetchSheetParser(sst: SharedStringsTable?): XMLReader {
        //        XMLReader parser =
        //                XMLReaderFactory.createXMLReader(
        //                        "org.apache.xerces.parsers.SAXParser"
        //                );

        val saxPF = SAXParserFactory.newInstance()

        var saxP: SAXParser? = null
        try {
            saxP = saxPF.newSAXParser()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }

        val handler = SheetHandler(sst)
        val xmlReader = saxP!!.xmlReader
        xmlReader.contentHandler = handler
        return xmlReader
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class SheetHandler constructor(private val sst: SharedStringsTable?) : DefaultHandler() {
        private var lastContents: String? = null
        private var nextIsString: Boolean = false

        @Throws(SAXException::class)
        override fun startElement(uri: String, localName: String, name: String,
                                  attributes: Attributes) {
            // c => cell
            if (name == "c") {
                // Print the cell reference
                print(attributes.getValue("r") + " - ")
                // Figure out if the value is an index in the SST
                val cellType = attributes.getValue("t")
                if (cellType != null && cellType == "s") {
                    nextIsString = true
                } else {
                    nextIsString = false
                }
            }
            // Clear contents cache
            lastContents = ""
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String, localName: String, name: String) {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                val idx = Integer.parseInt(lastContents)
                lastContents = XSSFRichTextString(sst?.getEntryAt(idx)).toString()
                nextIsString = false
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if (name == "v") {
                println(lastContents)
            }
        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray, start: Int, length: Int) {
            lastContents += String(ch, start, length)
        }
    }

    class SheetContentHandler @Inject constructor(private val batcher: Batcher) : XSSFSheetXMLHandler.SheetContentsHandler {

        private var builder: EventBuilder = EventBuilder()

        override fun startRow(i: Int) {
            builder = EventBuilder()
        }

        override fun endRow(i: Int) {
            if (i != 0) {
                val event = builder.create()
                batcher.put(event)
            }
        }

        override fun cell(cellIdentifier: String, cellText: String, xssfComment: XSSFComment?) {
            when (getColumnIdentifier(cellIdentifier)) {
                "A" -> {
                    builder.setId(cellText)
                }
                "B" -> {
                    builder.setGroup(cellText)
                }
                "C" -> {
                    builder.setTitle(cellText)
                }
                "D" -> {
                    builder.setShortDescription(cellText)
                }
                "E" -> {
                    builder.setLongDescription(cellText)
                }
                "F" -> {
                    builder.setEventType(cellText)
                }
                "G" -> {
                    builder.setGameSystem(cellText)
                }
                "H" -> {
                    builder.setRulesEdition(cellText)
                }
                "I" -> {
                    try {
                        builder.setMinimumPlayers(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from $cellText for min number of players")
                    }

                }
                "J" -> {
                    try {
                        builder.setMaximumPlayers(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from $cellText for max number of players")
                    }

                }
                "K" -> {
                    builder.setAgeRequired(cellText)
                }
                "L" -> {
                    builder.setExperienceRequired(cellText)
                }
                "M" -> {
                    builder.setMaterialsProvided(cellText.equals("yes", ignoreCase = true))
                }
                "N" -> {
                    try {
                        builder.setStartDate(ZonedDateTime.of(LocalDateTime.parse(cellText, DATE_TIME_FORMATTER), ZoneId.of("-4")))
                    } catch (e: DateTimeParseException) {
                        Timber.w(e, "Unable to parse a start date from %s", cellText)
                    }

                }
                "O" -> {
                    try {
                        builder.setDuration(java.lang.Double.parseDouble(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a double from %s for duration", cellText)
                    }

                }
                "P" -> {
                    try {
                        builder.setEndDate(ZonedDateTime.of(LocalDateTime.parse(cellText, DATE_TIME_FORMATTER), ZoneId.of("-4")))
                    } catch (e: DateTimeParseException) {
                        Timber.w(e, "Unable to parse an end time from %s", cellText)
                    }

                }
                "Q" -> {
                    builder.setGmNames(cellText)
                }
                "R" -> {
                    builder.setWebsite(cellText)
                }
                "S" -> {
                    builder.setEmail(cellText)
                }
                "T" -> {
                    builder.setIsTournament(cellText.equals("yes", ignoreCase = true))
                }
                "U" -> {
                    try {
                        builder.setRoundNumber(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from %s for round number", cellText)
                    }

                }
                "V" -> {
                    try {
                        builder.setTotalRounds(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from %s for total rounds", cellText)
                    }

                }
                "Y" -> {
                    try {
                        builder.setCost(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from %s for cost", cellText)
                    }

                }
                "Z" -> {
                    builder.setLocation(cellText)
                }
                "AA" -> {
                    builder.setRoomName(cellText)
                }
                "AB" -> {
                    builder.setTableNumber(cellText)
                }
                "AD" -> {
                    try {
                        builder.setAvailableTickets(Integer.parseInt(cellText))
                    } catch (e: NumberFormatException) {
                        Timber.w("Unable to parse a number from %s for available tickets", cellText)
                    }

                }
                "AE" -> {
                    builder.setLastModified(cellText)
                }
            }
        }

        override fun headerFooter(s: String, b: Boolean, s1: String) {

        }

        private fun getColumnIdentifier(columnAndRow: String): String {
            var columnId = ""

            for (c in columnAndRow.toCharArray()) {
                if (Character.isDigit(c)) {
                    break
                } else {
                    columnId += c
                }
            }

            return columnId
        }
    }

    @Singleton
    class Batcher @Inject constructor(private val eventDao: EventDao) {

        private val maxBufferSize = 20
        private val buffer = ArrayList<Event>(maxBufferSize)

        fun put(event: Event) {
            if (buffer.size >= maxBufferSize) {
                eventDao.putAll(buffer)
                buffer.clear()
            }
            buffer.add(event)
        }

        fun finish() {
            eventDao.putAll(buffer)
            buffer.clear()
        }
    }
}