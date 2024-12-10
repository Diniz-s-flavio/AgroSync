package com.agrosync.agrosyncapp.ui.fragment.financial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.databinding.FragmentFinanceCreateBinding
import com.agrosync.agrosyncapp.databinding.FragmentFinanceDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale

class FinanceDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFinanceDetailBinding.inflate(inflater, container, false)

        val finance = arguments?.getSerializable("finance") as? Finance

        finance?.let {
            binding.tvFinanceDetailTitle.text = it.title
            binding.tvValorLabel.text = "R$${String.format("%.2f", it.value)}"
            binding.tvDateLabel.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.date)
            binding.tvOperationLabel.text = it.operation.displayName
            binding.tvDescricaoLabel.text = it.description
        }

        return binding.root
    }
}
