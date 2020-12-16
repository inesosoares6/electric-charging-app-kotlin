package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.internal.Constants
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity


class FragmentCharge : Fragment(R.layout.fragment_charge) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_charge,container,false)
        val buttonSendId: Button = rootView.findViewById(R.id.button_sendid)
        val buttonQrCode: Button = rootView.findViewById(R.id.button_qrcode)
//        val buttonNFC: Button = rootView.findViewById(R.id.button_nfc)

        buttonSendId.setOnClickListener {

            val chargerIdText: EditText = rootView.findViewById(R.id.idcarregador)
            val list = listOf("202001","202002","202003","202004","202005")
            if(list.contains(chargerIdText.text.toString())){
                sendID(chargerIdText.text.toString())
            } else{
                Toast.makeText(context, getString(R.string.insertValidId),Toast.LENGTH_LONG).show()
            }
        }

        buttonQrCode.setOnClickListener{
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()
        }

        /*buttonNFC.setOnClickListener{
            val intent = Intent(context, ReceiverActivity::class.java)
            startActivity(intent)
        }*/

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val intent = Intent(context,ActivityCharging::class.java)
        if (result != null) {
            if (result.contents == null){
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                sendID(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendID(chargerID: String){
        val intent = Intent(context,ActivityCharging::class.java)
        intent.putExtra("chargerID",chargerID)
        startActivity(intent)
    }

}