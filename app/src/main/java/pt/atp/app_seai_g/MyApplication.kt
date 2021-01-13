package pt.atp.app_seai_g

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.*


class MyApplication : Application() {

    companion object{
        // every time we run it needs to be updated
        val urlStart : String = "http://36af6abd2019.ngrok.io"  // Forwarding: http://e4ea0cfe831a.ngrok.io -> http://localhost:5000
    }

    override fun onCreate() {
        super.onCreate()
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lang = preferences.getString("lang", "en")
        val locale = Locale(lang.toString())
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}