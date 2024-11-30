package com.agrosync.agrosyncapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        binding?.bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    // Troque para o fragmento relacionado ao Item 1
//                    navigateToFragment(Item1Fragment())
                    true
                }

                R.id.page_2 -> {
                    // Troque para o fragmento relacionado ao Item 2
//                    navigateToFragment(Item2Fragment())
                    true
                }

                else -> false
            }
        }


    }
}