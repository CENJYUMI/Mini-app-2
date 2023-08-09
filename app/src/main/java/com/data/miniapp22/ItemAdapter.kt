package com.data.miniapp22

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.miniapp22.databinding.ItemLayoutBinding

class ItemAdapter (private val items: List<Item>) : RecyclerView.Adapter<ItemViewHolder>(){

    var onDeleteClick: ((Item) -> Unit)? = null
    var onUpdateClick: ((Item) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.apply {
            deleteBtn.setOnClickListener{
                onDeleteClick ?.invoke(items[position])
            }
            editBtn.setOnClickListener{
                onUpdateClick ?.invoke(items[position])

            }
        }

    }
}