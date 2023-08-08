package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import one.njk.celestidesk.adapters.SearchResultAdapter
import one.njk.celestidesk.databinding.FragmentSearchBinding
import one.njk.celestidesk.viewmodels.SearchViewModel

@AndroidEntryPoint
class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchResultAdapter()
        binding.apply {
            results.adapter = adapter
            searchView.editText.setOnEditorActionListener { textView, _, _ ->
                searchBar.text = textView.text
                searchView.hide()
                // TODO: Call Search Function
                false
            }
        }
        viewModel.transactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
