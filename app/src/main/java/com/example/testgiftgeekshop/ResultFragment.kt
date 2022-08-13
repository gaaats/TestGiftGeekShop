package com.example.testgiftgeekshop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.testgiftgeekshop.adapters.ProductsListAdapter
import com.example.testgiftgeekshop.databinding.FragmentResultBinding
import com.example.testgiftgeekshop.presentation.ShopCartViewModel
import com.example.testgiftgeekshop.utils.Constances
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.example.testgiftgeekshop.utils.SnackBarListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
@FragmentScoped
class ResultFragment : Fragment() {

    @Inject
    lateinit var productsAdapter: ProductsListAdapter
    private val mainViewModel by activityViewModels<MainVievModel>()
    private val shopCartViewModel by activityViewModels<ShopCartViewModel>()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        shopCartViewModel.totalSum.observe(viewLifecycleOwner) {
            binding.tvTotalSumCount.text = it.toString()
        }

//        Log.d("onViewCreated", "${mainViewModel.listProducts.value!!.data}")

        startLoadingProgBar()
        iniLoadListProducts()
        initAdapterRecViev()
        observeProductList()
        addVertAndHorDividers()

        setOnClickNavigateToCompleteOrderFragment()
        setOnClickListenerGoToSite()
        setOnClickListenerRestartBtn()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun startLoadingProgBar() {
        lifecycleScope.launch {
            binding.resScrnMainGroup.visibility = View.INVISIBLE
            binding.lottieAnimVaiting.visibility = View.VISIBLE
            binding.tvPleaseVaitLoading.visibility = View.VISIBLE
            delay(2000)
            binding.resScrnMainGroup.visibility = View.VISIBLE
            binding.lottieAnimVaiting.visibility = View.GONE
            binding.tvPleaseVaitLoading.visibility = View.GONE
        }
    }

    private fun setOnClickListenerRestartBtn() {
        binding.btnRestartTestAll.setOnClickListener {
            initAlertDialog()
        }
    }

    private fun setOnClickListenerGoToSite() {
        binding.tvGoToSite.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constances.LINK_TO_GEEKSHOP)
                startActivity(this)
            }
        }
    }

    private fun setOnClickNavigateToCompleteOrderFragment() {
        binding.btnConfirmOrder.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_shopCartFragment)
        }
    }

    private fun observeProductList() {
        mainViewModel.listProducts.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it.data)
        }
    }

    private fun iniLoadListProducts() {
//        lifecycleScope.launch {
//            mainViewModel.loadList()
//        }
    }

    private fun initAdapterRecViev() {
        binding.recyclerView.adapter = productsAdapter
        productsAdapter.setOnItemClickListener {
            //done
            Snackbar.make(binding.root, "You pressed: $it element", Snackbar.LENGTH_SHORT).show()
            lifecycleScope.launch {
                mainViewModel.loadPressedSingleProduct(it)
            }
            findNavController().navigate(R.id.action_resultFragment_to_productDetailsFragment)
        }

        // on btn add to shop cart listener
        productsAdapter.setOnItemAddToCartClickListener {
            Snackbar.make(binding.root, "Ви додали до корзини: ${it.name}", Snackbar.LENGTH_SHORT).show()
            lifecycleScope.launch {
                binding.lottieAnimAddedToCart.visibility = View.VISIBLE
                binding.lottieAnimAddedToCart.playAnimation()
                delay(1800)
                binding.lottieAnimAddedToCart.visibility = View.GONE
                binding.lottieAnimAddedToCart.pauseAnimation()
            }
            shopCartViewModel.addToShopCart(it)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun addVertAndHorDividers() {
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Пройти тест заново")
            .setMessage("Ви точно хочете пройти тест заново, дані поточного тесту не будуть збережені?")
            .setPositiveButton("Restart") { _, _ ->
                findNavController().navigate(R.id.action_resultFragment_to_startFragment)
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(false)
            .create()
            .show()
    }
}