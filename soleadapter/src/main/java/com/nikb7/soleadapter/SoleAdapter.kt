package com.nikb7.soleadapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class SoleAdapter(
    protected val viewMap: MutableMap<Class<out StableId>, Int>,
    private val listener: OnRecyclerItemClickListener? = null
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var listEndObj: StableId? = null
    private var emptyListObj: StableId? = null

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
        items.isEmpty() && emptyListObj != null -> listOf(emptyListObj!!)
        listEndObj != null -> items.plus(listEndObj!!)
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

    fun addItem(item: StableId, pos: Int = itemCount) {
        if (pos >= 0) {
            if (items.isEmpty() || (emptyListObj != null && items[0] == emptyListObj)) {
                submitList(listOf(item))
            } else {
                items.add(pos, item)
                notifyItemInserted(pos)
            }
        }
    }

    fun addItems(items: List<StableId>) {
        if (items.isNotEmpty()) {
            val initSize = itemCount
            if (this.items.isEmpty() || (emptyListObj != null && items[0] == emptyListObj)) {
                submitList(items)
            } else {
                this.items.addAll(items)
                notifyItemRangeInserted(initSize, items.size)
            }
        }
    }

    fun getListItems() = items

    fun setEmptyListView(obj: StableId, @LayoutRes layoutId: Int) = apply {
        emptyListObj = obj
        viewMap[obj.javaClass] = layoutId
    }

    fun setListEndView(obj: StableId, @LayoutRes layoutId: Int) = apply {
        listEndObj = obj
        viewMap[obj.javaClass] = layoutId
    }


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