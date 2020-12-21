package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddVehicleActivity : AppCompatActivity() {

    private var vehicleTV: EditText? = null
    private var brandTV: EditText? = null
    private var modelTV: EditText? = null
    private var buttonAddVehicle: Button? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        initializeUI()
        database = Firebase.database.reference

        buttonAddVehicle!!.setOnClickListener{
            //TODO verify info of vehicle and write to database
        }

    }

    private fun initializeUI() {
        vehicleTV = findViewById(R.id.typeOfVehicle)
        brandTV = findViewById(R.id.brandOfVehicle)
        modelTV = findViewById(R.id.modelOfVehicle)
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle)
    }
}