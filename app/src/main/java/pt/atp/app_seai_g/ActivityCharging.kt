package pt.atp.app_seai_g

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

// Activity to charge the vehicle
//   - communication with control module

class ActivityCharging : AppCompatActivity() {

    private lateinit var chargingPage: View
    private lateinit var chargingModePage: View
    private lateinit var confirmCancelPage: View
    private lateinit var finishedPage: View


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_charging)

        val bb: Bundle? = intent.extras
        val chargerID = findViewById<TextView>(R.id.chargerId)
        chargerID.text = bb?.getString("chargerID")

        chargingPage = findViewById(R.id.chargingPage)
        chargingModePage = findViewById(R.id.chargingModePage)
        confirmCancelPage = findViewById(R.id.confirmCancelPage)
        finishedPage = findViewById(R.id.finishedPage)

        // Update numCharges in database
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        var numCharges = 0
        var timeStarted: LocalDateTime = LocalDateTime.now()
        var timeFinished: LocalDateTime = LocalDateTime.now()
        var day = ""
        var month = ""
        var year = ""
        var hour = ""
        var minute = ""
        var type = ""
        mAuth= FirebaseAuth.getInstance()

        val chargeNormal = findViewById<Button>(R.id.chargeNormal)
        chargeNormal.setOnClickListener{
            //TODO send info to control: id, charging mode NORMAL
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            timeStarted = LocalDateTime.now()
            day = timeStarted.dayOfMonth.toString()
            month = timeStarted.monthValue.toString()
            year = timeStarted.year.toString()
            hour = timeStarted.hour.toString()
            minute = timeStarted.minute.toString()
            type = "Normal"
        }

        val chargeFast = findViewById<Button>(R.id.chargeFast)
        chargeFast.setOnClickListener{
            //TODO send info to control: id, charging mode FAST
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            timeStarted = LocalDateTime.now()
            day = timeStarted.dayOfMonth.toString()
            month = timeStarted.monthValue.toString()
            year = timeStarted.year.toString()
            hour = timeStarted.hour.toString()
            minute = timeStarted.minute.toString()
            type = getString(R.string.fast)
        }

        val chargeGreen = findViewById<Button>(R.id.chargeGreen)
        chargeGreen.setOnClickListener{
            //TODO send info to control: id, charging mode GREEN
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            timeStarted = LocalDateTime.now()
            day = timeStarted.dayOfMonth.toString()
            month = timeStarted.monthValue.toString()
            year = timeStarted.year.toString()
            hour = timeStarted.hour.toString()
            minute = timeStarted.minute.toString()
            type = getString(R.string.green)
        }

        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener{
            val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
            finish()
        }

        val cancelCharge = findViewById<Button>(R.id.cancelCharge)
        cancelCharge.setOnClickListener{
            chargingPage.visibility=View.GONE
            confirmCancelPage.visibility=View.VISIBLE
        }

        val continueCharging = findViewById<Button>(R.id.continueCharging)
        continueCharging.setOnClickListener{
            confirmCancelPage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener{
            //TODO send info to control: id, cancel charging
            confirmCancelPage.visibility=View.GONE
            finishedPage.visibility=View.VISIBLE
            timeFinished = LocalDateTime.now()

        }

        val returnHomepage = findViewById<Button>(R.id.returnHomepage)
        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        numCharges=result["numCharges"].toString().toInt()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Error getting numCharges", Toast.LENGTH_LONG).show()
                    }
        }
        returnHomepage.setOnClickListener{
           val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
            mAuth.currentUser?.email?.let {
                db.collection("users").document(it).update(mapOf(
                        "numCharges" to (numCharges+1)
                ))
            }
            val charger = hashMapOf(
                "idCharger" to bb?.getString("chargerID"),
                "dayHour" to (day+"-"+month+"-"+year+"  "+hour+"h"+minute+"min"),
                "time" to java.time.Duration.between(timeStarted,timeFinished).toMinutes().toString(),
//                "timeStarted" to timeStarted,
//                "timeFinished" to timeFinished,
                "price" to 0,
                "type" to type
            )
            //TODO update price in database
            mAuth.currentUser?.email?.let { it1 ->
                db.collection("users").document(it1).collection("charges").document((numCharges+1).toString())
                    .set(charger)
                db.collection("users").document(it1).collection("lastCharge").document("last").set(charger)
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}