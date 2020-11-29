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

        email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.message, 0, 0, 0)
        password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.password, 0, 0, 0)

        email.addTextChangedListener(loginTextWatcher)
        password.addTextChangedListener(loginTextWatcher)

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    var drawable = resources.getDrawable(R.drawable.message)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorPrimaryDark))
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    email.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.message),
                        null, resources.getDrawable(R.drawable.cancel), null)
                }
                else if (s.isEmpty()) {
                    email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.message,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.message)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault))
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    email.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.message),
                        null, null, null
                    )
                }
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    var drawable = resources.getDrawable(R.drawable.password) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorPrimaryDark)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.password),
                        null, resources.getDrawable(R.drawable.cancel), null)
                }
                else if (s.isEmpty()) {
                    password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.password,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.password) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.password),
                        null, null, null
                    )
                }
            }
        })

        email.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (email.compoundDrawables[2] != null) {
                    if (event.x >= email.right - email.left -
                        email.compoundDrawables[2].bounds.width()
                    ) {
                        if (email.text.toString() != "") {
                            email.setText("")
                        }
                    }
                }
            }
            false
        }

        password.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (password.compoundDrawables[2] != null) {
                    if (event.x >= password.right - password.left -
                        password.compoundDrawables[2].bounds.width()
                    ) {
                        if (password.text.toString() != "") {
                            password.setText("")
                        }
                    }
                }
            }
            false
        }

        remember_password.setOnClickListener {
            if (!(remember_password.isSelected)) {
                remember_password.isChecked = true
                remember_password.isSelected = true
            } else {
                remember_password.isChecked = false
                remember_password.isSelected = false
            }
        }

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

    private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
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