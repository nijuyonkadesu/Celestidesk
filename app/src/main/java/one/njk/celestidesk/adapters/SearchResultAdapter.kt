package one.njk.celestidesk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import one.njk.celestidesk.databinding.SearchItemBinding
import one.njk.celestidesk.domain.History

class SearchResultAdapter
    : ListAdapter<History, SearchResultAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding: SearchItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: History){
                    binding.apply {
                        reasonSubject.text = item.subject
                        reason.text = item.message
                        time.text = item.fromShort
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