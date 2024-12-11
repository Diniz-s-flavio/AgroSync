package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import com.agrosync.agrosyncapp.data.repository.ResourceMovementRepository
import com.agrosync.agrosyncapp.databinding.FragmentResourceMovimentBinding
import com.agrosync.agrosyncapp.ui.adapter.ResourceMovimentAdapter
import com.agrosync.agrosyncapp.ui.adapter.SwipeToDeleteCallback
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class ResourceMovimentFragment : Fragment() {
    private lateinit var  navController: NavController
    private var _binding: FragmentResourceMovimentBinding? = null
    private val binding get() = _binding!!
    private lateinit var resourceMovimentAdapter: ResourceMovimentAdapter
    private lateinit var resourceMovimentRepository: ResourceMovementRepository
    private lateinit var firebaseAuth: FirebaseAuth

    private val months = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceMovimentBinding.inflate(inflater, container, false)

        resourceMovimentRepository = ResourceMovementRepository()
        firebaseAuth = FirebaseAuth.getInstance()

        setupRecyclerViewHeight()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        setupRecyclerView()

        setupMonthSpinner()

        binding.addResourceButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putBoolean("isFromResource", true)
            navController.navigate(R.id.action_resourceMovimentFragment_to_addResourceFragment, bundle)
        }

    }

    private fun setupRecyclerViewHeight() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        val dpToPx = 296 * displayMetrics.density

        val desiredHeight = (screenHeight - dpToPx).toInt()

        binding.resourceMovimentRecyclerView.layoutParams.height = desiredHeight
        binding.resourceMovimentRecyclerView.requestLayout()
    }

    private fun setupRecyclerView() {
        resourceMovimentAdapter = ResourceMovimentAdapter(emptyList()) { resourceMoviments ->
            val bundle = Bundle()
            bundle.putSerializable("resourceMoviments", resourceMoviments)
        }

        binding.resourceMovimentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resourceMovimentAdapter
        }

        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_withdrawal)
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val resourceMovement = resourceMovimentAdapter.resourceMoviments[position]

                deleteResourceMovement(resourceMovement, position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.resourceMovimentRecyclerView)
    }

    private fun deleteResourceMovement(resourceMovement: ResourceMovement, position: Int) {
        resourceMovimentRepository.deleteResourceMovement(resourceMovement.id, onSuccess = {
            val updatedList = resourceMovimentAdapter.resourceMoviments.toMutableList()
            updatedList.removeAt(position)

            resourceMovimentAdapter.updateData(updatedList)

            resourceMovimentAdapter.notifyItemRemoved(position)

            if (updatedList.isEmpty()) {
                binding.resourceMovimentRecyclerView.visibility = View.GONE
            } else {
                binding.resourceMovimentRecyclerView.visibility = View.VISIBLE
            }
        }, onFailure = { e ->
            Log.e("ResourceMovimentFragment", "Erro ao excluir movimentação", e)
        })
    }

    private fun setupMonthSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            months
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerPeriodo.adapter = adapter

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerPeriodo.setSelection(currentMonth)

        binding.spinnerPeriodo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadResourceMovementByData(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun loadResourceMovementByData(selectedMonthPosition: Int) {
        val currentUser = firebaseAuth.currentUser
        val resourceId = arguments?.getString("resourceId")
        if (currentUser != null) {

            resourceId?.let {
                resourceMovimentRepository.findByResourceId(
                    it,
                    onSuccess = { resourceMovements ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.MONTH, selectedMonthPosition)
                        val selectedMonth = selectedMonthPosition
                        val selectedYear = calendar.get(Calendar.YEAR)

                        val monthlyFinances = resourceMovements.filter { resourceMovement ->
                            val resourceMovimentCalendar = Calendar.getInstance().apply {
                                time = resourceMovement.movementDate
                            }
                            resourceMovimentCalendar.get(Calendar.MONTH) == selectedMonth &&
                                    resourceMovimentCalendar.get(Calendar.YEAR) == selectedYear
                        }.sortedByDescending { it.movementDate }

                        updateResourceMovimentList(monthlyFinances)

                    },
                    onFailure = { e ->
                        Log.e("ResouceMovimentFragment", "Erro ao carregar movimentações", e)
                    }
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateResourceMovimentList(resourceMoviments: List<ResourceMovement>) {
        resourceMovimentAdapter = ResourceMovimentAdapter(resourceMoviments) { resourceMoviment ->
            val bundle = Bundle()
            bundle.putSerializable("resourceMoviment", resourceMoviment)

            navController.navigate(R.id.action_resourceMovimentFragment_to_resourceMovimentDetailFragment, bundle)
        }
        binding.resourceMovimentRecyclerView.adapter = resourceMovimentAdapter
        resourceMovimentAdapter.notifyDataSetChanged()

        if (resourceMoviments.isEmpty()) {
            binding.resourceMovimentRecyclerView.visibility = View.GONE
        } else {
            binding.resourceMovimentRecyclerView.visibility = View.VISIBLE
        }
    }
}