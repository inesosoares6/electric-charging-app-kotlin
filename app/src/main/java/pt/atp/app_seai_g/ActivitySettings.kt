package pt.atp.app_seai_g

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// Activity of settings

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if(id==android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var myLocale: Locale

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val db = FirebaseFirestore.getInstance()
            val mAuth: FirebaseAuth?
            mAuth=FirebaseAuth.getInstance()
            val switchTheme: SwitchPreferenceCompat? = findPreference("darkTheme")
            switchTheme!!.setOnPreferenceChangeListener { _, _ ->
                if (switchTheme.isChecked) {
                    switchTheme.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    switchTheme.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false
            }

            val countingPreference: EditTextPreference? = findPreference("signature")
            countingPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    getNameFromDatabase(db, mAuth)
                } else {
                    mAuth.currentUser?.email?.let {
                        db.collection("users").document(it).update(mapOf(
                                "name" to text
                        ))
                    }
                    getNameFromDatabase(db, mAuth)
                }
            }
            val switchLanguage: ListPreference? = findPreference("language")
            switchLanguage?.setOnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    val index = preference.findIndexOfValue(newValue.toString())
                    val entryValue = preference.entryValues[index]
                    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                    if(entryValue=="portuguese"){
                        Toast.makeText(context, getString(R.string.languagePOR), Toast.LENGTH_SHORT).show()
                        setLocale("pt")
                        preferences.edit().putString("lang", "pt").apply()
                    } else{
                        Toast.makeText(context, getString(R.string.languageEN), Toast.LENGTH_SHORT).show()
                        setLocale("en")
                        preferences.edit().putString("lang", "en").apply()
                    }
                }
                true
            }
        }

        private fun setLocale(lang: String) {
            myLocale = Locale(lang)
            val res: Resources = resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(context, ActivityWelcome::class.java)
            startActivity(refresh)
        }

        private fun getNameFromDatabase(db: FirebaseFirestore, mAuth: FirebaseAuth): String {
            var name = ""
            mAuth.currentUser?.email?.let {
                db.collection("users").document(it).get()
                        .addOnSuccessListener { result ->
                            name = result["name"].toString()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, getString(R.string.error_name), Toast.LENGTH_LONG).show()
                        }
            }
            return name
        }
    }
}