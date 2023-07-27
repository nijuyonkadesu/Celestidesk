package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import one.njk.celestidesk.databinding.FragmentSignupBinding

@AndroidEntryPoint
class SignupFragment: Fragment() {

    lateinit var binding: FragmentSignupBinding

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
                // TODO: Attach a view model, and if success, then only navigate
                findNavController().navigate(
                    SignupFragmentDirections.actionSignupFragmentToRequestFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}