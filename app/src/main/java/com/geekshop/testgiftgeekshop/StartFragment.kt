package com.geekshop.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.geekshop.testgiftgeekshop.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lottieAnim.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_splashInstructionFragment)
        }

        // for fast test
//        binding.tvStScrnHaveIdea.setOnClickListener {
//
//            findNavController().navigate(R.id.action_startFragment_to_completeOrderFragment)
//
////            findNavController().navigate(R.id.action_startFragment_to_resultFragment)
//        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}