package one.njk.celestidesk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.njk.celestidesk.databinding.SearchItemBinding
import one.njk.celestidesk.domain.History

class SearchResultAdapter(private val dataset: List<History>)
    : RecyclerView.Adapter<SearchResultAdapter.ItemViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

}