package com.example.testgiftgeekshop

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testgiftgeekshop.databinding.FragmentStartBinding
import com.google.android.material.snackbar.Snackbar

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lottieAnim.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_splashInstructionFragment)
        }

        // for fast test
        binding.tvStScrnHaveIdea.setOnClickListener {

            findNavController().navigate(R.id.action_startFragment_to_completeOrderFragment)
//            testViberSend4()

//            findNavController().navigate(R.id.action_startFragment_to_resultFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    // just share text and ask to find vhoom
    fun testViberSend(): Intent {

        val text = "rtgtgttgt"
        val number = "+380632326954"

        val applicationpackage = "com.viber.voip"

        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT, text)
        return if (applicationpackage === "choose") {
            Intent.createChooser(i, "Share using")
        } else {
            i.setPackage(applicationpackage)
            i
        }


    }

    fun testViberSend2(): Intent {

        val text = "rtgtgttgt"
        val number = "+380632326954"
        val applicationpackage = "com.viber.voip"

        val i = Intent(Intent.ACTION_SEND).apply {
            `package` = applicationpackage
            data = Uri.parse("smsto:$number")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        return i


    }

    fun testViberSend3(): Intent {

        val text = "rtgtgttgt"
        val number = "+380632326954"
        val applicationpackage = "com.viber.voip"

        val sendIntent = Intent(Intent.ACTION_SEND)
//        sendIntent.type = "text/plain"
        sendIntent.putExtra("address", "smsto:$number")
//        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.`package` = applicationpackage
        return sendIntent


    }


    fun testViberSend4() {

        val text = "тут буде текст замовлення"
        val number = "+380632326954"


        if (checkIsWattsUppInstalled()) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=${number}&text=${text}")
            )
            startActivity(intent)
            return
        }
        Snackbar.make(
            binding.root,
            "Встановіть, будь ласка, WattsUpp",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun checkIsWattsUppInstalled(): Boolean {
        val applicationpackage = "com.whatsapp"
        val packageManager = requireContext().packageManager
        return try {
            packageManager.getPackageInfo(applicationpackage, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}