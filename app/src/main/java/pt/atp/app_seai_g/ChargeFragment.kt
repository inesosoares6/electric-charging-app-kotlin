package pt.atp.app_seai_g

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class ChargeFragment : Fragment(R.layout.fragment_charge) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_charge,container,false)
        val buttonSendId: Button = rootView.findViewById(R.id.button_sendid2)

        buttonSendId.setOnClickListener {
            val intent = Intent(context,ChargingMode::class.java)
            val chargerIdText: EditText = rootView.findViewById(R.id.idcarregador2)
            intent.putExtra("chargerID",chargerIdText.text.toString())
            startActivity(intent)
        }

        return rootView
    }

}