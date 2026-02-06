package cu.axel.smartdock.utils

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ZoomTouchListener(private val recyclerView: RecyclerView) : View.OnTouchListener {
    var enabled = true
    private val MAX_SCALE = 1.5f
    private val MIN_SCALE = 1.0f
    private val ZOOM_RADIUS = 300f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!enabled) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Adjust touch X based on scroll offset is not needed because getChildAt returns relative views
                // But touchX is relative to the RecyclerView (v).
                // child.left is also relative to RecyclerView content? No, relative to parent view.
                // Since RecyclerView scrolls, child.left changes relative to RecyclerView viewport?
                // Actually child.left is relative to RecyclerView (the view group).
                // So if we scrolled, child.left might be negative. This matches event.x which is relative to Viewport.
                applyZoom(event.x)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                resetZoom()
            }
        }
        // Return false to let RecyclerView handle scroll?
        // If we return true, we consume it and scroll might not work.
        // If we return false, scroll works, but does zoom update smoothly?
        // Yes, ACTION_MOVE will still be fired.
        return false
    }

    private fun applyZoom(touchX: Float) {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            if (child == null) continue
            // Scale from bottom
            child.pivotY = child.height.toFloat()
            child.pivotX = child.width / 2f

            val childCenter = child.left + child.width / 2f
            val dist = abs(touchX - childCenter)

            var scale = MIN_SCALE
            if (dist < ZOOM_RADIUS) {
                 scale = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * (1 - dist / ZOOM_RADIUS)
            }

            child.scaleX = scale
            child.scaleY = scale
        }
    }

    private fun resetZoom() {
         for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            if (child.scaleX != 1.0f) {
                child.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }
        }
    }
}
