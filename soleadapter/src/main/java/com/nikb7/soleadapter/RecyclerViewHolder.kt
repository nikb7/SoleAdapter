package com.nikb7.soleadapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nikhil.bansal.soleadapter.BR

open class RecyclerViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    /**NOTE use only obj, lis as variable names inside xml files
     * obj: it is the class linked to the view
     * lis: it is the listener added to the view*/
    open fun bind(obj: StableId, listenerRecycler: OnRecyclerItemClickListener?) {
        binding.setVariable(BR.obj, obj)
        binding.setVariable(BR.lis, listenerRecycler)
        binding.executePendingBindings()
        itemView.setOnClickListener { listenerRecycler?.onRecyclerItemClick(obj) }
    }
}


//Extend data classes with StableId
interface StableId {
    /** Used to check if the item is changed or not in the list. It should be unique for every cell in list. It is used by DiffCallback*/
    fun getStableId(): String
}

interface OnRecyclerItemClickListener {
    fun onRecyclerItemClick(obj: StableId)
    fun onRecyclerInnerItemClick(view: View, obj: StableId)
}
