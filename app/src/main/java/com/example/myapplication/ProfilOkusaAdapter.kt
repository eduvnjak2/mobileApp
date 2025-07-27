package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProfilOkusaAdapter(context: Context, private val resource: Int, private val profilOkusa: Array<ProfilOkusaBiljke>) :
    ArrayAdapter<ProfilOkusaBiljke>(context, resource, profilOkusa) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(resource, parent, false)
        }

        val profilOkusa = profilOkusa[position]
        val textView = itemView!!.findViewById<TextView>(android.R.id.text1)
        textView.text = profilOkusa.opis

        return itemView
    }
}






