package pt.atp.app_seai_g.models

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.atp.app_seai_g.R

class ChargerAdapter(private val context: Activity, private val date: Array<String>, private val time: Array<String>, private val price: Array<String>, private val chargerID: Array<String>)
    : ArrayAdapter<String>(context, R.layout.layout_history_list, date) {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.layout_history_list, null, true)

        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val dateText = rowView.findViewById(R.id.date) as TextView
        val timeText = rowView.findViewById(R.id.time) as TextView
        val priceText = rowView.findViewById(R.id.price) as TextView
        val chargerIDText = rowView.findViewById(R.id.charger) as TextView

        dateText.text = date[position]
        imageView.setImageResource(R.mipmap.ic_launcher)
        timeText.text = time[position]
        priceText.text = price[position]
        chargerIDText.text = chargerID[position]

        return rowView
    }
}