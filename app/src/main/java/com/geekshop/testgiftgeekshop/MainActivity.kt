package com.geekshop.testgiftgeekshop

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.geekshop.testgiftgeekshop.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@AndroidEntryPoint
@ActivityScoped
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainVievModel>()
    private var backPressedTime: Long = 0
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        val fragmentInstance =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.childFragmentManager?.fragments?.last()
        if (fragmentInstance is StartFragment){
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                Snackbar.make(binding.root, "Press back again to exit", Snackbar.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
            return
        }
        if (fragmentInstance is Question1Fragment){
            initAlertDialogExit()
            return
        }
        if (fragmentInstance is ResultFragment){
            initAlertDialogExit()
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun initAlertDialogExit() {
        AlertDialog.Builder(this)
            .setTitle("Закінчити тест")
            .setMessage("Ви точно хочете покинути тест, дані поточного тесту не будуть збережені?")
            .setPositiveButton("Вийти") { _, _ ->
                mainViewModel.restartTest()
                super.onBackPressed()
            }
            .setNegativeButton("Залишитись") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }
}