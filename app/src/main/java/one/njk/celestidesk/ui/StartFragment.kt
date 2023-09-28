package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import one.njk.celestidesk.databinding.FragmentStartBinding

class StartFragment: Fragment() {

    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginFragment())
            }
            signupBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartFragmentToSignupFragment())
            }
        }

    }

}