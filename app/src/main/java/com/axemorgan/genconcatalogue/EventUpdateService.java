package com.axemorgan.genconcatalogue;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.axemorgan.genconcatalogue.dagger.DaggerNetworkComponent;
import com.axemorgan.genconcatalogue.dagger.NetworkModule;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EventUpdateService extends IntentService {

    private static final String ACTION_UPDATE_EVENTS = "action_update_events";

    public static Intent getIntent(Context context) {
        return new Intent(context, EventUpdateService.class).
                setAction(ACTION_UPDATE_EVENTS);
    }


    @Inject
    Retrofit retrofit;

    public EventUpdateService() {
        super("Event Update Service");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerNetworkComponent.builder()
                .appComponent(CatalogueApplication.get(this.getApplicationContext()).getComponent())
                .networkModule(new NetworkModule())
                .build().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            EventListClient eventListClient = retrofit.create(EventListClient.class);
            Response<ResponseBody> response = eventListClient.getEventList().execute();
            if (response.isSuccessful()) {
                if (this.writeResponseBodyToDisk(response.body())) {
                    Log.i("EventUpdateService", "Successfully wrote to file");
                } else {
                    Log.e("EventUpdateService", "Failed to write to file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {


            OPCPackage pkg = OPCPackage.open(this.getEventsFile());
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);

//            // To look up the Sheet Name / Sheet Order / rID,
//            //  you need to process the core Workbook stream.
//            // Normally it's of the form rId# or rSheet#
//            InputStream sheet2 = r.getSheet("rId2");
//            InputSource sheetSource = new InputSource(sheet2);

            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            XSSFReader xssfReader = new XSSFReader(pkg);
            StylesTable styles = xssfReader.getStylesTable();
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            int index = 0;
            while (iter.hasNext()) {
                InputStream stream = iter.next();
                String sheetName = iter.getSheetName();
                DataFormatter formatter = new DataFormatter();
                ContentHandler handler = new XSSFSheetXMLHandler(
                        styles, null, strings, new MyContentHandler(), formatter, false);
                parser.setContentHandler(handler);
                parser.parse(new InputSource(stream));
                stream.close();
                ++index;
            }


            Log.i("PROCESS", "Workbook loaded");


        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        // Parse into model objects

        // Persist into database
    }

    private static class MyContentHandler implements SheetContentsHandler {


        @Override
        public void startRow(int i) {

        }

        @Override
        public void endRow(int i) {

        }

        @Override
        public void cell(String s, String s1, XSSFComment xssfComment) {
            Log.i("PROCESS", "Cell " + s + "," + s1);
        }

        @Override
        public void headerFooter(String s, boolean b, String s1) {

        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
//        XMLReader parser =
//                XMLReaderFactory.createXMLReader(
//                        "org.apache.xerces.parsers.SAXParser"
//                );

        SAXParserFactory saxPF = SAXParserFactory.newInstance();

        SAXParser saxP = null;
        try {
            saxP = saxPF.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        ContentHandler handler = new SheetHandler(sst);
        XMLReader xmlReader = saxP.getXMLReader();
        xmlReader.setContentHandler(handler);
        return xmlReader;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if (name.equals("c")) {
                // Print the cell reference
                System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if (name.equals("v")) {
                System.out.println(lastContents);
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(this.getNewEventsFile());

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private File getNewEventsFile() {
        File eventsFile = new File(this.getApplicationContext().getFilesDir(), "events.xlsx");
        if (eventsFile.exists()) {
            eventsFile.delete();
        }
        return eventsFile;
    }

    private File getEventsFile() {
        return new File(this.getApplicationContext().getFilesDir(), "events.xlsx");
    }
}
