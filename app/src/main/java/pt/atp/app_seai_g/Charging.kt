package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_charging.*

class Charging : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging)
        
        cancelcharge.setOnClickListener{
            val intent = Intent(this, ConfirmCancel::class.java)
            startActivity(intent)
        }
    }
}