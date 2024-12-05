package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.MeasureUnit
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding

class ResourceCreateFragment : Fragment() {
    private var _binding: FragmentResourceCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinnerCategories: Spinner
    private lateinit var spinnerMeasureUnit: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerMeasureUnitAdapter: ArrayAdapter<String>
    private lateinit var resourceImage: ImageView
    private lateinit var btnSelectImage: Button

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

        spinnerCategories = view.findViewById(R.id.spinnerCategories)
        spinnerMeasureUnit = view.findViewById(R.id.spinnerMeasureUnit)
        resourceImage = binding.resourceImage
        btnSelectImage = binding.btnSelectImage

        // Inicializando o Spinner Categories
        val items = listOf("Item 1", "Item 2", "Item 3")


        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Lógica quando um item é selecionado
                Toast.makeText(requireContext(), "Selecionado: ${items[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        // Dados para o Spinner Unidades de de Medida

        val mUnits = MeasureUnit.entries.map { it.displayName }

        spinnerMeasureUnitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mUnits)
        spinnerMeasureUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeasureUnit.adapter = spinnerMeasureUnitAdapter

        spinnerMeasureUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedUnit = MeasureUnit.entries[position]
                Toast.makeText(requireContext(), "Selecionado: $selectedUnit", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        //Image Upload
        btnSelectImage.setOnClickListener {
            // Abrir o seletor de imagens
            selectImageLauncher.launch("image/*")
        }
    }
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Definir a imagem selecionada no ImageView
            resourceImage.setImageURI(it)
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