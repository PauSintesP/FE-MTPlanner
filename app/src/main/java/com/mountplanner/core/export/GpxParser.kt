package com.mountplanner.core.export

import android.util.Xml
import com.mountplanner.data.local.entity.LocationPoint
import com.mountplanner.data.local.entity.Poi
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class GpxData(
    val trackPoints: List<LocationPoint>,
    val waypoints: List<Poi>
)

class GpxParser {
    
    fun parse(inputStream: InputStream): GpxData {
        val trackPoints = mutableListOf<LocationPoint>()
        val waypoints = mutableListOf<Poi>()
        
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        
        var currentLat = 0.0
        var currentLon = 0.0
        var currentEle: Double? = null
        var currentTime: Long = System.currentTimeMillis()
        var currentPoiName = ""
        var currentPoiDesc = ""
        
        var eventType = parser.eventType
        var inTrkpt = false
        var inWpt = false
        var textValue = ""
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "trkpt" -> {
                            inTrkpt = true
                            currentLat = parser.getAttributeValue(null, "lat").toDoubleOrNull() ?: 0.0
                            currentLon = parser.getAttributeValue(null, "lon").toDoubleOrNull() ?: 0.0
                            currentEle = null
                        }
                        "wpt" -> {
                            inWpt = true
                            currentLat = parser.getAttributeValue(null, "lat").toDoubleOrNull() ?: 0.0
                            currentLon = parser.getAttributeValue(null, "lon").toDoubleOrNull() ?: 0.0
                            currentEle = null
                            currentPoiName = ""
                            currentPoiDesc = ""
                        }
                    }
                }
                XmlPullParser.TEXT -> {
                    textValue = parser.text
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "ele" -> currentEle = textValue.toDoubleOrNull()
                        "time" -> {
                            try {
                                currentTime = dateFormat.parse(textValue)?.time ?: System.currentTimeMillis()
                            } catch (e: Exception) {
                                // ignore
                            }
                        }
                        "name" -> {
                            if (inWpt) currentPoiName = textValue
                        }
                        "desc" -> {
                            if (inWpt) currentPoiDesc = textValue
                        }
                        "trkpt" -> {
                            if (inTrkpt) {
                                trackPoints.add(
                                    LocationPoint(
                                        latitude = currentLat,
                                        longitude = currentLon,
                                        elevation = currentEle,
                                        timestamp = currentTime
                                    )
                                )
                                inTrkpt = false
                            }
                        }
                        "wpt" -> {
                            if (inWpt) {
                                waypoints.add(
                                    Poi(
                                        name = currentPoiName.ifEmpty { "Waypoint" },
                                        description = currentPoiDesc,
                                        latitude = currentLat,
                                        longitude = currentLon,
                                        elevation = currentEle
                                    )
                                )
                                inWpt = false
                            }
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        
        return GpxData(trackPoints, waypoints)
    }
}
