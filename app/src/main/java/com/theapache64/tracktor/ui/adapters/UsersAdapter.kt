package com.theapache64.tracktor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.databinding.ItemUserBinding

class UsersAdapter(
    private val users: List<UserEntity>,
    private val callback: (position: Int, tvUserName : View) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemUserBinding.inflate(inflater!!, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.binding.user = user
    }

    inner class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback(layoutPosition, binding.tvItemUserName)
            }
        }
    }
}