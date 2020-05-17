package com.example.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import com.example.api.model.Filter
import com.example.api.model.Launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList


class DataUtils {

    companion object {

        fun getDateTime(s: String): String? {
            return try {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm XXX")
                val netDate = Date(s.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }

        fun filterFirstLoad(filter: Filter): ArrayList<Launch> {
            var launches = Provider.allLaunches
            if (!filter.showUpcoming) {
                launches = showHideUpcoming(filter)
            }

            if (filter.reverseList) {
                launches.reverse()
            }
            return launches
        }

        fun reverseList(launches: ArrayList<Launch>): ArrayList<Launch> {
            launches.reverse()
            return launches
        }

        fun showHideUpcoming(filter: Filter): ArrayList<Launch> {
            var newLaunches = Provider.allLaunches
            if (!filter.showUpcoming) {
                newLaunches = ArrayList<Launch>()
                for (launch in Provider.allLaunches) {
                    if (!launch.upcoming) {
                        newLaunches.add(launch)
                    }
                }
            }

            return newLaunches
        }

        fun getStringFromJSONArray(jsonArray: JSONArray): String {
            var string = ""
            if (jsonArray.length() == 1) {
                string = jsonArray.getString(0)
            }
            if (jsonArray.length() > 1) {
                string = jsonArray.getString(0)
                for (i in 1 until jsonArray.length()) {
                    string += ", ${jsonArray.getString(i)}"
                }
            }
            return string
        }

        fun getStringFromJsonObject(jsonObject: JSONObject, valueparam: String, obj: Any): String {
            if (obj == String) {
                try {
                    if (jsonObject.getString(valueparam).toString() != "null") {
                        return jsonObject.getString(valueparam).toString()
                    }
                } catch (e: JSONException) {
                    return "Not available"
                }
            }
            if (obj == Int) {
                try {
                    return jsonObject.getInt(valueparam).toString()
                } catch (e: JSONException) {
                    return "Not available"
                }
            }
            if (obj == Boolean) {
                try {
                    return jsonObject.getBoolean(valueparam).toString()
                } catch (e: JSONException) {
                    return "Not available"
                }
            }
            return "Not available"
        }



        fun getBitmapFromString(url_string: String): Bitmap? {

            try {
                val url = URL(url_string)
                val connection: HttpsURLConnection =
                    url.openConnection() as HttpsURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
//                bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false)
//                val newbitmap = compressBitmap(bitmap, 1)

                return bitmap
            } catch (e: IOException) {
                Log.e("Exceptions", e.toString())
                return null
            }
        }

//        private fun compressBitmap(bitmap:Bitmap, quality:Int):Bitmap{
//            val stream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
//
//            val byteArray = stream.toByteArray()
//
//            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        }


    }
}