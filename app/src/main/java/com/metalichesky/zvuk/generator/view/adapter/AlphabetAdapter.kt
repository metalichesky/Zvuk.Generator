package com.metalichesky.zvuk.generator.view.adapter

import android.view.ViewGroup
import com.metalichesky.zvuk.generator.listitem.AlphabetItem
import com.metalichesky.zvuk.generator.listitem.BaseItem
import com.metalichesky.zvuk.generator.databinding.ItemAlphabetBinding
import com.metalichesky.zvuk.generator.util.getLayoutInflater

class AlphabetAdapter(
    listener: AdapterListener? = null
) : BaseAdapter<AlphabetItem>(
    listener,
    object : BaseViewHolder.Factory {
        override fun create(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return AlphabetViewHolder(
                ItemAlphabetBinding.inflate(parent.context.getLayoutInflater(), parent, false),
                listener
            )
        }
    }
)

private class AlphabetViewHolder(
    private val binding: ItemAlphabetBinding,
    private val listener: BaseAdapter.AdapterListener?
) : BaseAdapter.BaseViewHolder(binding.root) {

    override fun bind(item: BaseItem, position: Int) {
        (item as? AlphabetItem)?.let { bind(it, position) }
    }

    private fun bind(item: AlphabetItem, position: Int) {
        binding.apply {
            tvAlphabet.text = item.alphabet.type.name.substring(0, 1).uppercase()
            tvAlphabetName.text = item.alphabet.type.name
            cbxAlphabet.isChecked = item.isSelected
            cbxAlphabet.isClickable = false
            flAlphabet.setOnClickListener {
                listener?.onClicked(item, position)
            }
        }
    }

}