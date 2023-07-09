package one.njk.celestidesk.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.njk.celestidesk.databinding.FragmentLoginBinding
import one.njk.celestidesk.viewmodels.AuthViewModel

class LoginFragment: Fragment() {
    lateinit var binding: FragmentLoginBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.apply {
            submit.setOnClickListener {
                lifecycleScope.launch {
                    loading.visibility = View.VISIBLE
                    submit.visibility = View.GONE
                    delay(2000)
                    loading.visibility = View.INVISIBLE
                    submit.visibility = View.VISIBLE
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRequestFragment())
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
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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