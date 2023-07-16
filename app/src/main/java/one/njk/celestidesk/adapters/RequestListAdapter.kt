package one.njk.celestidesk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.databinding.RequestItemBinding

class RequestListAdapter(
    private val exposeRequest: (BreakRequest) -> Unit,
): ListAdapter<BreakRequest, RequestListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding: RequestItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(req: BreakRequest){
            binding.apply {
                reasonSubject.text = req.subject
                reason.text = req.message
                time.text = req.dateShort
            }
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<BreakRequest>() {
        override fun areItemsTheSame(oldItem: BreakRequest, newItem: BreakRequest): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: BreakRequest, newItem: BreakRequest): Boolean {
            return oldItem.message == newItem.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = RequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { exposeRequest(item) }
        holder.bind(item)
    }
}
