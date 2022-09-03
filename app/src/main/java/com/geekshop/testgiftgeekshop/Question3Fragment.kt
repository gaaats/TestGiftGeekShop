package com.geekshop.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.geekshop.testgiftgeekshop.databinding.ActivityMainBinding.inflate
import com.geekshop.testgiftgeekshop.databinding.FragmentQuestion3Binding
import com.geekshop.testgiftgeekshop.utils.ScreenModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@AndroidEntryPoint
@FragmentScoped
class Question3Fragment : Fragment() {

    private val mainViewModel by activityViewModels<MainVievModel>()
    private var _binding: FragmentQuestion3Binding? = null
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
        _binding = FragmentQuestion3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel.screenThirdState.observe(viewLifecycleOwner) {
            mainViewModel.initCheckBoxCustom(binding.imgPoint1, it.ans1.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint2, it.ans2.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint3, it.ans3.isActive)
            initBtnConfirmObserve(it)
        }
        initOnBtnAnsPressed()
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
                    Snackbar.make(it, "Оберіть хоча б один із варіантів", Snackbar.LENGTH_SHORT)
                        .show()
                }
                btnConfirm.background = btnConfirmNonActive
                btnConfirm.alpha = 0.3f
            }
        }
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(R.id.action_question3Fragment_to_question4Fragment)
    }

    private fun initSetOnClickListener(text: String) {
        mainViewModel.pressedAnswer3(text)
    }

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
        binding.btnRestartTestAll.setOnClickListener {
            initAlertDialog()
        }
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Пройти тест заново")
            .setMessage("Ви точно хочете пройти тест заново, дані поточного тесту не будуть збережені?")
            .setPositiveButton("Restart") { _, _ ->
                findNavController().navigate(R.id.action_question3Fragment_to_startFragment)
                mainViewModel.restartTest()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }
}