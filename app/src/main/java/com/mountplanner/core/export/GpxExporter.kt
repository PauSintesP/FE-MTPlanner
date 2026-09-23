package com.mountplanner.core.export

import android.content.Context
import com.mountplanner.data.model.LocationPoint
import dagger.hilt.android.qualifiers.ApplicationContext
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
class GpxExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun exportToGpx(
        expeditionId: String,
        expeditionName: String,
        points: List<LocationPoint>
    ): File = withContext(Dispatchers.IO) {
        val safeName = expeditionName.replace(" ", "_").replace("/", "_")
        val filename = "${safeName}_$expeditionId.gpx"
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
                writer.write("      <trkpt lat=\"${point.lat}\" lon=\"${point.lng}\">\n")
                if (point.altitudeM != null) {
                    writer.write("        <ele>${point.altitudeM}</ele>\n")
                }
                writer.write("        <time>${dateFormat.format(Date(point.capturedAt))}</time>\n")
                writer.write("      </trkpt>\n")
            }
            
            writer.write("    </trkseg>\n")
            writer.write("  </trk>\n")
            writer.write("</gpx>")
        }

        file
    }
}
