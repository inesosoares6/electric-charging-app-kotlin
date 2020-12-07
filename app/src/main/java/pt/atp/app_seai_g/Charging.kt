package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Charging : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging)

        val cancelCharge = findViewById<Button>(R.id.cancelcharge)
        cancelCharge.setOnClickListener{
            val intent = Intent(this, ConfirmCancel::class.java)
            startActivity(intent)
        }
    }
}