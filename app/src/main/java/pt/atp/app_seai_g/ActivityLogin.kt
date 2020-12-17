package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ActivityLogin : AppCompatActivity() {

    private val viewModel: ViewModelLogin by viewModels()

    private var mAuth: FirebaseAuth? = null
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null
    private var forgotPassword: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if(mAuth!!.currentUser != null){
            startActivity(Intent(this, ActivityWelcome::class.java))
            finish()
        }
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        loginBtn!!.setOnClickListener { validateCredentialsAndRedirect() }
        registerBtn!!.setOnClickListener{
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
        forgotPassword!!.setOnClickListener{
            val intent = Intent(this, ActivityResetPassword::class.java)
            startActivity(intent)
        }
        viewModel.loginResultLiveData.observe(this){ loginResult ->
            if(!loginResult){
                Toast.makeText(applicationContext, getString(R.string.failLogin), Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(applicationContext, getString(R.string.successLogin), Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ActivityWelcome::class.java))
                finish()
            }
        }
    }

    private fun validateCredentialsAndRedirect(){
        val email: String = emailTV!!.text.toString()
        val password: String = passwordTV!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, getString(R.string.enterEmail), Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, getString(R.string.enterPassword), Toast.LENGTH_LONG).show()
            return
        }
        viewModel.areCredentialsValid(mAuth,email,password)
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        loginBtn = findViewById(R.id.button_login)
        registerBtn = findViewById(R.id.button_register)
        forgotPassword = findViewById(R.id.forgot_password)
    }
}