package pt.atp.app_seai_g

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// QR code reader activity

class ActivityCapture : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_qrcode)
    }
}