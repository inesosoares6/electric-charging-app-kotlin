package pt.atp.app_seai_g

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

// Activity of settings

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

        private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val onOffRandomColor: SwitchPreferenceCompat? = findPreference("darkTheme")
            //val onOffRandomColor = findPreference("darkTheme") as SwitchPreference?

            // SwitchPreference preference change listener
            onOffRandomColor!!.setOnPreferenceChangeListener { _, _ ->
                if (onOffRandomColor.isChecked) {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show()

                    // Checked the switch programmatically
                    onOffRandomColor.isChecked = false
                } else {
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()

                    // Unchecked the switch programmatically
                    onOffRandomColor.isChecked = true
                }
                false
            }
            /*preferenceChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    when (val preference = findPreference<PreferenceCategory>(key)) {
                        is SwitchPreferenceCompat -> {
                            if (key == "darkTheme") {
                                Toast.makeText(context, "darkTheme : " + preference.isChecked, Toast.LENGTH_LONG).show()
                            }
                        }
                        is CheckBoxPreference -> {
                            // do sth else
                        }
                        is ListPreference -> {
                            // do sth else
                        }
                    }
                }*/
        }
    }
}