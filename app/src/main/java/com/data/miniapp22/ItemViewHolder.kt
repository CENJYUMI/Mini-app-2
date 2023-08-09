package com.data.miniapp22

import androidx.recyclerview.widget.RecyclerView
import com.data.miniapp22.databinding.ItemLayoutBinding

class ItemViewHolder (val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (item:Item){
        binding.itemName.text = item.name
        binding.itemDesc.text = item.description
        binding.itemQty.text = item.quantity.toString()
    }
}