package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.model.Operation
import com.agrosync.agrosyncapp.data.model.Resource
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import com.agrosync.agrosyncapp.data.model.ResourceOperation
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.data.repository.ResourceMovementRepository
import com.agrosync.agrosyncapp.data.repository.ResourceRepository
import com.agrosync.agrosyncapp.data.repository.UserRepository
import com.agrosync.agrosyncapp.databinding.FragmentAddResourceBinding
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddResourceFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    private lateinit var _binding: FragmentAddResourceBinding
    private val binding get() = _binding
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    private lateinit var financeRepository: FinanceRepository
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var resourceMovementRepository: ResourceMovementRepository
    private lateinit var spinnerOperations: Spinner
    private lateinit var spinnerOperationAdapter: ArrayAdapter<String>
    private lateinit var selectedOperation: ResourceOperation
    private var calendar = Calendar.getInstance()
    private lateinit var resource: Resource


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddResourceBinding.inflate(inflater, container, false)

        resourceRepository = ResourceRepository()
        resourceMovementRepository = ResourceMovementRepository()
        userRepository = UserRepository()
        financeRepository = FinanceRepository()
        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        resource = mainViewModel.refResource
        spinnerOperations = binding.spinnerOperations

        binding.tvResourceAddTitle.text = resource.name
        binding.tvMeasureUnit.text = resource.measureUnit.displayName

        setupOperationSpinner()
        setupDatePicker()

        binding.btnSave.setOnClickListener{
            addResource()
        }

        binding.btnCancel.setOnClickListener{
            navController.navigate(R.id.action_addResourceFragment_to_resourceDetailFragment)
        }
    }

    private fun setupOperationSpinner() {
        val listOperation = ResourceOperation.entries.map { it.displayName }

        spinnerOperationAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOperation)
        spinnerOperationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOperations.adapter = spinnerOperationAdapter

        spinnerOperations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOperation = ResourceOperation.entries[position]
                Toast.makeText(requireContext(), "Selecionado: $selectedOperation", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupDatePicker() {
        calendar = Calendar.getInstance()
        updateDateInView()
        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.edtDate.setText(sdf.format(calendar.time))
    }

    private fun addResource() {
        val title = "Movimenção de Insumo"
        var value = binding.etValue.text.toString()
        val amount = binding.etAmount.text.toString()
        val description = binding.etDescription.text.toString()
        val dateString = binding.edtDate.text.toString()

        if (value.isEmpty()){
            value = 0.0.toString()
        }else if (selectedOperation == ResourceOperation.BUY){
            value = "-$value"
        }

        if (amount.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val userUid = firebaseAuth.currentUser?.uid
        if (userUid != null)
            lifecycleScope.launch {
                val user = userRepository.findById(userUid)
                val farm = mainViewModel.refFarm

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val parsedDate = sdf.parse(dateString) ?: Date()

                val resourceMovement = ResourceMovement(
                    "",
                    resource.id,
                    user?.id!!,
                    amount.toDouble(),
                    resource.totalAmount,
                    value.toDouble(),
                    selectedOperation,
                    parsedDate
                )

                Log.d(TAG,"Resource Amount: ${amount.toDouble()}")
                resource.calcResourceNewAmount(selectedOperation, amount.toDouble())
                resource.calcResourceNewTotalValue(selectedOperation, value.toDouble())
                resource.farm = farm

                resourceMovement.newResourceAmount = resource.totalAmount

                resourceMovementRepository.save(resourceMovement, onSuccess = { response ->
                        if (response.isNotEmpty()){
                            Toast.makeText(requireContext(), "Movimentação Salva com sucesso", Toast.LENGTH_SHORT).show()

                            if (value.toDouble() != 0.0){
                                val operation: Operation
                                if (selectedOperation == ResourceOperation.BUY)
                                    operation = Operation.WITHDRAWAL
                                else
                                    operation = Operation.ENTRY

                                val finance = Finance(
                                    "",
                                    user,
                                    farm,
                                    title,
                                    description,
                                    value.toDouble(),
                                    operation,
                                    parsedDate,
                                    true
                                )
                                finance.resource = resource

                                financeRepository.create(finance, onSuccess = { res ->
                                    if (res.isEmpty()){
                                        Toast.makeText(requireContext(), "Erro ao criar financia", Toast.LENGTH_SHORT).show()
                                        Log.e(TAG, "Erro ao criar financia")
                                    }
                                })
                            }

                            resourceRepository.save(resource, onSuccess = { r ->
                                if (r.isEmpty()){
                                    Toast.makeText(requireContext(), "Erro ao atualizar o Insumo", Toast.LENGTH_SHORT).show()
                                }
                            })

                            navController.navigate(R.id.action_addResourceFragment_to_resourceDetailFragment)
                        } else
                            Toast.makeText(requireContext(), "Erro ao salvar movimentação", Toast.LENGTH_SHORT).show()
                    })

            }
    }

    companion object {
        private val TAG = "AddResourceFragment"
    }
}