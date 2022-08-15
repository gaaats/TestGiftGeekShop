package com.example.testgiftgeekshop

import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.adapters.MoreImagesPagerAdapter
import com.example.testgiftgeekshop.databinding.FragmentProductDetailsBinding
import com.example.testgiftgeekshop.presentation.ShopCartViewModel
import com.example.testgiftgeekshop.utils.Constances
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@FragmentScoped
class ProductDetailsFragment : Fragment() {

    @Inject
    lateinit var pagerAdapter: MoreImagesPagerAdapter

    private val mainViewModel by activityViewModels<MainVievModel>()
    private val shopCartViewModel by activityViewModels<ShopCartViewModel>()
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            binding.mainGroup.visibility = View.GONE
            binding.lottieAnimVaiting.visibility = View.VISIBLE
            delay(1500)
            binding.mainGroup.visibility = View.VISIBLE
            binding.lottieAnimVaiting.visibility = View.GONE
            binding.lottieAnimVaiting.pauseAnimation()

        }


        btnImgExitSetOnClickListener()
        btnAddToBasketSetOnClickListener()

        Log.d("image_test", "i am in onViewCreated")
        Log.d("image_test", "element is ${mainViewModel.singleProductOpened.value?.data?.name}")
        Log.d("image_test", "element is ${mainViewModel.singleProductOpened.value?.data?.images}")
        mainViewModel.singleProductOpened.observe(viewLifecycleOwner) {
            Log.d("image_test", "images: ${it.data?.images}")
            when (it) {
                is ResourceVrap.Success -> {
                    Log.d("image_test", "images: ${it.data?.images}")
                    binding.viewPager.adapter = pagerAdapter
                    pagerAdapter.submitList(it.data!!.images)
                    binding.tvName.text = it.data.name
                    fromHtml(it.data.description).also { text ->
                        binding.tvDesc.text = text
                    }
                    Log.d("desc_test", "desc ${it.data}")
                    binding.indicatorDot.setViewPager(binding.viewPager)
                }
                is ResourceVrap.Loading -> {

                }
                is ResourceVrap.Error -> {

                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun btnImgExitSetOnClickListener() {
        binding.btnImgExit.setOnClickListener {
            findNavController().navigate(R.id.action_productDetailsFragment_to_resultFragment)
        }
    }

    private fun btnAddToBasketSetOnClickListener() {
        binding.btnAddToBasket.setOnClickListener {
            shopCartViewModel.addToShopCart(mainViewModel.singleProductOpened.value!!.data!!)
            Snackbar.make(
                binding.root,
                "Ви додали до корзини: ${mainViewModel.singleProductOpened.value!!.data!!.name}",
                Snackbar.LENGTH_SHORT
            ).show()
            lifecycleScope.launch {
                binding.viewPager.visibility = View.GONE
                binding.lottieAnimAddedToCart.setAnimation(R.raw.lottie_anim_added_to_cart)
                binding.lottieAnimAddedToCart.visibility = View.VISIBLE
                binding.lottieAnimAddedToCart.playAnimation()
                delay(2000)
                binding.viewPager.visibility = View.VISIBLE
                binding.lottieAnimAddedToCart.pauseAnimation()
                binding.lottieAnimAddedToCart.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {

//        fun newInstance(itemId:Int): ProductDetailsFragment {
//            return ProductDetailsFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(Constances.KEY_PRODUCT_ID, itemId)
//                }
//            }
//        }
    }
}