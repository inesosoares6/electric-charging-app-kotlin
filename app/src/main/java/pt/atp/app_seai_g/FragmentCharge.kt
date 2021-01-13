package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import pt.atp.app_seai_g.Data.Request
import pt.atp.app_seai_g.MyApplication.Companion.urlStart

// Fragment to insert charger id
//     - verify id charger
//          - if charger is available
//          - the vehicle is connected
//     - begin charging (ActivityCharging)

class FragmentCharge : Fragment(R.layout.fragment_charge) {

    private var message: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val rootView: View = inflater.inflate(R.layout.fragment_charge,container,false)
        val buttonSendId: Button = rootView.findViewById(R.id.button_sendId)
        val buttonQrCode: Button = rootView.findViewById(R.id.button_QRCode)
        val buttonNFC: Button = rootView.findViewById(R.id.button_nfc)

        buttonSendId.setOnClickListener {
            val chargerIdText: EditText = rootView.findViewById(R.id.idCharger)
            confirmIdCharger(chargerIdText.text.toString())
        }

        buttonQrCode.setOnClickListener{
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()
        }

        buttonNFC.setOnClickListener{
            val intent = Intent(context, ReceiverActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null){
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            }
            else{
                //Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                confirmIdCharger(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun confirmIdCharger(chargerID: String){
        doAsync {
            message = Request("$urlStart/readytocharge/$chargerID").run()
            uiThread{
                val obj = JSONObject(message.toString())
                //Toast.makeText(context,message,Toast.LENGTH_LONG).show()
                if (obj.getString("flag")=="1"){
                    sendID(chargerID)
                } else{
                    Toast.makeText(context,getString(R.string.insert_valid_id1) +" " + chargerID + " " + getString(R.string.insert_valid_id2),Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendID(chargerID: String){
        val intent = Intent(context,ActivityCharging::class.java)
        intent.putExtra("chargerID",chargerID)
        startActivity(intent)
    }
}