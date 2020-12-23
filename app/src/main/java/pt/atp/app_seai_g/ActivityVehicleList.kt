package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.atp.app_seai_g.models.VehicleAdapter

class ActivityVehicleList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        mAuth=FirebaseAuth.getInstance()
        val arrayType: ArrayList<String> = ArrayList()
        val arrayDetails: ArrayList<String> = ArrayList()

        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).collection("vehicles").get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        arrayType.add(document["type"].toString())
                        arrayDetails.add((document["brand"].toString())+" - "+(document["model"] as String))
                    }
                    sendData(arrayType,arrayDetails)
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,getString(R.string.error_getting_documents),Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun sendData(arrayType: ArrayList<String>, arrayDetails: ArrayList<String>) {
        val myListAdapter = VehicleAdapter(this, arrayType.toTypedArray(), arrayDetails.toTypedArray())
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener { adapterView, _, position, _ ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }
    }
}