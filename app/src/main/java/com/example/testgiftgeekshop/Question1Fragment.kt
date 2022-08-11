package com.example.testgiftgeekshop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.databinding.FragmentQuestion1Binding
import com.example.testgiftgeekshop.utils.ScreenModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@AndroidEntryPoint
@FragmentScoped
class Question1Fragment : Fragment() {

    private val mainViewModel by activityViewModels<MainVievModel>()
    private var _binding: FragmentQuestion1Binding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    private val btnConfirmActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.btn_confirm_custom_on)
    }

    private val btnConfirmNonActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.btn_confirm_custom_off)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel.screenFirstState.observe(viewLifecycleOwner) {
            mainViewModel.initCheckBoxCustom(binding.imgPoint1, it.ans1.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint2, it.ans2.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint3, it.ans3.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint4, it.ans4.isActive)
            initBtnConfirmObserve(it)
        }
        initOnBtnAnsPressed()


        //for future
//        mainViewModel.statusMessage.observe(viewLifecycleOwner) {
//            it.getContentIfNotHandled()?.let { text ->
//                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
//            }
//        }
//        binding.btnConfirm.setOnClickListener {
//            mainViewModel.pressedBtnConfirm()
//        }
//        mainViewModel.navigateToNextScreen.observe(viewLifecycleOwner){
//            navigateToNextQuestion()
//        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


//    private fun initCheckBoxCustom(imgView: ImageView, isActive: Boolean) {
//        if (isActive) {
//            imgView.setImageResource(R.drawable.icon_point_on)
//            return
//        }
//        imgView.setImageResource(R.drawable.icon_point_off)
//    }

    private fun initOnBtnAnsPressed() {
        binding.tvQuestAns1.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns1.text.toString())
        }
        binding.tvQuestAns2.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns2.text.toString())
        }
        binding.tvQuestAns3.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns3.text.toString())
        }
        binding.tvQuestAns4.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns4.text.toString())
        }
        binding.btnRestartTestAll.setOnClickListener {
            initAlertDialog()
        }
    }

    private fun initSetOnClickListener(text: String) {
        mainViewModel.pressedAnswer1(text)
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(R.id.action_question1Fragment_to_question2Fragment)
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Пройти тест заново")
            .setMessage("Ви точно хочете пройти тест заново, дані поточного тесту не будуть збережені?")
            .setPositiveButton("Restart") { _, _ ->
                mainViewModel.restartTest()
                findNavController().navigate(R.id.action_question1Fragment_to_startFragment)
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }

    private fun initBtnConfirmObserve(it: ScreenModel) {
        binding.apply {
            if (it.isAnyAnswerActive) {
                btnConfirm.background = btnConfirmActive
                btnConfirm.alpha = 1f
                btnConfirm.setOnClickListener {
                    navigateToNextQuestion()
                }
            } else {
                btnConfirm.setOnClickListener {
                    Snackbar.make(it, "Оберіть хоча б один із варіантів", Snackbar.LENGTH_SHORT).show()
                }
                btnConfirm.background = btnConfirmNonActive
                btnConfirm.alpha = 0.3f
            }
        }
    }
}