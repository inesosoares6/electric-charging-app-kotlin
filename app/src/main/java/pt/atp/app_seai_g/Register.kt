package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class Register : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        val buttonRegister = findViewById<Button>(R.id.button_register)
        buttonRegister.setOnClickListener{
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        val email = findViewById<EditText>(R.id.email).toString()
        val password = findViewById<EditText>(R.id.password).toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Registration successful!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Registration failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}