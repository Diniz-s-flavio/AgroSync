package com.agrosync.agrosyncapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import java.text.SimpleDateFormat
import java.util.Locale

class ResourceMovimentAdapter (
    private val resourceMoviments: List<ResourceMovement>,
    private val onItemClick: (ResourceMovement) -> Unit // Adiciona o listener de clique
) : RecyclerView.Adapter<ResourceMovimentAdapter.ResourceMovimentViewHolder>() {

    class ResourceMovimentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataResourceMovement: TextView = itemView.findViewById(R.id.dataResourceMovement)
        val valorResourceMovement: TextView = itemView.findViewById(R.id.valorResourceMovement)
        val tipoResourceMovement: TextView = itemView.findViewById(R.id.tipoResourceMovement)

        fun bind(resourceMovement: ResourceMovement, onItemClick: (ResourceMovement) -> Unit) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dataResourceMovement.text = dateFormat.format(resourceMovement.date)

            valorResourceMovement.text = "Valor: R$${String.format("%.2f", resourceMovement.movementAmount)}"

            tipoResourceMovement.text = resourceMovement.operation.displayName

            itemView.setOnClickListener { onItemClick(resourceMovement) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceMovimentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resource_moviment_item, parent, false)
        return ResourceMovimentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceMovimentViewHolder, position: Int) {
        holder.bind(resourceMoviments[position], onItemClick)
    }

    override fun getItemCount() = resourceMoviments.size
}