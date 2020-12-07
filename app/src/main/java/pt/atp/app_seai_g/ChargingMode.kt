package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChargingMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging_mode)

        val bb: Bundle? = intent.extras
        val chargerID = findViewById<TextView>(R.id.chargerid)
        chargerID.text = bb!!.getString("chargerID")

        val chargeNormal = findViewById<Button>(R.id.chargenormal)
        chargeNormal.setOnClickListener{
            //TODO send info to control: charging mode NORMAL
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }

        val chargeFast = findViewById<Button>(R.id.chargefast)
        chargeFast.setOnClickListener{
            //TODO send info to control: charging mode FAST
            val intent = Intent(this, Charging::class.java)
            startActivity(intent)
        }
    }
}