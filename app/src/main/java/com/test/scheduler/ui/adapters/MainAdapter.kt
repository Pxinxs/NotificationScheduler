package com.test.scheduler.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.scheduler.R
import com.test.scheduler.entity.room.Notification

class MainAdapter(private val notifications: List<Notification>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = notifications.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            CardHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_main, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as CardHolder).tvDate.text = notifications[position].date
        holder.tvTime.text = notifications[position].time
        holder.tvNote.text = notifications[position].note
    }

    inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
    }
}