package cu.axel.smartdock.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.preference.PreferenceManager
import java.io.InputStream

object CustomIconUtils {
    private const val PREF_PREFIX = "custom_icon_"

    fun getCustomIconPath(context: Context, packageName: String): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_PREFIX + packageName, null)
    }

    fun setCustomIcon(context: Context, packageName: String, uri: Uri) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(PREF_PREFIX + packageName, uri.toString()).apply()
    }

    fun removeCustomIcon(context: Context, packageName: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove(PREF_PREFIX + packageName).apply()
    }

    fun getCustomIconDrawable(context: Context, path: String): Drawable? {
        return try {
             val uri = Uri.parse(path)
             val stream: InputStream? = context.contentResolver.openInputStream(uri)
             val drawable = Drawable.createFromStream(stream, null)
             stream?.close()
             drawable
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
