package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

// Activity of settings

var darkMode: Boolean = false

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val switchTheme: SwitchPreferenceCompat? = findPreference("darkTheme")
            switchTheme!!.setOnPreferenceChangeListener { _, _ ->
                if (switchTheme.isChecked) {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show()
                    switchTheme.isChecked = false
                    darkMode=true
                } else {
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()
                    switchTheme.isChecked = true
                    darkMode=false
                }
                false
            }
        }
    }
}