package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.atp.app_seai_g.models.VehicleAdapter

class ActivityVehicleList : AppCompatActivity() {

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null
    private val arrayType: ArrayList<String> = ArrayList()
    private val arrayDetails: ArrayList<String> = ArrayList()
    private val arrayDocs: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        mAuth=FirebaseAuth.getInstance()

        mAuth!!.currentUser?.email?.let {
            db.collection("users").document(it).collection("vehicles").get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        arrayDocs.add(document.id)
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
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(getString(R.string.performAction))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.favorite)) { _, _ ->
                        setAsFavorite(itemIdAtPos.toInt())
                    }
                    .setNegativeButton(getString(R.string.deleteFromList)) { _, _ ->
                        deleteFromList(itemIdAtPos.toInt())
                    }
                    .setNeutralButton(getString(R.string.cancelElimination)) { dialog, _ ->
                        dialog.cancel()
                    }
            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.vehicle) + " " + (itemIdAtPos+1))
            alert.show()
        }
    }

    private fun deleteFromList(idList: Int) {
        mAuth?.currentUser?.email?.let {
            db.collection("users").document(it).collection("vehicles").document(arrayDocs[idList]).delete()
        }
        finish()
    }

    private fun setAsFavorite(idList: Int) {
        val favVehicle = hashMapOf(
                "type" to arrayType[idList],
                "details" to arrayDetails[idList]
        )
        mAuth?.currentUser?.email?.let {
            db.collection("users").document(it).collection("favVehicle").document(arrayDocs[idList]).set(favVehicle)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, getString(R.string.vehicle_addition_successful), Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.error_adding_vehicle), Toast.LENGTH_LONG).show()
                    }
        }
    }
}