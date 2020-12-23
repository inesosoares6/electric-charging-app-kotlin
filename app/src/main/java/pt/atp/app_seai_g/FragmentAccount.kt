package pt.atp.app_seai_g

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Fragment of account page
//    - sign out
//    - go to settings

class FragmentAccount : Fragment(R.layout.fragment_account) {

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_account,container,false)
        val buttonLogout: FloatingActionButton = rootView.findViewById(R.id.logoutButton)
        val settingsButton: FloatingActionButton = rootView.findViewById(R.id.settingsButton)
        val historicButton: FloatingActionButton = rootView.findViewById(R.id.historicButton)
        val numChargesText: TextView = rootView.findViewById(R.id.numChargesText)
        val userNameText: TextView = rootView.findViewById(R.id.userNameText)
        val listOfVehicles: TextView = rootView.findViewById(R.id.listOfVehicles)
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        mAuth=FirebaseAuth.getInstance()

        buttonLogout.setOnClickListener{
            mAuth.signOut()
            Toast.makeText(context,getString(R.string.successSignOut),Toast.LENGTH_LONG).show()
            val intent = Intent(context,ActivityLogin::class.java)
            startActivity(intent)
        }

        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
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
                        numChargesText.text = (activity?.getString(R.string.number_of_charges)) + "   " + numCharges
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_number_of_charges),Toast.LENGTH_LONG).show()
                    }
        }

        var userName : String
        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        userName = result["name"].toString()
                        userNameText.text = userName
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_name),Toast.LENGTH_LONG).show()
                    }
        }

        historicButton.setOnClickListener{
            //TODO implement a listView of charges history
        }

        listOfVehicles.setOnClickListener {
            //TODO show list of vehicles
            val intent = Intent(context,ActivityVehicleList::class.java)
            startActivity(intent)
        }

        return rootView
    }
}