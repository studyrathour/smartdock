package suraj.veera.launcher.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import suraj.veera.launcher.R

class KeyboardPreferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(arg0: Bundle?, arg1: String?) {
        setPreferencesFromResource(R.xml.preferences_keyboard, arg1)
    }
}
