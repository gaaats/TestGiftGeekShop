package com.geekshop.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.geekshop.testgiftgeekshop.databinding.FragmentEmptyResultBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@AndroidEntryPoint
@FragmentScoped
class EmptyResultFragment : Fragment() {

    private val mainViewModel by activityViewModels<MainVievModel>()
    private var _binding: FragmentEmptyResultBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmptyResultBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lottieAnimRestart.setOnClickListener {
            mainViewModel.restartTest()
            findNavController().navigate(R.id.action_emptyResultFragment_to_startFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}