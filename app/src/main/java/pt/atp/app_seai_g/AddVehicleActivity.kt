package pt.atp.app_seai_g

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddVehicleActivity : AppCompatActivity() {

    private var vehicleTV: EditText? = null
    private var brandTV: EditText? = null
    private var modelTV: EditText? = null
    private var buttonAddVehicle: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        initializeUI()

        buttonAddVehicle!!.setOnClickListener{
            //TODO verify info of vehicle

        }

    }

    private fun initializeUI() {
        vehicleTV = findViewById(R.id.typeOfVehicle)
        brandTV = findViewById(R.id.brandOfVehicle)
        modelTV = findViewById(R.id.modelOfVehicle)
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle)
    }
}