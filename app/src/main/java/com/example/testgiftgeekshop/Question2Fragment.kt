package com.example.testgiftgeekshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.databinding.ActivityMainBinding.inflate
import com.example.testgiftgeekshop.databinding.FragmentQuestion1Binding
import com.example.testgiftgeekshop.databinding.FragmentQuestion2Binding
import com.example.testgiftgeekshop.databinding.FragmentQuestion3Binding
import com.example.testgiftgeekshop.databinding.FragmentStartBinding
import com.example.testgiftgeekshop.utils.ScreenModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@AndroidEntryPoint
@FragmentScoped
class Question2Fragment : Fragment() {

    private val mainViewModel by activityViewModels<MainVievModel>()
    private var _binding: FragmentQuestion2Binding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private val btnConfirmActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.btn_confirm_custom_on)
    }

    private val btnConfirmNonActive by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.btn_confirm_custom_off)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel.screenSecondState.observe(viewLifecycleOwner) {
            mainViewModel.initCheckBoxCustom(binding.imgPoint1, it.ans1.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint2, it.ans2.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint3, it.ans3.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint4, it.ans4.isActive)
            mainViewModel.initCheckBoxCustom(binding.imgPoint5, it.ans5.isActive)
            initBtnConfirmObserve(it)
        }
        initOnBtnAnsPressed()


        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun navigateToNextQuestion() {
        findNavController().navigate(R.id.action_question2Fragment_to_question3Fragment)
    }

    private fun navigateToMarvelExternalQuestion() {
        findNavController().navigate(R.id.action_question2Fragment_to_externalMarvelDcQuestionFragment)
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
        binding.tvQuestAns4.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns4.text.toString())
        }
        binding.tvQuestAns5.setOnClickListener {
            initSetOnClickListener(binding.tvQuestAns5.text.toString())
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
                findNavController().navigate(R.id.action_question2Fragment_to_startFragment)
                mainViewModel.restartTest()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }

    private fun initSetOnClickListener(text: String) {
        mainViewModel.pressedAnswer2(text)
    }

    private fun initBtnConfirmObserve(it: ScreenModel) {
        binding.apply {
            if (!it.isAnyAnswerActive) {
                btnConfirm.setOnClickListener {
                    Snackbar.make(it, "Оберіть хоча б один із варіантів", Snackbar.LENGTH_SHORT)
                        .show()
                }
                btnConfirm.background = btnConfirmNonActive
                btnConfirm.alpha = 0.3f
                return
            } else {
                // if at least one var is active

                btnConfirm.background = btnConfirmActive
                btnConfirm.alpha = 1f
                if (it.ans2.isActive) {
                    // MARVEL / DC
                    btnConfirm.setOnClickListener {
                        navigateToMarvelExternalQuestion()
                    }
                } else {
                    btnConfirm.setOnClickListener {
                        navigateToNextQuestion()
                    }
                }
            }
        }
    }
}