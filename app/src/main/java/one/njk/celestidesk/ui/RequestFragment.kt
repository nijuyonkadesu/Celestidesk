package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import one.njk.celestidesk.R
import one.njk.celestidesk.adapters.RequestListAdapter
import one.njk.celestidesk.database.Role
import one.njk.celestidesk.database.RolesDataStore
import one.njk.celestidesk.databinding.FragmentRequestBinding
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.Decision
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.toStage
import one.njk.celestidesk.viewmodels.EmployeeViewModel
import one.njk.celestidesk.viewmodels.ManagerViewModel
import one.njk.celestidesk.viewmodels.RoleAgreement
import one.njk.celestidesk.viewmodels.TeamLeadViewModel
import one.njk.celestidesk.viewmodels.stages
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */


@AndroidEntryPoint
class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    @Inject
    lateinit var rolesDataStore: RolesDataStore
    private lateinit var viewModel: RoleAgreement

    @DrawableRes private var iconId = R.drawable.search

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val role = rolesDataStore.getRole()
            if(role == Role.EMPLOYEE) iconId = R.drawable.add
            viewModel = chooseLogic(role)
            viewModel.refreshRequests()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            _binding!!.role.text = viewModel.name
            _binding!!.fab.setImageResource(iconId)
        }
        addFilterChips(binding.filter, stages, lifecycleScope)
        val adapter = RequestListAdapter {
            lifecycleScope.launch {
                if(rolesDataStore.getRole() != Role.EMPLOYEE)
                    showConfirmationDialog(it)
            }
        }
        binding.requestList.adapter = adapter

        viewModel.requestsFlow.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            findNavController()
                .navigate(RequestFragmentDirections.actionRequestFragmentToSearchFragment())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseLogic(role: Role): RoleAgreement {
        return when(role){
            Role.EMPLOYEE -> {
                val employeeViewModel: EmployeeViewModel by activityViewModels()
                employeeViewModel
            }
            Role.TEAM_LEAD -> {
                val teamLeadViewModel: TeamLeadViewModel by activityViewModels()
                teamLeadViewModel
            }
            Role.MANAGER -> {
                val managerLeadViewModel: ManagerViewModel by activityViewModels()
                managerLeadViewModel
            }
        }
    }

    private fun addFilterChips(
        chipGroup: ChipGroup,
        categories: List<String>,
        lifeCycleScope: CoroutineScope,
    ){
        // Define ChipGroup Behaviours
        chipGroup.apply {
            removeAllViews()
            isSelectionRequired = true
            isSingleSelection = true
        }

        // Generate Chip based on chosen Category (SFW/NSFW)
        for(category in categories){
            val chip = Chip(this.context)
            chip.apply {
                text = category
                isCheckable = true
                setOnClickListener {
                    viewModel.updateStage(category.toStage())
                }
            }

            chipGroup.addView(chip)
            lifeCycleScope.launch {
                // Check 1st Chip by default
                chipGroup.getChildAt(0).performClick()
            }
        }
    }

    private fun showConfirmationDialog(breakRequest: BreakRequest) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.approve_or_not))
            .setNegativeButton(getString(R.string.button_deny)) { _, _ ->
                val decision = DecisionRequest(breakRequest.id, Decision.REJECTED)
                 viewModel.decide(decision, breakRequest)
            }
            .setPositiveButton(getString(R.string.button_approve)) { _, _ ->
                val decision = DecisionRequest(breakRequest.id, Decision.ACCEPTED)
                viewModel.decide(decision, breakRequest)
            }
            .show()
    }
}
// TODO: Profile Pic for users
// TODO: >5 Request, it goes to Manager directly
// TODO: 20min expiry after reaching manager