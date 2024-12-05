package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.ResourceCategory
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding

class ResourceCreateFragment : Fragment() {
    private var _binding: FragmentResourceCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializando o Spinner
        spinner = view.findViewById(R.id.spinnerCategories)

        // Dados para o Spinner
        val items = listOf("Item 1", "Item 2", "Item 3")

        // Configurando o Adapter
        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Configurar o Listener para mudanças
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Lógica quando um item é selecionado
                Toast.makeText(requireContext(), "Selecionado: ${items[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ResourceCreateFragment"
    }
}