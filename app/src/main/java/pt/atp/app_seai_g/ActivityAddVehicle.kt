package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityAddVehicle : AppCompatActivity() {

    private var vehicleTV: EditText? = null
    private var brandTV: EditText? = null
    private var modelTV: EditText? = null
    private var buttonAddVehicle: Button? = null
    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        initializeUI()
        mAuth= FirebaseAuth.getInstance()

        buttonAddVehicle!!.setOnClickListener{
            writeVehicleToDatabase(vehicleTV?.text.toString(),brandTV?.text.toString(),modelTV?.text.toString())
        }

    }

    private fun writeVehicleToDatabase(type: String, brand: String, model: String){
        val vehicle = hashMapOf(
                "type" to type,
                "brand" to brand,
                "model" to model
        )
        val vehicleId: String = type + "_" + brand + "_" + model
        mAuth?.currentUser?.email?.let { it1 ->
            db.collection("users").document(it1).collection("vehicles").document(vehicleId).set(vehicle)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, getString(R.string.vehicle_addition_successful), Toast.LENGTH_LONG).show()
                        val intent = Intent(this, ActivityWelcome::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.error_adding_vehicle), Toast.LENGTH_LONG).show()
                    }
        }
    }

    private fun initializeUI() {
        vehicleTV = findViewById(R.id.typeOfVehicle)
        brandTV = findViewById(R.id.brandOfVehicle)
        modelTV = findViewById(R.id.modelOfVehicle)
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle)
    }
}