package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Farm
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.databinding.FragmentInventoryBinding
import com.agrosync.agrosyncapp.ui.adapter.ResourceAdapter
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InventoryFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    private lateinit var recyclerView: RecyclerView
    private var resourceList: List<Resource>? = emptyList()
    private lateinit var adapter: ResourceAdapter
    private lateinit var _binding: FragmentInventoryBinding
    private val binding get() = _binding
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var farmRepository: FarmRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    private var originalResourceList: List<Resource>? = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        resourceRepository = ResourceRepository()
        farmRepository = FarmRepository()
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.resourceRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        navController = view.findNavController()

        binding.searchViewResources.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterResources(newText.orEmpty())
                return true
            }
        })

        lifecycleScope.launch {
            val farm = auth.currentUser?.let { farmRepository.findByOwnerId(it.uid) }
            mainViewModel.refFarm = farm!!
            resourceList = farm.id?.let { resourceRepository.findAllResourceByFarm(it) }
            originalResourceList = resourceList // Store the original list

            Log.d(TAG, "Resource List: $resourceList")

            updateRecyclerView(resourceList)

            binding.createResourceButton.setOnClickListener {
                navController.navigate(R.id.action_inventoryFragment_to_resourceCreateFragment)
            }
        }
    }

    private fun filterResources(query: String) {
        val filteredList = originalResourceList?.filter { resource ->
            resource.name?.contains(query, ignoreCase = true) == true
        }
        updateRecyclerView(filteredList)
    }

    private fun updateRecyclerView(resources: List<Resource>?) {
        if (resources != null && resources.isNotEmpty()) {
            adapter = ResourceAdapter(
                requireContext(),
                resources.toMutableList(),
                onClickItem()
            )
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "Nenhum insumo encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickItem(): ResourceAdapter.ResourceClickListener {
        return object : ResourceAdapter.ResourceClickListener{
            @SuppressLint("RestrictedApi")
            override fun onItemClick(holder: ResourceAdapter.ResourceViewHolder, position: Int) {
                val resource = resourceList?.getOrNull(position)?: return
                Log.d(TAG, "Clicou no item ${resource.name}")

                mainViewModel.refResource = resource
                navController.navigate(R.id.action_inventoryFragment_to_resourceDetailFragment)
            }
        }
    }
    companion object{
        private val TAG = "InventoryFragment"
    }

}