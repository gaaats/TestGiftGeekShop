package com.example.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.testgiftgeekshop.databinding.FragmentThankYouBinding
import com.example.testgiftgeekshop.presentation.ShopCartViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
@FragmentScoped
class ThankYouFragment : Fragment() {


    private val shopCartViewModel by activityViewModels<ShopCartViewModel>()
    private var _binding: FragmentThankYouBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThankYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(2500)
            try {
                startActivity(shopCartViewModel.sendOrderToNusick())
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    e.message!!,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}