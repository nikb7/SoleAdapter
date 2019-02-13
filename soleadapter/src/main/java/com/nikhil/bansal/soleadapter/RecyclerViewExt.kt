package com.nikhil.bansal.soleadapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

fun RecyclerView.addHorizontalDivider(@DrawableRes drawableInt: Int) {
    val itemDecor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
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
