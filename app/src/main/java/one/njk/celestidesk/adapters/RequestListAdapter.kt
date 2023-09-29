package one.njk.celestidesk.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import one.njk.celestidesk.databinding.RequestItemBinding
import one.njk.celestidesk.domain.BreakRequest

class RequestListAdapter(
    private val exposeRequest: (BreakRequest) -> Unit,
): ListAdapter<BreakRequest, RequestListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding: RequestItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(req: BreakRequest){
            binding.apply {
                reasonSubject.text = req.subject
                reason.text = req.message
                name.text = req.name
                time.text = req.dateShort
                val lifeline = req.getProgress()

                elapsedDays.progress = lifeline
                fineOrStrike(reasonSubject, lifeline)
                fineOrStrike(reason, lifeline)
                fineOrStrike(name, lifeline)
            }
        }

        private fun fineOrStrike(textview: TextView, lifeLine: Int) {
            textview.paintFlags = when(lifeLine){
                0 -> textview.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else -> textview.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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
