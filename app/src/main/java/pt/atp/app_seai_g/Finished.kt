package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Finished : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finished)

        val returnHomepage = findViewById<Button>(R.id.returnHomepage)
        returnHomepage.setOnClickListener{
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
        }
    }
}