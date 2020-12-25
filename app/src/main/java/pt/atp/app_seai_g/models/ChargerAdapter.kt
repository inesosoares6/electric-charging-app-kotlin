package pt.atp.app_seai_g.models

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.atp.app_seai_g.R

class ChargerAdapter(private val context: Activity, private val date: Array<String>, private val type: Array<String>, private val time: Array<String>, private val price: Array<String>, private val chargerID: Array<String>)
    : ArrayAdapter<String>(context, R.layout.layout_history_list, date) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.layout_history_list, null, true)

        val imageView = rowView.findViewById(R.id.iconHistory) as ImageView
        val dateText = rowView.findViewById(R.id.dateHistory) as TextView
        val timeText = rowView.findViewById(R.id.timeHistory) as TextView
        val priceText = rowView.findViewById(R.id.priceHistory) as TextView
        val chargerIDText = rowView.findViewById(R.id.chargerHistory) as TextView
        val typeText = rowView.findViewById(R.id.typeHistory) as TextView

        dateText.text = context.getString(R.string.dateHistory) + " " + date[position]
        typeText.text = context.getString(R.string.typeHistory) + " " + type[position]
        imageView.setImageResource(R.mipmap.ic_launcher)
        timeText.text = context.getString(R.string.timeHistory) + " " + time[position] + " " + context.getString(R.string.minutesHistory)
        priceText.text = context.getString(R.string.priceHistory) + " " + price[position] + " €"
        chargerIDText.text = context.getString(R.string.chargerHistory) + " " + chargerID[position]

        return rowView
    }
}