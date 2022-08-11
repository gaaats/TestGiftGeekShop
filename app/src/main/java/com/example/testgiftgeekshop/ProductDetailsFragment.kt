package com.example.testgiftgeekshop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.testgiftgeekshop.adapters.MoreImagesPagerAdapter
import com.example.testgiftgeekshop.databinding.FragmentProductDetailsBinding
import com.example.testgiftgeekshop.utils.Constances
import com.example.testgiftgeekshop.utils.ResourceVrap
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@AndroidEntryPoint
@FragmentScoped
class ProductDetailsFragment : Fragment() {

    @Inject
    lateinit var pagerAdapter: MoreImagesPagerAdapter

    private val mainViewModel by activityViewModels<MainVievModel>()
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
        Log.d("image_test", "i am in onViewCreated")
        Log.d("image_test", "element is ${mainViewModel.singleProductOpened.value?.data?.name}")
        Log.d("image_test", "element is ${mainViewModel.singleProductOpened.value?.data?.images}")
        mainViewModel.singleProductOpened.observe(viewLifecycleOwner){
            Log.d("image_test", "images: ${it.data?.images}")
            when (it){
                is ResourceVrap.Success -> {
                    Log.d("image_test", "images: ${it.data?.images}")
                    binding.viewPager.adapter = pagerAdapter
                    pagerAdapter.submitList(it.data!!.images)
                }
                is ResourceVrap.Loading -> {

                }
                is ResourceVrap.Error -> {

                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
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