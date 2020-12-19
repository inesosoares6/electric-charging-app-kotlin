package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

// Activity of settings

var darkMode: Boolean = false

class SettingsActivity : AppCompatActivity() {

    companion object{
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener{ preference, value ->
            val stringValue = value.toString()
            if(preference is ListPreference){
                val index = preference.findIndexOfValue(stringValue)
                preference.setSummary(
                        if(index >= 0) preference.entries[index]
                        else null
                )
            } else {
                preference.summary = stringValue
            }
            true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference?){
            if (preference != null) {
                preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,PreferenceManager.getDefaultSharedPreferences(preference.context).getString(preference.key,""))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.root_preferences)

            bindPreferenceSummaryToValue(findPreference("signature"))
            bindPreferenceSummaryToValue(findPreference("vehicle"))
            bindPreferenceSummaryToValue(findPreference("brand"))
            bindPreferenceSummaryToValue(findPreference("model"))
            bindPreferenceSummaryToValue(findPreference("darkTheme"))
            bindPreferenceSummaryToValue(findPreference("language"))
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val switchTheme: SwitchPreference? = findPreference("darkTheme")
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