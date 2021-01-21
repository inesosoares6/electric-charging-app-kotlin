package pt.atp.app_seai_g

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import pt.atp.app_seai_g.Data.Request

// Activity to read NFC

const val MIME_TEXT_PLAIN = "text/plain"

class ReceiverActivity : AppCompatActivity() {

    private var nfcIDCharger: TextView? = null
    private var nfcAdapter: NfcAdapter? = null
//    private val isNfcSupported: Boolean = this.nfcAdapter != null
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver_nfc)
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        /*if (!isNfcSupported) {
            Toast.makeText(this, getString(R.string.nfcNotSupported), Toast.LENGTH_SHORT).show()
            this.finish()
        }*/
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, getString(R.string.nfcDisabled), Toast.LENGTH_SHORT).show()
        }
        initViews()
    }

    private fun initViews() {
        this.nfcIDCharger = findViewById(R.id.nfcMessage)
    }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        receiveMessageFromDevice(intent)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType(MIME_TEXT_PLAIN)
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException(getString(R.string.mimeType))
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if(NfcAdapter.ACTION_NDEF_DISCOVERED==action){
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables){
                val inNDefMessage = this?.get(0) as NdefMessage
                val inNefRecords = inNDefMessage.records
                val nDefRecord0 = inNefRecords[0]
                val inMessage = String(nDefRecord0.payload)
                nfcIDCharger?.text = inMessage
                confirmIdCharger(inMessage)
            }
        }
    }
    private fun confirmIdCharger(chargerID: String){
        doAsync {
            message = Request("${MyApplication.urlStart}/readytocharge/$chargerID").run()
            uiThread{
                val obj = JSONObject(message.toString())
                val flag = obj.getString("flag")
                //Toast.makeText(applicationContext,flag,Toast.LENGTH_LONG).show()
                if (flag=="1"){
                    sendID(chargerID)
                } else {
                    Toast.makeText(applicationContext,getString(R.string.insert_valid_id1) +" " + chargerID + " " + getString(R.string.insert_valid_id2),Toast.LENGTH_LONG).show()
                    returnToWelcome()
                }
            }
        }
    }

    private fun returnToWelcome() {
        val intent = Intent(this,ActivityWelcome::class.java)
        startActivity(intent)
    }

    private fun sendID(chargerID: String){
        val intent = Intent(this,ActivityCharging::class.java)
        intent.putExtra("chargerID",chargerID)
        startActivity(intent)
    }
}