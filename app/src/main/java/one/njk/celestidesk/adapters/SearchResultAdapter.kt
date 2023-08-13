package one.njk.celestidesk.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import one.njk.celestidesk.databinding.SearchItemBinding
import one.njk.celestidesk.domain.History
import one.njk.celestidesk.network.ActionResult

class SearchResultAdapter
    : ListAdapter<History, SearchResultAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding: SearchItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: History){
                    binding.apply {
                        from.text = item.fromShort
                        to.text = item.dateRange

                        origin.text = item.origin
                        subject.text = item.subject
                        message.text = item.message
                        action.text = item.action
                        responder.text = "By: ${item.responder}"

                        if (item.nowIn == ActionResult.REJECTED) action.setTextColor(Color.rgb(244, 67, 54))
                        else if (item.nowIn == ActionResult.EXPIRED) action.setTextColor(Color.rgb(255, 152, 0))
                    }
                }
            }

    companion object DiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return false
        }
        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}