package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.databinding.FragmentInventoryBinding
import com.agrosync.agrosyncapp.databinding.FragmentResourceDetailBinding
import com.google.firebase.auth.FirebaseAuth

class ResourceDetailFragment : Fragment() {
    private lateinit var _binding: FragmentResourceDetailBinding
    private val binding get() = _binding
    private lateinit var resource: Resource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceDetailBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments?.getBundle("resource")
        resource = bundle?.let { Resource.fromBundle(it) }!!
        binding.tvResourceDetailTitle.text = resource.name
    }

    companion object {
        private val TAG = "ResourceDetailFragment"
    }
}