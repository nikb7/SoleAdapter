package com.nikb7.soleadapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

open class SoleAdapter(
    private val viewMap: Map<Class<out StableId>, Int>,
    private val listener: OnRecyclerItemClickListener? = null,
    private val listEndView: Pair<StableId, Int>? = null,
    private val emptyListView: Pair<StableId, Int>? = null
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    companion object {
        private const val TAG = "SoleAdapter"
    }

    init {
        viewMap.apply {
            listEndView?.let {
                plus(it.first::class.java to it.second)
            }
            emptyListView?.let {
                plus(it.first::class.java to it.second)
            }
        }
    }

    protected var items = mutableListOf<StableId>()

    override fun getItemViewType(position: Int) = getAssignedViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(parent.bindView(viewType))

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.bind(items[position], listener)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount() = items.size

    private fun listItemHandler(items: List<StableId>) = when {
        items.isEmpty() && emptyListView != null -> listOf(emptyListView.first)
        listEndView != null -> items.plus(listEndView.first)
        else -> items
    }

    protected fun getAssignedViewType(position: Int) = viewMap[items[position].javaClass]
        ?: throw Exception("View not found for ${items[position]::class}")


    fun submitList(items: List<StableId>) {
        val mItems = listItemHandler(items)
        val diffCallback = DiffCallback(this.items, mItems)
        val res = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(mItems)
        res.dispatchUpdatesTo(this)
    }


    fun clearAllItems() {
        val initSize = itemCount
        items.clear()
        notifyItemRangeRemoved(0, initSize)
    }

    fun clearAndAddItems(items: List<StableId>) {
        this.items.clear()
        this.items.addAll(listItemHandler(items))
        notifyDataSetChanged()
    }


    fun getListItems() = items


}

private class DiffCallback(private val oldList: List<StableId>, private val newList: List<StableId>) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].getStableId() == newList[newItemPosition].getStableId()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]


    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) =
        super.getChangePayload(oldItemPosition, newItemPosition)

}