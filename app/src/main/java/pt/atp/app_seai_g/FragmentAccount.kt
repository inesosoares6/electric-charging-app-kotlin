package pt.atp.app_seai_g

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

// Fragment of account page
//    - sign out
//    - go to settings

class FragmentAccount : Fragment(R.layout.fragment_account) {

    private var fbAuth = FirebaseAuth.getInstance()

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_account,container,false)
        val buttonLogout: Button = rootView.findViewById(R.id.logoutButton)
        val settingsButton: FloatingActionButton = rootView.findViewById(R.id.settingsButton)
        val darkThemeButton: Switch = rootView.findViewById(R.id.darkThemeButton)
        val historicButton: FloatingActionButton = rootView.findViewById(R.id.historicButton)
        val numChargesText: TextView = rootView.findViewById(R.id.numChargesText)
        val database = Firebase.database
        val myRef = database.getReference("message")
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        mAuth=FirebaseAuth.getInstance()

        buttonLogout.setOnClickListener{
            fbAuth.signOut()
            Toast.makeText(context,getString(R.string.successSignOut),Toast.LENGTH_LONG).show()
            val intent = Intent(context,ActivityLogin::class.java)
            startActivity(intent)
        }

        fbAuth.addAuthStateListener {
            if(fbAuth.currentUser == null){
                activity?.finish()
            }
        }

        settingsButton.setOnClickListener{
            val intent = Intent(context,SettingsActivity::class.java)
            startActivity(intent)
        }

        var numCharges : String
        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        numCharges = result["numCharges"].toString()
                        numChargesText.text = getString(R.string.number_of_charges) + "   " + numCharges
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,"Error in reading number of charges",Toast.LENGTH_LONG).show()
                    }
        }

        historicButton.setOnClickListener{
            db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Toast.makeText(context, "${document.id} => ${document.data}",Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error getting documents.", Toast.LENGTH_LONG).show()
                    }
        }

        darkThemeButton.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    darkThemeButton.isChecked=true
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkThemeButton.isChecked=false
                }
            }
        }

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Toast.makeText(context,"Value is: $value", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(context,"Failed to read value.", Toast.LENGTH_LONG).show()
            }
        })

        return rootView
    }
}