package com.metalichesky.zvuk.generator.view.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.metalichesky.zvuk.generator.listitem.BaseItem

abstract class BaseAdapter <T: BaseItem>(
    var listener: AdapterListener? = null,
    private val viewHolderFactory: BaseViewHolder.Factory
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    var items: List<T> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return viewHolderFactory.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val realPosition = holder.absoluteAdapterPosition
        if (realPosition != RecyclerView.NO_POSITION) {
            items.getOrNull(realPosition)?.let {
                holder.bind(it, realPosition)
            }
        }
    }

    interface AdapterListener {

        fun onClicked(item: BaseItem, position: Int)

    }

    abstract class BaseViewHolder(view: View): ViewHolder(view) {

        abstract fun bind(item: BaseItem, position: Int)

        interface Factory {

            fun create(parent: ViewGroup, viewType: Int): BaseViewHolder

        }

    }

}