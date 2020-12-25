package pt.atp.app_seai_g

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.atp.app_seai_g.models.ChargerAdapter
import pt.atp.app_seai_g.models.VehicleAdapter

class ActivityHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        mAuth= FirebaseAuth.getInstance()
        val arrayDate: ArrayList<String> = ArrayList()
        val arrayTime: ArrayList<String> = ArrayList()
        val arrayPrice: ArrayList<String> = ArrayList()
        val arrayCharger: ArrayList<String> = ArrayList()

        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).collection("charges").get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        arrayDate.add(document["dayHour"].toString())
                        arrayCharger.add(document["idCharger"].toString())
                        arrayPrice.add(document["price"].toString())
                        arrayTime.add(document["time"].toString())
                    }
                    Toast.makeText(applicationContext,arrayTime.toString(),Toast.LENGTH_LONG).show()
                    sendData(arrayDate,arrayTime, arrayPrice, arrayCharger)
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,getString(R.string.error_getting_documents), Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun sendData(arrayDate: ArrayList<String>, arrayTime: ArrayList<String>, arrayPrice: ArrayList<String>, arrayCharger: ArrayList<String>) {
        val myListAdapter = ChargerAdapter(this, arrayDate.toTypedArray(), arrayTime.toTypedArray(), arrayPrice.toTypedArray(), arrayCharger.toTypedArray())
        val listView: ListView = findViewById(R.id.listViewHistory)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener { adapterView, _, position, _ ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }
    }
}