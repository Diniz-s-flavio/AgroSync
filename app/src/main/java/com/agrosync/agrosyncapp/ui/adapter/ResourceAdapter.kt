package com.agrosync.agrosyncapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Resource

class ResourceAdapter(private val context: Context,
                      private val resourceList: MutableList<Resource>, private val onItemClickListener: ResourceClickListener) :
    RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {
    inner class ResourceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvItemTitle)
        val amount = itemView.findViewById<TextView>(R.id.tvItemAmount)
        val unit = itemView.findViewById<TextView>(R.id.tvItemUnit)
        val image = itemView.findViewById<ImageView>(R.id.ivItemImage)
    }

    interface ResourceClickListener {
        fun onItemClick(holder: ResourceViewHolder, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val itemList = LayoutInflater.from(context).inflate(R.layout.resource_item, parent, false)
        val holder = ResourceViewHolder(itemList)
        return holder
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.title.text = resourceList[position].name
        holder.amount.text = "Qtd: " + resourceList[position].totalAmount.toString()
        holder.unit.text = resourceList[position].measureUnit.displayName

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder, position)
        }
    }
    override fun getItemCount(): Int = resourceList.size
}