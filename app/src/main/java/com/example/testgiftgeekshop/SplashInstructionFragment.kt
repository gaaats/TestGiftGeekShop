package com.example.testgiftgeekshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.databinding.FragmentSplashInstructionBinding
import com.example.testgiftgeekshop.databinding.FragmentThankYouBinding
import com.example.testgiftgeekshop.utils.Constances

class SplashInstructionFragment : Fragment() {

    private var _binding: FragmentSplashInstructionBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lottieAnimOk.setOnClickListener {
            findNavController().navigate(R.id.action_splashInstructionFragment_to_question1Fragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}