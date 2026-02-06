package cu.axel.smartdock.services

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import cu.axel.smartdock.R

class StatusBarOverlay(private val context: Context) {
    private var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var statusBarView: View? = null
    private var params: WindowManager.LayoutParams? = null

    fun show() {
        if (statusBarView != null) return

        statusBarView = LayoutInflater.from(context).inflate(R.layout.status_bar, null)

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        params?.gravity = Gravity.TOP

        try {
            windowManager.addView(statusBarView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        if (statusBarView == null) return
        try {
            windowManager.removeView(statusBarView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        statusBarView = null
    }

    fun updateAppName(name: String) {
        val tv = statusBarView?.findViewById<TextView>(R.id.sb_app_name)
        if (tv != null) {
            tv.text = name
        }
    }

    fun setVisibility(visible: Boolean) {
        if (statusBarView != null) {
            statusBarView?.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    fun isVisible(): Boolean {
        return statusBarView?.visibility == View.VISIBLE
    }
}
