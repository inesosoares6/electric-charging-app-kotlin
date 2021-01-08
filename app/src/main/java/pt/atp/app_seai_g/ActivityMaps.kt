package pt.atp.app_seai_g

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import pt.atp.app_seai_g.Data.Request
import pt.atp.app_seai_g.MyApplication.Companion.urlStart

// Activity that contains the locations of the charging stations
//     - pin in charging station
//     - shows the slots available
// https://www.raywenderlich.com/230-introduction-to-google-maps-api-for-android-with-kotlin

class ActivityMaps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private var message: String? =null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val feup = LatLng(41.1780, -8.5980)

        //Check number of free slots
        doAsync {
            message = Request("$urlStart/checkslots/").run()
            uiThread {
                val obj = JSONObject(message.toString())
                val freeSlots = obj.getString("slots")
                map.addMarker(MarkerOptions().position(feup).title("FEUP: "+ freeSlots + " " + getString(R.string.parkingFeup)))
            }
        }

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        // Map types:
        //   - MAP_TYPE_TERRAIN displays a more detailed view of the area
        //   - MAP_TYPE_NORMAL displays a typical road map with labels
        //   - MAP_TYPE_SATELLITE displays a satellite view of an area with no labels
        //   - MAP_TYPE_HYBRID displays a combination of the satellite and normal mode
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
    }
}

