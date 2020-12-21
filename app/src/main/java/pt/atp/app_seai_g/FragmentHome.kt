package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

// Fragment home that contains:
//    - button to open maps with location of parking stations

class FragmentHome : Fragment(R.layout.fragment_home) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_home,container,false)
        val mapButton: Button = rootView.findViewById(R.id.mapButton)
        mapButton.setOnClickListener{
            val intent = Intent(context, ActivityMaps::class.java)
            startActivity(intent)
        }
        return rootView
    }



}