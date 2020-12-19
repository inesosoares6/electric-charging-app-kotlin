package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Activity to charge the vehicle
//   - communication with control module

class ActivityCharging : AppCompatActivity() {

    private lateinit var chargingPage: View
    private lateinit var chargingModePage: View
    private lateinit var confirmCancelPage: View
    private lateinit var finishedPage: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_charging)

        val bb: Bundle? = intent.extras
        val chargerID = findViewById<TextView>(R.id.chargerid)
        chargerID.text = bb!!.getString("chargerID")

        chargingPage = findViewById(R.id.chargingPage)
        chargingModePage = findViewById(R.id.chargingModePage)
        confirmCancelPage = findViewById(R.id.confirmCancelPage)
        finishedPage = findViewById(R.id.finishedPage)

        val chargeNormal = findViewById<Button>(R.id.chargenormal)
        chargeNormal.setOnClickListener{
            //TODO send info to control: id, charging mode NORMAL
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        val chargeFast = findViewById<Button>(R.id.chargefast)
        chargeFast.setOnClickListener{
            //TODO send info to control: id, charging mode FAST
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        val chargeGreen = findViewById<Button>(R.id.chargeGreen)
        chargeGreen.setOnClickListener{
            //TODO send info to control: id, charging mode GREEN
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener{
            val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
            finish()
        }

        val cancelCharge = findViewById<Button>(R.id.cancelcharge)
        cancelCharge.setOnClickListener{
            chargingPage.visibility=View.GONE
            confirmCancelPage.visibility=View.VISIBLE
        }

        val continueCharging = findViewById<Button>(R.id.continuecharging)
        continueCharging.setOnClickListener{
            confirmCancelPage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener{
            //TODO send info to control: id, cancel charging
            confirmCancelPage.visibility=View.GONE
            finishedPage.visibility=View.VISIBLE
        }

        val returnHomepage = findViewById<Button>(R.id.returnHomepage)
        returnHomepage.setOnClickListener{
           val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}