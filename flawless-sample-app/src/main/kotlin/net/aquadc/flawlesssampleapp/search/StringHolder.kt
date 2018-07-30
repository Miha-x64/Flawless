package net.aquadc.flawlesssampleapp.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class StringHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
) {

    fun bind(s: String) {
        (itemView as TextView).text = s
    }

}
