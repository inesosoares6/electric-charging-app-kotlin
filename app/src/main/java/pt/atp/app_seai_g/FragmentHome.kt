package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView


class FragmentHome : Fragment(R.layout.fragment_home) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_home,container,false)
        val mapButton: Button = rootView.findViewById(R.id.mapButton)

        mapButton.setOnClickListener{
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
        }

        // Gets the map from the xml layout and creates it
        /*val mapView: MapView  = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Gets GoogleMap from the MapView and does initialization
        val map: GoogleMap = mapView.getMapAsync(this)*/

        return rootView
    }



}