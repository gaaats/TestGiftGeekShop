package com.example.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.databinding.FragmentCompleteOrderBinding
import com.example.testgiftgeekshop.databinding.FragmentResultBinding

class CompleteOrderFragment : Fragment() {

    private var _binding: FragmentCompleteOrderBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.testV.setOnClickListener {
            findNavController().navigate(R.id.action_completeOrderFragment_to_thankYouFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}