package com.metalichesky.zvuk.generator.view.adapter

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.metalichesky.zvuk.generator.listitem.BaseItem
import com.metalichesky.zvuk.generator.listitem.NoiseTypeItem
import com.metalichesky.zvuk.generator.databinding.ItemNoiseTypeBinding
import com.metalichesky.zvuk.generator.view.getColorResId
import com.metalichesky.zvuk.generator.view.getNameResId
import com.metalichesky.zvuk.generator.util.getLayoutInflater

class NoiseAdapter(
    listener: AdapterListener? = null,
) : BaseAdapter<NoiseTypeItem>(
    listener,
    object : BaseViewHolder.Factory {
        override fun create(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return NoiseTypeViewHolder(
                ItemNoiseTypeBinding.inflate(parent.context.getLayoutInflater(), parent, false),
                listener
            )
        }
    }
)


private class NoiseTypeViewHolder(
    private val binding: ItemNoiseTypeBinding,
    private val listener: BaseAdapter.AdapterListener?
) : BaseAdapter.BaseViewHolder(binding.root) {

    override fun bind(item: BaseItem, position: Int) {
        (item as? NoiseTypeItem)?.let { bind(it, position) }
    }

    private fun bind(item: NoiseTypeItem, position: Int) {
        binding.apply {
            val context = root.context
            ivNoise.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, item.noiseType.getColorResId()))
            tvNoise.text = context.getText(item.noiseType.getNameResId())
            cbxWaveform.isChecked = item.isSelected
            cbxWaveform.isClickable = false
            flNoise.setOnClickListener {
                listener?.onClicked(item, position)
            }
        }
    }
}

