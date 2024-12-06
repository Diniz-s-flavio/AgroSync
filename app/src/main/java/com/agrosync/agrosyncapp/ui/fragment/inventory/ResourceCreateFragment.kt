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
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Farm
import com.agrosync.agrosyncapp.data.model.MeasureUnit
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.data.repository.UserRepository
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding
import com.agrosync.agrosyncapp.ui.fragment.HomeFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class ResourceCreateFragment : Fragment() {
    private var _binding: FragmentResourceCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinnerCategories: Spinner
    private lateinit var spinnerMeasureUnit: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerMeasureUnitAdapter: ArrayAdapter<String>
    private lateinit var resourceImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var selectedCategory: String
    private lateinit var selectedMeasureUnit: MeasureUnit
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var farmRepository: FarmRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceCreateBinding.inflate(inflater, container, false)
        resourceRepository = ResourceRepository()
        farmRepository = FarmRepository()

        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerCategories = view.findViewById(R.id.spinnerCategories)
        spinnerMeasureUnit = view.findViewById(R.id.spinnerMeasureUnit)
        resourceImage = binding.resourceImage
        btnSelectImage = binding.btnSelectImage
        btnSave = binding.btnSave
        btnCancel = binding.btnCancel

//        navController = view.findNavController()

        setupCategoriesSpinner()
        setupMeasureUnitSpinner()

        //Image Upload
        btnSelectImage.setOnClickListener {
            // Abrir o seletor de imagens
            selectImageLauncher.launch("image/*")
        }
        btnSave.setOnClickListener{
            val userUid = firebaseAuth.currentUser?.uid
            if (userUid != null)
                farmRepository.findByOwnerId(userUid, onSuccess = {farm ->
                    if (farm != null) {
                        var resource: Resource = Resource("",
                            binding.etName.text.toString(),
                            binding.etDescription.text.toString(),
                            selectedCategory.toString(),
                            farm,
                            selectedMeasureUnit
                        )
                    resourceRepository.create(resource,
                        onSuccess = {
                            response ->
                            if (response != ""){
                                Toast.makeText(requireContext(), "Insumo Salvo Com Sucesso", Toast.LENGTH_SHORT).show()
                            }else
                                Toast.makeText(requireContext(), "Erro ao criar recurso", Toast.LENGTH_SHORT).show()
                        })
                    }
            })



        }
        btnCancel.setOnClickListener{
//            navController.navigate(R.id.action_resourceCreateFragment_to_homeFragment)
        }
    }

    private fun setupMeasureUnitSpinner() {
        // Dados para o Spinner Unidades de de Medida
        val mUnits = MeasureUnit.entries.map { it.displayName }

        spinnerMeasureUnitAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mUnits)
        spinnerMeasureUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeasureUnit.adapter = spinnerMeasureUnitAdapter

        spinnerMeasureUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMeasureUnit = MeasureUnit.entries[position]
                Toast.makeText(requireContext(), "Selecionado: $selectedMeasureUnit", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupCategoriesSpinner() {
        // Inicializando o Spinner Categories
        val items = listOf("Item 1", "Item 2", "Item 3")


        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Lógica quando um item é selecionado
                selectedCategory = items[position]
                Toast.makeText(
                    requireContext(),
                    "Selecionado: ${items[position]}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
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