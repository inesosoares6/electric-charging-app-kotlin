package pt.atp.app_seai_g

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.activity_id_carregador.*


class IDcarregador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_carregador)

        button_qrcode.setOnClickListener{
            scanQRCode()
        }

        val button = findViewById<Button>(R.id.button_nfc)
        button.setOnClickListener{
            val intent = Intent(this, ReceiverActivity::class.java)
            startActivity(intent)
        }
    }

    private fun scanQRCode(){
        val integrator = IntentIntegrator(this).apply{
            captureActivity=CaptureActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scanning Code")
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data)
        if(result!=null){
            if(result.contents == null) Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show()
            else Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        } else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}