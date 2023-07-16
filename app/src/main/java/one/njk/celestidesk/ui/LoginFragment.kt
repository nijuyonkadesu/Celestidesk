package one.njk.celestidesk.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.databinding.FragmentLoginBinding
import one.njk.celestidesk.viewmodels.AuthViewModel

@AndroidEntryPoint
class LoginFragment: Fragment() {
    lateinit var binding: FragmentLoginBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            submit.setOnClickListener {
                lifecycleScope.launch {
                    val inputUsername = username.editText?.text.toString()
                    val inputPassword = password.editText?.text.toString()
                    if(inputPassword.isNotEmpty() && inputUsername.isNotEmpty()){
                        viewModel.logIn(inputUsername, inputPassword)
                    }
                }
            }
            viewModel.state.observe(viewLifecycleOwner){
                if(it.isLoading) {
                    loading.visibility = View.VISIBLE
                    submit.visibility = View.GONE
                } else {
                    loading.visibility = View.INVISIBLE
                    submit.visibility = View.VISIBLE
                }
                if(it.authResult is AuthResult.Authorized) {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToRequestFragment())
                } else {
                    Toast.makeText(context, "verify your username / password", Toast.LENGTH_SHORT).show()
                }
            }
            submit.setOnEditorActionListener { view, event, _ ->
                if(event == EditorInfo.IME_ACTION_NEXT){
                    view.requestFocus()
                    true
                } else {
                    handleKeyEvent(view, event)
                    true
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    viewModel.logIn("radextrem", "123456")
//                    delay(7000)
                    viewModel.authenticate()
                    // Wait for network inspector to launch
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}