package pt.atp.app_seai_g

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_charging_mode.*

class ChargingMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging_mode)

        val bb: Bundle? = intent.extras
        chargerid.text = bb!!.getString("chargerID")

    }
}