package cu.axel.smartdock.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cu.axel.smartdock.R
import cu.axel.smartdock.adapters.SettingCategory
import cu.axel.smartdock.adapters.SettingsSidebarAdapter
import cu.axel.smartdock.fragments.*

class MacOSSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macos_settings)

        showNagScreen()

        val categories = listOf(
            SettingCategory(0, "Dock & Menu Bar", R.drawable.ic_dock, DockPreferences::class.java),
            SettingCategory(1, "Launchpad & Apps", R.drawable.ic_apps_menu, AppMenuPreferences::class.java),
            SettingCategory(2, "Mission Control", R.drawable.ic_desktop, DesktopPreferences::class.java), // Reusing desktop prefs
            SettingCategory(3, "Appearance", R.drawable.ic_appearance, AppearancePreferences::class.java),
            SettingCategory(4, "Notifications", R.drawable.ic_notifications, NotificationPreferences::class.java),
            SettingCategory(5, "Keyboard", R.drawable.ic_keyboard, KeyboardPreferences::class.java),
            SettingCategory(6, "Hot Corners", R.drawable.ic_corners, HotCornersPreferences::class.java),
            SettingCategory(7, "Advanced", R.drawable.ic_advanced, AdvancedPreferences::class.java),
            SettingCategory(8, "About", R.drawable.ic_info, HelpAboutPreferences::class.java)
        )

        val sidebar = findViewById<RecyclerView>(R.id.settings_sidebar)
        sidebar.layoutManager = LinearLayoutManager(this)
        val adapter = SettingsSidebarAdapter(categories) { category ->
            val fragment = category.fragmentClass.newInstance() as androidx.fragment.app.Fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_content_frame, fragment)
                .commit()
        }
        sidebar.adapter = adapter

        // Load initial fragment
        if (savedInstanceState == null) {
            val initialFragment = DockPreferences()
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_content_frame, initialFragment)
                .commit()
        }
    }

    private fun showNagScreen() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Contact Developer")
            .setMessage("For support, customization requests, or feedback, please contact the owner on Telegram.")
            .setCancelable(false)
            .setPositiveButton("Contact Owner") { _, _ ->
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, "tg://resolve?domain=smartdock358".toUri()))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, "https://t.me/smartdock358".toUri()))
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }
}
