package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_charging.*
import kotlinx.android.synthetic.main.activity_charging_mode.*

class ChargingMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging_mode)

        val bb: Bundle? = intent.extras
        chargerid.text = bb!!.getString("chargerID")

        chargenormal.setOnClickListener{
            //TODO send info to control: charging mode NORMAL
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }

        chargefast.setOnClickListener{
            //TODO send info to control: charging mode FAST
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }
    }
}