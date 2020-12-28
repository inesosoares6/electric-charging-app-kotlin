package pt.atp.app_seai_g

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.atp.app_seai_g.models.ChargerAdapter

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
        val arrayType: ArrayList<String> = ArrayList()

        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).collection("charges").get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        arrayDate.add(document["dayHour"].toString())
                        arrayCharger.add(document["idCharger"].toString())
                        arrayPrice.add(document["price"].toString())
                        arrayTime.add(document["time"].toString())
                        arrayType.add(document["type"].toString())
                    }
                    if(arrayDate.size == 1){
                        sendData(arrayDate,arrayType, arrayTime, arrayPrice, arrayCharger)
                    } else{
                        sendData(arrayDate.reversed() as ArrayList<String>,arrayType.reversed() as ArrayList<String>, arrayTime.reversed() as ArrayList<String>, arrayPrice.reversed() as ArrayList<String>, arrayCharger.reversed() as ArrayList<String>)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,getString(R.string.error_getting_documents), Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun sendData(arrayDate: ArrayList<String>, arrayType: ArrayList<String>, arrayTime: ArrayList<String>, arrayPrice: ArrayList<String>, arrayCharger: ArrayList<String>) {
        val myListAdapter = ChargerAdapter(this, arrayDate.toTypedArray(), arrayType.toTypedArray(), arrayTime.toTypedArray(), arrayPrice.toTypedArray(), arrayCharger.toTypedArray())
        val listView: ListView = findViewById(R.id.listViewHistory)
        listView.adapter = myListAdapter
    }
}