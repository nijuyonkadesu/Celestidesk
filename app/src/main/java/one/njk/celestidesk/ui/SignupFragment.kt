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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import one.njk.celestidesk.database.Role
import one.njk.celestidesk.databinding.FragmentSignupBinding
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import one.njk.celestidesk.viewmodels.AuthViewModel

@AndroidEntryPoint
class SignupFragment: Fragment() {

    lateinit var binding: FragmentSignupBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            signupBtn.setOnClickListener {
                lifecycleScope.launch {
                    val inputName = name.editText?.text.toString()
                    val inputMail = email.editText?.text.toString()
                    val inputUsername = usernameSignup.editText?.text.toString()
                    val inputPassword = passwordSignup.editText?.text.toString()
                    val inputRoomNo = roomNo.editText?.text.toString()
                    val inputParentNumber = parentNumber.editText?.text.toString()

                    val raw = AuthSignUpRequest(
                        inputName,
                        inputUsername,
                        "svcecollege",
                        Role.EMPLOYEE,
                        inputPassword
                    )
                    // TODO: Ask for role in form

                    if(!viewModel.validateAndSignUp(raw))
                        Toast.makeText(context, "verify entered details!", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.state.observe(viewLifecycleOwner) {
                if(it.isLoading){
                    signupBtn.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                } else {
                    loading.visibility = View.GONE
                    signupBtn.visibility = View.VISIBLE
                }

                if(it.authResult is AuthResult.Authorized){
                    findNavController().navigate(
                        SignupFragmentDirections.actionSignupFragmentToRequestFragment())
                } else {
                    Toast.makeText(context, "Are you from our institution lol? check entered details", Toast.LENGTH_SHORT).show()
                }
            }

            signupBtn.setOnEditorActionListener { view, event, _ ->
                if(event == EditorInfo.IME_ACTION_NEXT){
                    view.requestFocus()
                    true
                } else {
                    handleKeyEvent(view, event)
                    true
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