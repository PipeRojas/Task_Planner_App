package com.example.taskplannerapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.taskplannerapp.R
import com.example.taskplannerapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    //Declare ViewModel
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Instantiate ViewModel
        viewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]
        binding.login.setOnClickListener {
            onLoginButtonClicked(it)
        }
    }

    private fun onLoginButtonClicked(view: View) {
        view.isEnabled = false
        if (binding.username.text.isEmpty() || binding.username.text.isBlank()) {
            binding.username.error = getString(R.string.field_cannot_be_empty)
            view.isEnabled = true
        } else if (binding.password.text.isEmpty() || binding.password.text.isBlank()){
            binding.password.error = getString(R.string.field_cannot_be_empty)
        view.isEnabled = true
        }else{
            val loginJob = viewModel.authenticate(binding.username.text.toString().trim(), binding.password.text.toString().trim())
            loginJob.invokeOnCompletion {
                GlobalScope.launch(Main) {
                    if(viewModel.isAuthenticated()){
                        binding.login.text="Exito"
                    }else{
                        binding.username.error = getString(R.string.auth_error)
                        binding.password.error = getString(R.string.auth_error)
                        view.isEnabled = true
                    }
                }
            }
        }
    }
}