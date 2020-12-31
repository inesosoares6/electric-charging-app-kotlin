package pt.atp.app_seai_g

import android.annotation.SuppressLint
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
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

    private val normalPrice: TextView = findViewById(R.id.priceNormal)
    private val premiumPrice: TextView = findViewById(R.id.priceFast)
    private val greenPrice: TextView = findViewById(R.id.priceGreen)
    private val totalPrice: TextView = findViewById(R.id.textTotalPrice)

    private var message: String? = null

    private var timeStarted: LocalDateTime? = null
    private var timeFinished: LocalDateTime? = null
    private var day = ""
    private var month = ""
    private var year = ""
    private var hour = ""
    private var minute = ""
    private var type = ""
    private var priceTotalDB: String? = null


    @SuppressLint("SetTextI18n")
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

        mAuth= FirebaseAuth.getInstance()

        // Get prices of charging modes to show in page
       getPrices("http://127.0.0.1:5000/pricesAPP/$apiID")

        // Regular charging mode
        val chargeNormal = findViewById<Button>(R.id.chargeNormal)
        chargeNormal.setOnClickListener{
            // send charging mode to control
            doAsync {
                Request("http://127.0.0.1:5000/normal/$apiID").run()
            }
            // update page visible
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            // save variables to use in database
            saveInitialValues(getString(R.string.regular_mode))
        }

        // Fast charging mode
        val chargeFast = findViewById<Button>(R.id.chargeFast)
        chargeFast.setOnClickListener{
            // TODO verify if fast charging is available
            // send charging mode to control
            doAsync {
                Request("http://127.0.0.1:5000/premium/$apiID").run()
            }
            // update page visible
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            // save variables to use in database
            saveInitialValues(getString(R.string.fast))
        }

        // Green charging mode
        val chargeGreen = findViewById<Button>(R.id.chargeGreen)
        chargeGreen.setOnClickListener{
            // TODO verify if green charging is available
            // send charging mode to control
            doAsync {
                Request("http://127.0.0.1:5000/green/$apiID").run()
            }
            // update page visible
            chargingModePage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
            timeStarted = LocalDateTime.now()
            // save variables to use in database
            saveInitialValues(getString(R.string.green))
        }

        // Return to welcome page, the client doesn't want to charge the vehicle
        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener{
            val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
            finish()
        }

        // Confirm that the client wants to cancel the charge
        val cancelCharge = findViewById<Button>(R.id.cancelCharge)
        cancelCharge.setOnClickListener{
            chargingPage.visibility=View.GONE
            confirmCancelPage.visibility=View.VISIBLE
        }

        // The client wants to continue charging the vehicle
        val continueCharging = findViewById<Button>(R.id.continueCharging)
        continueCharging.setOnClickListener{
            confirmCancelPage.visibility=View.GONE
            chargingPage.visibility=View.VISIBLE
        }

        // The client canceled the vehicle
        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener{
            doAsync {
                Request("http://127.0.0.1:5000/stop/$apiID").run()
            }
            // update page visible
            confirmCancelPage.visibility=View.GONE
            finishedPage.visibility=View.VISIBLE
            // send info to control, send notification and save data for database
            chargeFinished("http://127.0.0.1:5000/finalpriceAPP/$apiID")
        }

        // Save charge in database before returning home
        val returnHomepage = findViewById<Button>(R.id.returnHomepage)
        // get number of charges done to save the document in the correct order
        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        numCharges=result["numCharges"].toString().toInt()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.error_getting_numCharges), Toast.LENGTH_LONG).show()
                    }
        }
        returnHomepage.setOnClickListener{
            // get number of charges to use in document name
            mAuth.currentUser?.email?.let {
                db.collection("users").document(it).update(mapOf("numCharges" to (numCharges+1)))
            }
            // values to save in database
            val charger = hashMapOf(
                "idCharger" to bb?.getString("chargerID"),
                "dayHour" to (day+"-"+month+"-"+year+"  "+hour+"h"+minute+"min"),
                "time" to java.time.Duration.between(timeStarted,timeFinished).toMinutes().toString(),
                "price" to priceTotalDB,
                "type" to type
            )
            // add zeros before the number so that the documents stay in order to write in historic
            val docName: String = if((numCharges+1)<10){
                "00"+(numCharges+1).toString()
            } else if ((numCharges+1)>9 && (numCharges+1)<100){
                "0"+(numCharges+1).toString()
            } else{
                (numCharges+1).toString()
            }
            // save charge to historic and last charge
            mAuth.currentUser?.email?.let { it1 ->
                db.collection("users").document(it1).collection("charges").document(docName).set(charger)
                db.collection("users").document(it1).collection("lastCharge").document("last").set(charger)
            }
            // return to welcome page
            val intent = Intent(this, ActivityWelcome::class.java)
            startActivity(intent)
        }
    }

    // Send notification when finished
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
                .setContentTitle(getString(R.string.vehicleCharged))
                .setContentText(getString(R.string.vehicleChargedMessage))
                .setSmallIcon(R.mipmap.ic_launcher)
        }else{

            builder = Notification.Builder(this)
                .setContentTitle(getString(R.string.vehicleCharged))
                .setContentText(getString(R.string.vehicleChargedMessage))
                .setSmallIcon(R.mipmap.ic_launcher)
        }
        notificationManager.notify(1234,builder.build())
    }

    // Get prices to display in the screen
    @SuppressLint("SetTextI18n")
    private fun getPrices(url: String){
        var message: String?
        doAsync {
            message = Request(url).run()
            try {
                val obj = JSONObject(message.toString())
                val priceNormal = obj.getString("normal")
                val pricePremium = obj.getString("premium")
                val priceGreen = obj.getString("green")
                normalPrice.text = getString(R.string.textPriceNormal) + " " + priceNormal + " €/kWh"
                premiumPrice.text = getString(R.string.textPriceFast) + " " + pricePremium + "€/kWh"
                greenPrice.text = getString(R.string.textPriceGreen) + " " + priceGreen + "€/kWh"
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    // Save initial values to save to database
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveInitialValues(typeCharge: String){
        // save variables to use in database
        timeStarted = LocalDateTime.now()
        day = timeStarted!!.dayOfMonth.toString()
        month = timeStarted!!.monthValue.toString()
        year = timeStarted!!.year.toString()
        hour = timeStarted!!.hour.toString()
        minute = timeStarted!!.minute.toString()
        type = typeCharge
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun chargeFinished(url: String){
        // save variables to use in database
        timeFinished = LocalDateTime.now()
        sendNotification()
        // get final price to pay
        doAsync {
            message = Request(url).run()
            try {
                val obj = JSONObject(message.toString())
                val priceTotal = obj.getString("total")
                totalPrice.text = getString(R.string.totalPrice) + " " + priceTotal + " €"
                priceTotalDB = priceTotal
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed(){}
}
