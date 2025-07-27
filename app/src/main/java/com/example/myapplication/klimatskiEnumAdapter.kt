package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class klimatskiEnumAdapter(context: Context, private val resource: Int, private val klimatskiTip: Array<KlimatskiTip>) :
    ArrayAdapter<KlimatskiTip>(context, resource, klimatskiTip) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(resource, parent, false)
        }

        val medicinskaKorist = klimatskiTip[position]
        val textView = itemView!!.findViewById<TextView>(android.R.id.text1)
        textView.text = medicinskaKorist.opis

        return itemView
    }
}