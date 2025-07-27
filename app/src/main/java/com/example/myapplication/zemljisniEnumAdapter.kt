package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class zemljisniEnumAdapter(context: Context, private val resource: Int, private val zemljisniTip: Array<Zemljiste>) :
    ArrayAdapter<Zemljiste>(context, resource, zemljisniTip) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(resource, parent, false)
        }

        val zemljisniTip = zemljisniTip[position]
        val textView = itemView!!.findViewById<TextView>(android.R.id.text1)
        textView.text = zemljisniTip.toString()

        return itemView
    }
}