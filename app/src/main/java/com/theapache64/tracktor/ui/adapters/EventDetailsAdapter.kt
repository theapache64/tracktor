package com.theapache64.tracktor.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.tracktor.databinding.ItemEventDetailBinding
import com.theapache64.tracktor.models.UserEvent

class EventDetailsAdapter(
    private val eventDetails: List<UserEvent.Detail>,
    private val callback: (position: Int) -> Unit
) : RecyclerView.Adapter<EventDetailsAdapter.ViewHolder>() {

    private var inflater :LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(inflater==null){
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemEventDetailBinding.inflate(inflater!!, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = eventDetails.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventDetail = eventDetails[position]
        holder.binding.eventDetail = eventDetail
    }

    inner class ViewHolder(val binding: ItemEventDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback(layoutPosition)
            }
        }
    }
}