package com.axemorgan.genconcatalogue.events.service

import com.axemorgan.genconcatalogue.events.EventListClient
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*
import javax.inject.Inject

class EventsDownloader @Inject constructor(private val client: EventListClient) {

    fun downloadEvents(eventsFile: File) {
        Timber.i("Downloading events list")
        val response = client.eventList.execute()
        if (response.isSuccessful) {
            writeResponseBodyToDisk(response.body(), eventsFile)
            Timber.i("Events List Fetched")
        } else {
            Timber.e("Failed to fetch events list %s",
                    response.errorBody() ?: "No response error message")
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody?, eventsFile: File): Boolean {
        Timber.i("Writing event list file to disk")
        try {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                inputStream = body!!.byteStream()
                outputStream = FileOutputStream(eventsFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)
                }

                outputStream.flush()

                Timber.i("Finished writing events file")
                return true
            } catch (e: IOException) {
                Timber.e(e, "Exception while writing events file to disk")
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            Timber.e(e, "Exception while writing events file to disk")
            return false
        }
    }
}
