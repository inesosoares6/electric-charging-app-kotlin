package pt.atp.app_seai_g

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// Activity of settings

var darkMode: Boolean = false

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

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val db = FirebaseFirestore.getInstance()
            val mAuth: FirebaseAuth?
            mAuth=FirebaseAuth.getInstance()
            val switchTheme: SwitchPreferenceCompat? = findPreference("darkTheme")
            switchTheme!!.setOnPreferenceChangeListener { _, _ ->
                if (switchTheme.isChecked) {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show()
                    switchTheme.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkMode=true
                } else {
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()
                    switchTheme.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    darkMode=false
                }
                false
            }

            var name = ""
            mAuth.currentUser?.email?.let {
                db.collection("users").document(it).get()
                        .addOnSuccessListener { result ->
                            name=result["name"].toString()
                            //Toast.makeText(context, result["name"].toString(), Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, getString(R.string.error_name), Toast.LENGTH_LONG).show()
                        }
            }
            val countingPreference: EditTextPreference? = findPreference("signature")
            countingPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    name
                } else {
                    //TODO go find the name in database
                    mAuth.currentUser?.email?.let {
                        db.collection("users").document(it).update(mapOf(
                                "name" to text
                        ))
                    }
                    text
                }
            }
            val switchLanguage: ListPreference? = findPreference("language")
            switchLanguage?.setOnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    val index = preference.findIndexOfValue(newValue.toString())
                    //val entry = preference.entries[index]
                    val entryValue = preference.entryValues[index]
                    if(entryValue=="portuguese"){
                        //TODO change language to Portuguese
                        Locale("por")
                        Toast.makeText(context, "POR", Toast.LENGTH_SHORT).show()
                    } else{
                        //TODO change Language to English
                        Locale("en")
                        Toast.makeText(context, "EN", Toast.LENGTH_SHORT).show()
                    }
                }

                true
            }
        }
    }
}