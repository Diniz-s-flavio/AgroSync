package com.agrosync.agrosyncapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.model.Operation
import java.text.SimpleDateFormat
import java.util.Locale

class FinanceAdapter(
    var finances: List<Finance>,
    private val onItemClick: (Finance) -> Unit
) : RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder>() {

    class FinanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataFinancia: TextView = itemView.findViewById(R.id.dateFinance)
        val valorFinancia: TextView = itemView.findViewById(R.id.valueFinance)
        val tipoFinancia: TextView = itemView.findViewById(R.id.financeType)
        val iconeFinancia: ImageView = itemView.findViewById(R.id.financeIcon)

        fun bind(finance: Finance, onItemClick: (Finance) -> Unit) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dataFinancia.text = dateFormat.format(finance.date)

            valorFinancia.text = "Valor: R$${String.format("%.2f", finance.value)}"

            tipoFinancia.text = finance.title

            when (finance.operation) {
                Operation.ENTRY -> {
                    iconeFinancia.setColorFilter(
                        ContextCompat.getColor(itemView.context, R.color.green),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    iconeFinancia.setImageResource(R.drawable.ic_entry)
                }
                Operation.WITHDRAWAL -> {
                    iconeFinancia.setColorFilter(
                        ContextCompat.getColor(itemView.context, R.color.red),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    iconeFinancia.setImageResource(R.drawable.ic_withdrawal)
                }
                else -> {
                    iconeFinancia.clearColorFilter()
                    iconeFinancia.setImageResource(R.drawable.as_app_icon)
                }
            }

            itemView.setOnClickListener { onItemClick(finance) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.financial_item, parent, false)
        return FinanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        holder.bind(finances[position], onItemClick)
    }

    override fun getItemCount() = finances.size
}
