package com.geekshop.testgiftgeekshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.testgiftgeekshop.adapters.ShopCartListAdapter
import com.geekshop.testgiftgeekshop.databinding.FragmentShopCartBinding
import com.geekshop.testgiftgeekshop.presentation.ShopCartViewModel
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

        shopCartViewModel.isShopCartEmpty.observe(viewLifecycleOwner) {
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

        productsAdapter.setOnItemDeleteClickListener { singleProduct, countProductPreDelete ->
            initAlertDialogDeleteFromShopCart(singleProduct, countProductPreDelete)
        }

        productsAdapter.setOnItemMinusClickListener {
            shopCartViewModel.minusProductFromShopCart(it)
        }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAlertDialogDeleteFromShopCart(
        singleProduct: GeekProductUI,
        countProductPreDelete: Int
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Видалення з корзинки")
            .setMessage("Ви точно хочете даний товар з Вашої корзинки?")
            .setPositiveButton("Видалити") { _, _ ->
                shopCartViewModel.removeFromShopCart(singleProduct, countProductPreDelete)
            }
            .setNegativeButton("Залишити") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
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
            } else {
                btnConfirmOrder.setOnClickListener {
                    Snackbar.make(
                        it,
                        "Нам сумно, що Ваша корзинка порожня :(",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                btnConfirmOrder.background = btnConfirmNonActive
                btnConfirmOrder.alpha = 0.3f
                lottieEmptyShopCart.visibility = View.VISIBLE
                lottieEmptyShopCart.playAnimation()
            }
        }
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(R.id.action_shopCartFragment_to_completeOrderFragment)
    }
}