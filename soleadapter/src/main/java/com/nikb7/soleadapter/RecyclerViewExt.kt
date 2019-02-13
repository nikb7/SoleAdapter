package com.nikb7.soleadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addHorizontalDivider(@DrawableRes drawableInt: Int) {
    val itemDecor = androidx.recyclerview.widget.DividerItemDecoration(
        context,
        androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
    )
    itemDecor.setDrawable(ContextCompat.getDrawable(context, drawableInt)!!)
    addItemDecoration(itemDecor)
}

fun RecyclerView.autoGridLayoutManager(spanCount: Int = 2, viewsWithFullWidth: List<Int>? = null) {
    val gridLayoutManager = GridLayoutManager(context, spanCount)
    if (viewsWithFullWidth != null)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (viewsWithFullWidth.contains(adapter?.getItemViewType(position))) spanCount else 1
            }
        }
    this.layoutManager = gridLayoutManager
}


fun <T : ViewDataBinding?> ViewGroup?.bindView(layoutId: Int, inflator: LayoutInflater = LayoutInflater.from(this!!.context), attachToRoot: Boolean = false) =
    DataBindingUtil.inflate<T>(inflator, layoutId, this, attachToRoot)!!
