package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.databinding.FragmentInventoryBinding
import com.agrosync.agrosyncapp.ui.adapter.ResourceAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InventoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var resourceList: List<Resource>? = emptyList()
    private lateinit var adapter: ResourceAdapter
    private lateinit var _binding: FragmentInventoryBinding
    private val binding get() = _binding
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var farmRepository: FarmRepository
    private lateinit var auth: FirebaseAuth

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

        lifecycleScope.launch {
            val farm = auth.currentUser?.let { farmRepository.findByOwnerId(it.uid) }
            resourceList = farm?.id?.let { resourceRepository.findAllResourceByFarm(it) }
            Log.d(TAG, "Resource List: $resourceList")

            if (resourceList != null && (resourceList as MutableList<Resource>).isNotEmpty()) {
                adapter = ResourceAdapter(requireContext(),
                    (resourceList as MutableList<Resource>).toMutableList(), onClickItem())
                recyclerView.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "Nenhum recurso encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickItem(): ResourceAdapter.ResourceClickListener {
        return object : ResourceAdapter.ResourceClickListener{
            @SuppressLint("RestrictedApi")
            override fun onItemClick(holder: ResourceAdapter.ResourceViewHolder, position: Int) {
                val resource = resourceList?.getOrNull(position)?: return
                Toast.makeText(requireContext(), "Clicou no item ${resource.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object{
        private val TAG = "InventoryFragment"
    }

}