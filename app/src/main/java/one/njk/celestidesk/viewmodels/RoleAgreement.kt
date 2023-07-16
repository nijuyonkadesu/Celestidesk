package one.njk.celestidesk.viewmodels

import androidx.lifecycle.LiveData
import one.njk.celestidesk.domain.BreakRequest

interface RoleAgreement {
    val name: String
    val requestsFlow: LiveData<List<BreakRequest>>
    fun refreshRequests()
}