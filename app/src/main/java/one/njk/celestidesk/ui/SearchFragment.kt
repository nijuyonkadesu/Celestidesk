package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.adapters.SearchResultAdapter
import one.njk.celestidesk.databinding.FragmentSearchBinding
import one.njk.celestidesk.domain.History
import one.njk.celestidesk.network.ActionResult
import one.njk.celestidesk.network.Stage

@AndroidEntryPoint
class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
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

        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
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
        adapter.submitList(
            listOf(
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
                History(
                    origin = "Customer Support",
                    subject = "New Support Ticket",
                    message = "I need help with my account.",
                    from = currentTime,
                    to = currentTime,
                    wasIn = Stage.IN_REVIEW,
                    nowIn = ActionResult.ACCEPTED,
                    responder = "John Doe",
                ),
            )
        )
    }
}
