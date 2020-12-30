package pt.atp.app_seai_g

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import pt.atp.app_seai_g.Data.Request
import java.time.LocalDateTime

// Activity to charge the vehicle
//   - communication with control module

class ActivityCharging : AppCompatActivity() {

    private lateinit var chargingPage: View
    private lateinit var chargingModePage: View
    private lateinit var confirmCancelPage: View
    private lateinit var finishedPage: View

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_charging)

        //TODO wait for message of charge finished

        val bb: Bundle? = intent.extras
        val chargerID = findViewById<TextView>(R.id.chargerId)
        chargerID.text = bb?.getString("chargerID")
        val apiID = bb?.getString("chargerID")

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

        val normalPrice = findViewById<TextView>(R.id.priceNormal)
        val premiumPrice = findViewById<TextView>(R.id.priceFast)
        val greenPrice = findViewById<TextView>(R.id.priceGreen)

        val url = "http://127.0.0.1:5000/pricesAPP/$apiID"
       // getPrices(url)
        var message: String?

        doAsync {
            message = Request(url).run()
            try {
                // get JSONObject from JSON file
                val obj = JSONObject(message)
                // fetch JSONObject named employee
                val prices: JSONObject = obj.getJSONObject("prices")
                // get employee name and salary
                val priceNormal = prices.getString("normal")
                val pricePremium = prices.getString("premium")
                val priceGreen = prices.getString("green")
                normalPrice.text = getString(R.string.textPriceNormal)+" "+priceNormal+" €/kWh"
                premiumPrice.text = getString(R.string.textPriceFast)+" "+pricePremium+"€/kWh"
                greenPrice.text = getString(R.string.textPriceGreen)+" "+priceGreen+"€/kWh"
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        val chargeNormal = findViewById<Button>(R.id.chargeNormal)
        chargeNormal.setOnClickListener{
            val url = "http://127.0.0.1:5000/normal/$apiID"
            var message: String?

            doAsync {
                message = Request(url).run()
            }

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
            val url = "http://127.0.0.1:5000/premium/$apiID"
            var message: String?

            doAsync {
                message = Request(url).run()
            }

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
            val url = "http://127.0.0.1:5000/green/$apiID"
            var message: String?

            doAsync {
                message = Request(url).run()
            }

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
            val url = "http://127.0.0.1:5000/stop/$apiID"
            var message: String?

            doAsync {
                message = Request(url).run()
            }

            confirmCancelPage.visibility=View.GONE
            finishedPage.visibility=View.VISIBLE
            timeFinished = LocalDateTime.now()
            sendNotification()
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

            //Codigo de acesso ao valor do preco final

            /*val url = "http://127.0.0.1:5000/finalpriceAPP/$apiID"
            var message: String?

            doAsync {
                message = Request(url).run()
            }*/

            //TODO update price in database
            val charger = hashMapOf(
                "idCharger" to bb?.getString("chargerID"),
                "dayHour" to (day+"-"+month+"-"+year+"  "+hour+"h"+minute+"min"),
                "time" to java.time.Duration.between(timeStarted,timeFinished).toMinutes().toString(),
                "price" to 0,
                "type" to type
            )
            val docName: String = if((numCharges+1)<10){
                "00"+(numCharges+1).toString()
            } else if ((numCharges+1)>9 && (numCharges+1)<100){
                "0"+(numCharges+1).toString()
            } else{
                (numCharges+1).toString()
            }
            mAuth.currentUser?.email?.let { it1 ->
                db.collection("users").document(it1).collection("charges").document(docName)
                    .set(charger)
                db.collection("users").document(it1).collection("lastCharge").document("last").set(charger)
            }
        }
    }

    private fun sendNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setContentTitle("Vehicle charged")
                .setContentText("Your vehicle is charged, thank you for your preference!")
                .setSmallIcon(R.mipmap.ic_launcher)
        }else{

            builder = Notification.Builder(this)
                .setContentTitle("Vehicle charged")
                .setContentText("Your vehicle is charged, thank you for your preference!")
                .setSmallIcon(R.mipmap.ic_launcher)
        }
        notificationManager.notify(1234,builder.build())
    }

    private fun getPrices(url: String){
        var message: String?
        val normalPrice = findViewById<TextView>(R.id.priceNormal)
        val premiumPrice = findViewById<TextView>(R.id.priceFast)
        val greenPrice = findViewById<TextView>(R.id.priceGreen)

        doAsync {
            message = Request(url).run()
            try {
                // get JSONObject from JSON file
                val obj = JSONObject(message)
                // fetch JSONObject named employee
                val prices: JSONObject = obj.getJSONObject("prices")
                // get employee name and salary
                val priceNormal = prices.getString("normal")
                val pricePremium = prices.getString("premium")
                val priceGreen = prices.getString("green")
                normalPrice.text = getString(R.string.textPriceNormal)+" "+priceNormal+" €/kWh"
                premiumPrice.text = getString(R.string.textPriceFast)+" "+pricePremium+"€/kWh"
                greenPrice.text = getString(R.string.textPriceGreen)+" "+priceGreen+"€/kWh"
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed(){}
}
