package com.example.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.testgiftgeekshop.adapters.ProductsListAdapter
import com.example.testgiftgeekshop.adapters.ShopCartListAdapter
import com.example.testgiftgeekshop.databinding.FragmentResultBinding
import com.example.testgiftgeekshop.databinding.FragmentShopCartBinding
import com.example.testgiftgeekshop.presentation.ShopCartViewModel
import com.example.testgiftgeekshop.utils.ScreenModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@AndroidEntryPoint
@FragmentScoped
class ShopCartFragment : Fragment() {

    @Inject
    lateinit var productsAdapter: ShopCartListAdapter
    private val shopCartViewModel by activityViewModels<ShopCartViewModel>()
    private var _binding: FragmentShopCartBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")
    private val btnConfirmNonActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.btn_confirm_custom_off)
    }
    private val btnConfirmActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.tv_custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        shopCartViewModel.isShopCartEmpty.observe(viewLifecycleOwner){
            initBtnConfirmObserve(it)
        }

        shopCartViewModel.totalSum.observe(viewLifecycleOwner) {
            val sum = "$it₴"
            binding.tvTotalSumCount.text = sum
        }
        shopCartViewModel.listShopCart.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it)
        }
        binding.recyclerViewShopCart.adapter = productsAdapter
        addHorDividers()

        productsAdapter.setOnItemPlusClickListener {
            shopCartViewModel.addToShopCart(it)
        }

        productsAdapter.setOnItemDeleteClickListener {
            shopCartViewModel.removeFromShopCart(it)
        }

        productsAdapter.setOnItemMinusClickListener {
            shopCartViewModel.minusProductFromShopCart(it)
        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun addHorDividers() {
        binding.recyclerViewShopCart.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.recyclerViewShopCart.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initBtnConfirmObserve(it: Boolean) {
        binding.apply {
            if (!it) {
                lottieEmptyShopCart.visibility = View.GONE
                lottieEmptyShopCart.pauseAnimation()
                btnConfirmOrder.background = btnConfirmActive
                btnConfirmOrder.alpha = 1f
                btnConfirmOrder.setOnClickListener {
                    navigateToNextQuestion()
                }
                return
            }
            btnConfirmOrder.setOnClickListener {
                Snackbar.make(it, "Нам сумно, що Ваша корзинка порожня :(", Snackbar.LENGTH_SHORT).show()
            }
            btnConfirmOrder.background = btnConfirmNonActive
            btnConfirmOrder.alpha = 0.3f
            lottieEmptyShopCart.visibility = View.VISIBLE
            lottieEmptyShopCart.playAnimation()

        }
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(R.id.action_shopCartFragment_to_completeOrderFragment)
    }
}