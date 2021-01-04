package pt.atp.app_seai_g.Data

import android.util.Log
import java.net.URL


class Request(private val url: String) {

    fun run(): String {
        val repoListJsonStr = URL(url).readText()
        Log.d(javaClass.simpleName, repoListJsonStr)
        return repoListJsonStr
    }
}