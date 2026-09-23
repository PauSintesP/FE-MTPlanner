package com.mountplanner.core.export

import android.content.Context
import com.mountplanner.data.local.entity.LocationPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class GpxExporter @Inject constructor(private val context: Context) {

    suspend fun exportToGpx(
        expeditionId: String,
        expeditionName: String,
        points: List<LocationPoint>
    ): File = withContext(Dispatchers.IO) {
        val filename = "${expeditionName.replace(" ", "_")}_$expeditionId.gpx"
        val file = File(context.getExternalFilesDir(null), filename)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        FileOutputStream(file).bufferedWriter().use { writer ->
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<gpx version=\"1.1\" creator=\"MountPlanner\">\n")
            
            writer.write("  <trk>\n")
            writer.write("    <name>$expeditionName</name>\n")
            writer.write("    <trkseg>\n")
            
            for (point in points) {
                writer.write("      <trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\">\n")
                if (point.elevation != null) {
                    writer.write("        <ele>${point.elevation}</ele>\n")
                }
                writer.write("        <time>${dateFormat.format(Date(point.timestamp))}</time>\n")
                writer.write("      </trkpt>\n")
            }
            
            writer.write("    </trkseg>\n")
            writer.write("  </trk>\n")
            writer.write("</gpx>")
        }

        file
    }
}
