package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class FragmentAccount : Fragment(R.layout.fragment_account) {

    private var fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_account,container,false)
        val buttonLogout: Button = rootView.findViewById(R.id.logoutButton)

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

        return rootView
    }
}