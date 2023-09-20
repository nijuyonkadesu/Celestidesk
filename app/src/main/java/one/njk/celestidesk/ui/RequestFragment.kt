package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
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
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */


@AndroidEntryPoint
class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    @Inject
    lateinit var rolesDataStore: RolesDataStore
    private lateinit var currentRole: Role
    private lateinit var viewModel: RoleAgreement

    @DrawableRes private var fabIcon = R.drawable.search
    @StringRes private var fabHint = R.string.search_fab

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            currentRole = rolesDataStore.getRole()
            if(currentRole == Role.EMPLOYEE) {
                fabIcon = R.drawable.add
                fabHint = R.string.create_fab
            }
            viewModel = chooseLogic(currentRole)
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

        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(calendarConstraints.build())
                .build()

        val requestSheet = NewRequestSheet {
            dateRangePicker.show(childFragmentManager, "DATE_PICK")
        }

        lifecycleScope.launch {
            _binding!!.role.text = viewModel.name
            _binding!!.fab.setIconResource(fabIcon)
            _binding!!.fab.text = getString(fabHint)
        }
        addFilterChips(binding.filter, viewModel.stages, lifecycleScope)
        val adapter = RequestListAdapter {
            lifecycleScope.launch {
                if(currentRole != Role.EMPLOYEE)
                    showConfirmationDialog(it)
            }
        }
        binding.requestList.adapter = adapter

        viewModel.requestsFlow.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if(it.isLoading) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.INVISIBLE
            }
        }

        binding.fab.setOnClickListener {
            if(currentRole != Role.EMPLOYEE)
                findNavController()
                    .navigate(RequestFragmentDirections.actionRequestFragmentToSearchFragment())
            else {
                requestSheet.show(childFragmentManager, NewRequestSheet.TAG)
                dateRangePicker.addOnDismissListener {
                    requestSheet.updateDateRange(dateRangePicker.selection)
                }

                /*
                * TODO: add remaining request count near button
                * TODO: disable clicking emergency request unless normal req exhausts
                * */
            }
        }
        binding.refresh.apply {
            setOnRefreshListener {
                viewModel.refreshRequests()
                isRefreshing = false
            }
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