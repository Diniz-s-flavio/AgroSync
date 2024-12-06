package com.agrosync.agrosyncapp.ui.fragment.financial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentFinanceCreateBinding

class FinanceCreateFragment : Fragment() {

    private var binding: FragmentFinanceCreateBinding? = null
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinanceCreateBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        binding?.btnSave?.setOnClickListener{
            navController.navigate(R.id.action_finance_create_fragment_to_financialFragment)
        }

        binding?.btnCancel?.setOnClickListener{
            navController.navigate(R.id.action_finance_create_fragment_to_financialFragment)
        }

    }

}