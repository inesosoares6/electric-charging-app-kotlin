package pt.atp.app_seai_g

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, IDcarregador::class.java)
            startActivity(intent)
        }

        button_register.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val mUsername: String = email.text.toString().trim()
        val mPassword: String = password.text.toString().trim()
        val t = mUsername.isNotEmpty() && mPassword.isNotEmpty()
        if (t) {
            button.setBackgroundResource(R.color.colorPrimaryDark)
        }
        else {
            button.setBackgroundResource(R.color.colorwhitegreenshade)
        }
    }
}