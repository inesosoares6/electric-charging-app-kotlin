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


class FragmentCharge : Fragment(R.layout.fragment_charge) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_charge,container,false)
        val buttonSendId: Button = rootView.findViewById(R.id.button_sendid)
//        val buttonQrCode: Button = rootView.findViewById(R.id.button_qrcode)
//        val buttonNFC: Button = rootView.findViewById(R.id.button_nfc)

        buttonSendId.setOnClickListener {
            val intent = Intent(context,ActivityCharging::class.java)
            val chargerIdText: EditText = rootView.findViewById(R.id.idcarregador)
            val list = listOf("202001","202002","202003","202004","202005")
            if(list.contains(chargerIdText.text.toString())){
                intent.putExtra("chargerID",chargerIdText.text.toString())
                startActivity(intent)
            } else{
                println("Insert valid charger ID")
                Toast.makeText(context, "Inset valid charger ID",Toast.LENGTH_LONG).show()
            }

        }

/*        buttonQrCode.setOnClickListener{
            val integrator = IntentIntegrator(activity).apply{
                captureActivity= CaptureActivity::class.java
                setOrientationLocked(false)
                setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                setPrompt("Scanning Code")
            }
            integrator.initiateScan()
        }

        buttonNFC.setOnClickListener{
            val intent = Intent(context, ReceiverActivity::class.java)
            startActivity(intent)
        }*/

        return rootView
    }

}