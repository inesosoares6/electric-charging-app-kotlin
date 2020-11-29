package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_charging_mode.*
import kotlinx.android.synthetic.main.activity_charging_mode.chargenormal
import kotlinx.android.synthetic.main.activity_confirm_cancel.*

class ConfirmCancel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_cancel)

        cancel.setOnClickListener{
            //TODO send info to control: cancel charging
            val intent = Intent(this, Finished::class.java)
            startActivity(intent)
        }

        continuecharging.setOnClickListener{
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }
    }
}