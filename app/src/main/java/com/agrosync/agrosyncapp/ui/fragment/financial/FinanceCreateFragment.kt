package com.agrosync.agrosyncapp.ui.fragment.financial

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.model.Operation
import com.agrosync.agrosyncapp.data.repository.FarmRepository
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.data.repository.UserRepository
import com.agrosync.agrosyncapp.databinding.FragmentFinanceCreateBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FinanceCreateFragment : Fragment() {

    private var binding: FragmentFinanceCreateBinding? = null
    private lateinit var navController: NavController
    private lateinit var spinnerOperaton: Spinner
    private lateinit var spinnerOperationAdapter: ArrayAdapter<String>
    private lateinit var selectedOperation: Operation
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var farmRepository: FarmRepository
    private lateinit var userRepository: UserRepository
    private lateinit var financeRepository: FinanceRepository
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinanceCreateBinding.inflate(inflater, container, false)
        farmRepository = FarmRepository()
        userRepository = UserRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerOperaton = view.findViewById(R.id.spinnerOperations)
        navController = view.findNavController()

        financeRepository = FinanceRepository()
        setupOperationSpinner()
        setupDatePicker()

        binding?.btnSave?.setOnClickListener{
            saveFinance()
        }

        binding?.btnCancel?.setOnClickListener{
            navController.navigate(R.id.action_finance_create_fragment_to_financialFragment)
        }
    }

    private fun setupDatePicker() {
        calendar = Calendar.getInstance() // Obtém a data atual
        updateDateInView()
        binding?.etDate?.setOnClickListener {
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
        binding?.etDate?.setText(sdf.format(calendar.time))
    }

    private fun setupOperationSpinner() {
        val listOperation = Operation.entries.map { it.displayName }

        spinnerOperationAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOperation)
        spinnerOperationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOperaton.adapter = spinnerOperationAdapter

        spinnerOperaton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOperation = Operation.entries[position]
                Toast.makeText(requireContext(), "Selecionado: $selectedOperation", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun saveFinance() {
        // Obtendo os valores dos campos
        val title = binding?.etName?.text.toString()
        val value = binding?.etValue?.text.toString()
        val description = binding?.etDescription?.text.toString()
        val dateString = binding?.etDate?.text.toString()

        // Validando os campos
        if (title.isEmpty() || value.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val userUid = firebaseAuth.currentUser?.uid
        if (userUid != null)
            farmRepository.findByOwnerId(userUid, onSuccess = {farm ->
                if (farm != null) {
                    lifecycleScope.launch {
                        val user = userRepository.findById(userUid)

                        // Parse the date
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDate = sdf.parse(dateString) ?: Date()

                        val finance = Finance(
                            "",
                            user!!,
                            farm,
                            title,
                            description,
                            value.toDouble(),
                            selectedOperation,
                            parsedDate,
                            isFromResource = false
                        )

                        financeRepository.create(finance, onSuccess = { response ->
                            if (response.isNotEmpty()){
                                Toast.makeText(requireContext(), "Financia salvada com sucesso", Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_finance_create_fragment_to_financialFragment)
                            } else
                                Toast.makeText(requireContext(), "Erro ao criar financia", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}