package com.theapache64.tracktor.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.tracktor.databinding.ItemUserEventBinding
import com.theapache64.tracktor.models.UserEvent

class UserEventsAdapter(
    private val userEvents: List<UserEvent>,
    private val getAdapter: (position: Int) -> EventDetailsAdapter,
    private val callback: (position: Int) -> Unit
) : RecyclerView.Adapter<UserEventsAdapter.ViewHolder>() {

    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemUserEventBinding.inflate(inflater!!, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = userEvents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userEvent = userEvents[position]
        holder.binding.userEvent = userEvent
        val eventDetailsAdapter = getAdapter(position)
        holder.binding.rvEventDetails.adapter = eventDetailsAdapter
    }

    inner class ViewHolder(val binding: ItemUserEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback(layoutPosition)
            }
        }
    }
}