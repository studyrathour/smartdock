package cu.axel.smartdock.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.Locale

object ThemeUtils {
    private const val TAG = "ThemeUtils"
    // Cache for file lists to avoid repeated asset listing
    private val folderCache = mutableMapOf<String, List<String>>()

    fun getThemeIconPath(context: Context, themeName: String, appLabel: String): String? {
        if (themeName == "system" || themeName.isEmpty()) return null

        val fullThemePath = when (themeName) {
            "clear" -> "themes/clear/png"
            "dark" -> "themes/dark/png"
            "default" -> "themes/default/png"
            else -> return null
        }

        try {
            val files = getAssetFiles(context, fullThemePath)
            val normalizedLabel = appLabel.lowercase(Locale.ROOT).replace(" ", "")

            val match = files.find { fileName ->
                val normalizedFile = fileName.lowercase(Locale.ROOT)
                normalizedFile.startsWith(normalizedLabel + "@") ||
                normalizedFile.startsWith(normalizedLabel + ".") ||
                normalizedFile == "$normalizedLabel.png" ||
                mapCommonApps(normalizedLabel, normalizedFile)
            }

            if (match != null) {
                // Coil expects file:///android_asset/path
                return "file:///android_asset/$fullThemePath/$match"
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error finding theme icon for $appLabel in $themeName", e)
        }
        return null
    }

    private fun getAssetFiles(context: Context, path: String): List<String> {
        if (folderCache.containsKey(path)) {
            return folderCache[path]!!
        }

        return try {
            val list = context.assets.list(path)?.toList() ?: emptyList()
            folderCache[path] = list
            list
        } catch (e: IOException) {
            emptyList()
        }
    }

    private fun mapCommonApps(label: String, fileName: String): Boolean {
        // Manual mapping for some common apps if names differ significantly
        if (label == "settings" && fileName.startsWith("systempreferences")) return true
        if (label == "playstore" && fileName.startsWith("appstore")) return true
        if (label == "chrome" && fileName.startsWith("safari")) return true
        if (label == "camera" && fileName.startsWith("camera")) return true // standard
        if (label == "photos" && fileName.startsWith("photos")) return true
        return false
    }
}
