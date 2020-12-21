package pt.atp.app_seai_g

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

// Activity that contains the three fragments:
//    - home          (FragmentHome)
//    - charging id   (Fragment Charge)
//    - account       (Fragment Account)

class ActivityWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val homeFragment=FragmentHome()
        val chargeFragment=FragmentCharge()
        val accountFragment=FragmentAccount()

        setCurrentFragment(homeFragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.charge->setCurrentFragment(chargeFragment)
                R.id.person->setCurrentFragment(accountFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}