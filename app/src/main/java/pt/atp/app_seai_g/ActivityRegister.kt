package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class ActivityRegister : AppCompatActivity() {

    private var nameTV: EditText? = null
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var confirmPasswordTV: EditText? = null
    private var regBtn: Button? = null
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        regBtn!!.setOnClickListener{
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        val name: String = nameTV!!.text.toString()
        val email: String = emailTV!!.text.toString()
        val password: String = passwordTV!!.text.toString()
        val confirmPassword: String = confirmPasswordTV!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, getString(R.string.enterName), Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, getString(R.string.enterEmail), Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, getString(R.string.enterPassword), Toast.LENGTH_LONG).show()
            return
        }
        if(password != confirmPassword){
            Toast.makeText(applicationContext, getString(R.string.matchPassword), Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.successRegister), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.failRegister), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        nameTV = findViewById(R.id.name)
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        confirmPasswordTV = findViewById(R.id.confirm_password)
        regBtn = findViewById(R.id.button_register)
    }
}