package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.databinding.FragmentInventoryBinding
import com.agrosync.agrosyncapp.databinding.FragmentResourceDetailBinding
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ResourceDetailFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    private lateinit var _binding: FragmentResourceDetailBinding
    private val binding get() = _binding
    private lateinit var resource: Resource
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resource = mainViewModel.refResource

        navController = view.findNavController()

        binding.tvResourceDetailTitle.text = resource.name
        binding.edtAmountDisplay.text = resource.totalAmount.toString() + " " + resource.measureUnit.acronym
        binding.tvCostLabel.text = if (resource.totalValue<0) "Custo:" else "Lucro:"
        binding.edtCostDisplay.text = resource.formatToCurrency()
        binding.edtDescriptionDisplay.text = resource.description
        binding.tvCategory.text = resource.category.displayName

        if (resource.imgUrl.isNotBlank()){
            Glide.with(binding.ivPhotoDisplay.context)
                .load(resource.imgUrl)
                .placeholder(R.drawable.resource_img_placeholder)
                .error(R.drawable.resource_img_placeholder)
                .into(binding.ivPhotoDisplay)
        }

        binding.editResourceButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putBoolean("isEditing", true)
            navController.navigate(R.id.action_resourceDetailFragment_to_resourceCreateFragment, bundle)
        }

        binding.btnHistorico.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("resourceId", resource.id)
            navController.navigate(R.id.action_resourceDetailFragment_to_resourceMovimentFragment,bundle)
        }

    }

    companion object {
        private val TAG = "ResourceDetailFragment"
    }
}