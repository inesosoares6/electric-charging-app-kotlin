package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ConfirmCancel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_cancel)

        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener{
            //TODO send info to control: cancel charging
            val intent = Intent(this, Finished::class.java)
            startActivity(intent)
        }

        val continueCharging = findViewById<Button>(R.id.continuecharging)
        continueCharging.setOnClickListener{
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }
    }
}