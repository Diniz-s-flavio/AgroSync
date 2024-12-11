package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.MeasureUnit
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.model.ResourceCategory
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ImageRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ResourceCreateFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
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
    private lateinit var selectedCategory: ResourceCategory
    private lateinit var selectedMeasureUnit: MeasureUnit
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var farmRepository: FarmRepository
    private lateinit var imageRepository: ImageRepository
    private var imgUrl: String = ""
    private lateinit var refResource: Resource
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceCreateBinding.inflate(inflater, container, false)
        resourceRepository = ResourceRepository()
        farmRepository = FarmRepository()
        imageRepository = ImageRepository(requireContext(), FirebaseFirestore.getInstance())

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

        navController = view.findNavController()

        if (arguments?.getBoolean("isEditing") == true) {
            refResource = mainViewModel.refResource
            binding.tvResourceCreateTitle.text = "Editar Insumo"
            binding.etName.setText(refResource.name)
            binding.etDescription.setText(refResource.description)
            binding.spinnerCategories.setSelection(refResource.category.ordinal)
            binding.spinnerMeasureUnit.setSelection(refResource.measureUnit.ordinal)
            binding.etDescription.setText(refResource.description)
            if (refResource.imgUrl.isNotBlank()){
                Glide.with(binding.resourceImage.context)
                    .load(refResource.imgUrl)
                    .placeholder(R.drawable.resource_img_placeholder)
                    .error(R.drawable.resource_img_placeholder)
                    .into(binding.resourceImage)
            }
        }
        setupCategoriesSpinner()
        setupMeasureUnitSpinner()

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imgUrl = uri.toString()
                    binding.resourceImage.setImageURI(uri)
                }
            }
        }

        //Image Upload
        btnSelectImage.setOnClickListener {
            // Abrir o seletor de imagens
            openImagePicker()
        }

        btnSave.setOnClickListener{
            lifecycleScope.launch {
                val userUid = firebaseAuth.currentUser?.uid
                if (userUid != null) {
                    var farm = farmRepository.findByOwnerId(userUid)
                    if (farm != null) {
                        var resource = Resource(
                            "",
                            binding.etName.text.toString(),
                            binding.etDescription.text.toString(),
                            selectedCategory,
                            farm,
                            selectedMeasureUnit
                        )
                        if (imgUrl.isNotBlank()){
                            resource.imgUrl = imgUrl
                        }
                        if(arguments?.getBoolean("isEditing") == true){
                            resource.id = mainViewModel.refResource.id
                            resource.totalAmount = mainViewModel.refResource.totalAmount
                            resource.totalValue = mainViewModel.refResource.totalValue
                            Log.d(TAG, "IMG do recurso: ${resource.imgUrl}")
                            resourceRepository.save(resource,
                                onSuccess = { response ->
                                    if (response != "") {
                                        Toast.makeText(
                                            requireContext(),
                                            "Insumo Salvo Com Sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        arguments?.putBoolean("isEditing", false)
                                        if (resource.imgUrl.isNotBlank()){
                                            Log.d(TAG, "URL da imagem: ${resource.imgUrl}")
                                            lifecycleScope.launch {
                                                imageRepository.uploadImage(resource.imgUrl, resource.id, "resource")
                                            }
                                        }
                                        navController.navigate(R.id.action_resourceCreateFragment_to_inventoryFragment)
                                    } else
                                        Toast.makeText(
                                            requireContext(),

                                            "Erro ao criar recurso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                })
                        }else{
                            resourceRepository.save(resource,
                                onSuccess = { response ->
                                    if (response != "") {
                                        Toast.makeText(
                                            requireContext(),
                                            "Insumo Salvo Com Sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (resource.imgUrl.isNotBlank()){
                                            Log.d(TAG, "URL da imagem: ${resource.imgUrl}")
                                            lifecycleScope.launch {
                                                imageRepository.uploadImage(resource.imgUrl, resource.id, "resource")
                                            }
                                        }
                                        navController.navigate(R.id.action_resourceCreateFragment_to_inventoryFragment)
                                    } else
                                        Toast.makeText(
                                            requireContext(),

                                            "Erro ao criar recurso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                })
                        }
                        }
                }
            }
        }

        btnCancel.setOnClickListener{
            navController.navigate(R.id.action_resourceCreateFragment_to_inventoryFragment)
        }
    }

    private fun setupMeasureUnitSpinner() {
        // Dados para o Spinner Unidades de de Medida
        val mUnits = MeasureUnit.entries.map { it.displayName }

        spinnerMeasureUnitAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mUnits)
        spinnerMeasureUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeasureUnit.adapter = spinnerMeasureUnitAdapter

        if (arguments?.getBoolean("isEditing") == true) {
            val defaultCategory = refResource.measureUnit
            val defaultPosition = mUnits.indexOf(defaultCategory.displayName)
            spinnerMeasureUnit.setSelection(defaultPosition)
        }

        spinnerMeasureUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMeasureUnit = MeasureUnit.entries[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupCategoriesSpinner() {
        val categories = ResourceCategory.entries.map { it.displayName }

        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        if (arguments?.getBoolean("isEditing") == true) {
            val defaultCategory = refResource.category
            val defaultPosition = categories.indexOf(defaultCategory.displayName)
            spinnerCategories.setSelection(defaultPosition)
        }

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = ResourceCategory.entries.first { it.displayName == categories[position] }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ResourceCreateFragment"
        private const val IMAGE_PICK_REQUEST_CODE = 101
    }
}