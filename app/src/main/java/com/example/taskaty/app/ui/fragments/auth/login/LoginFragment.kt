package com.example.taskaty.app.ui.fragments.auth.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.auth.signup.SignupFragment
import com.example.taskaty.databinding.FragmentLoginBinding
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.home.HomeFragment
import com.example.taskaty.data.repositories.AuthRepositoryImpl
import com.example.taskaty.domain.interactors.AuthInteractor


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    LoginView {
    private lateinit var presenter: LoginPresenter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
        presenter = LoginPresenter(
            AuthInteractor(
               AuthRepositoryImpl.getInstance()
            ), this
        )
        setup()
    }

    private fun setup() {
        presenter.onLoginWithSaveToken()
        with(binding) {
            buttonLogin.setOnClickListener {
                presenter.onLogin(
                    editTextUsername.text.toString(), editTextPassword.text.toString()
                )
            }
            textGoSignup.setOnClickListener {
                navigateToSignupScreen()
            }
            imageShowPassword.setOnClickListener {
                if(editTextPassword.transformationMethod == null) {
                    imageShowPassword.setImageResource(R.drawable.baseline_visibility_off_24)
                    editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    imageShowPassword.setImageResource(R.drawable.baseline_visibility_24)
                    editTextPassword.transformationMethod = null
                }
            }
        }
    }

    override fun showLoading() {
        activity?.runOnUiThread {
            setVisibility(true)
            binding.buttonLogin.text = ""
        }
    }

    override fun hideLoading() {
        activity?.runOnUiThread {
            setVisibility(false)
            binding.buttonLogin.text = getString(R.string.text_button_login)
        }
    }

    override fun showErrorMessage(message: String) {
        showToast(message)

    }

    override fun navigateToHomeScreen() {
       startFragmentNavigation()
    }

    override fun showValidationError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSignupScreen() {
        val signupFragment = SignupFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, signupFragment)
        transaction.commit()
    }

    private fun setVisibility(isVisible: Boolean) {
        with(binding) {
            progressBar.isVisible = isVisible
            buttonLogin.isEnabled = !isVisible
        }
    }

    private fun startFragmentNavigation(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, HomeFragment())
        transaction.commit()
    }


}