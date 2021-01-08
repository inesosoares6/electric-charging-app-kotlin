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
        val vehiclesButton: FloatingActionButton = rootView.findViewById(R.id.vehiclesButton)
        val numChargesText: TextView = rootView.findViewById(R.id.numChargesText)
        val userNameText: TextView = rootView.findViewById(R.id.userNameText)
        val lastChargeDate: TextView = rootView.findViewById(R.id.lastChargeDate)
        val lastChargeType: TextView = rootView.findViewById(R.id.lastChargeType)
        val lastChargeTime: TextView = rootView.findViewById(R.id.lastChargeTime)
        val lastChargePrice: TextView = rootView.findViewById(R.id.lastChargePrice)
        val lastChargeID: TextView = rootView.findViewById(R.id.lastChargeID)
        val lastCharge: TextView = rootView.findViewById(R.id.lastCharge)
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

        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        userNameText.text = result["name"].toString()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_name),Toast.LENGTH_LONG).show()
                    }
        }

        historicButton.setOnClickListener{
            val intent = Intent(context,ActivityHistory::class.java)
            startActivity(intent)
        }

        vehiclesButton.setOnClickListener {
            val intent = Intent(context,ActivityVehicleList::class.java)
            startActivity(intent)
        }

        mAuth.currentUser?.email?.let {
            db.collection("users").document(it).collection("lastCharge").document("last").get()
                    .addOnSuccessListener { result ->
                        if(result["dayHour"]!=null){
                            lastCharge.text = (context?.getString(R.string.lastCharge))
                            lastChargeDate.text = (context?.getString(R.string.dateHistory)) + " " + result["dayHour"].toString()
                            lastChargeType.text = (context?.getString(R.string.typeHistory)) + " " + result["type"].toString()
                            lastChargeTime.text = (context?.getString(R.string.timeHistory)) + " " + result["time"].toString() + " " + (context?.getString(R.string.minutesHistory))
                            lastChargePrice.text = (context?.getString(R.string.priceHistory)) + " " + result["price"].toString() + " €"
                            lastChargeID.text = (context?.getString(R.string.chargerHistory)) + " " + result["idCharger"].toString()
                        } else{
                            lastCharge.text = (context?.getString(R.string.firstCharge))
                            lastChargeDate.text = (context?.getString(R.string.firstChargeText))
                            lastChargeType.text = (context?.getString(R.string.firstChargeText2))
                            lastChargeTime.text = (context?.getString(R.string.firstChargeText3))
                            lastChargePrice.text = (context?.getString(R.string.firstChargeText4))
                            lastChargeID.text = (context?.getString(R.string.firstChargeText5))
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.errorLastCharge),Toast.LENGTH_LONG).show()
                    }
        }

        return rootView
    }
}