package com.metalichesky.zvuk.generator.view.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.metalichesky.zvuk.generator.listitem.BaseItem
import com.metalichesky.zvuk.generator.listitem.WaveformTypeItem
import com.metalichesky.zvuk.generator.databinding.ItemWaveformTypeBinding
import com.metalichesky.zvuk.generator.view.getIconResId
import com.metalichesky.zvuk.generator.view.getNameResId
import com.metalichesky.zvuk.generator.util.getLayoutInflater

class WaveformAdapter(
    listener: AdapterListener? = null
) : BaseAdapter<WaveformTypeItem>(
    listener,
    object : BaseViewHolder.Factory {
        override fun create(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return WaveformViewHolder(
                ItemWaveformTypeBinding.inflate(parent.context.getLayoutInflater(), parent, false),
                listener
            )
        }
    }
)

private class WaveformViewHolder(
    private val binding: ItemWaveformTypeBinding,
    private val listener: BaseAdapter.AdapterListener?
) : BaseAdapter.BaseViewHolder(binding.root) {

    override fun bind(item: BaseItem, position: Int) {
        (item as? WaveformTypeItem)?.let { bind(it, position) }
    }

    private fun bind(item: WaveformTypeItem, position: Int) {
        binding.apply {
            val context = binding.root.context
            ivWaveform.setImageDrawable(ContextCompat.getDrawable(context, item.waveformType.getIconResId()))
            tvWaveform.text = context.getText(item.waveformType.getNameResId())
            cbxWaveform.isChecked = item.isSelected
            cbxWaveform.isClickable = false
            flWaveform.setOnClickListener {
                listener?.onClicked(item, position)
            }
        }
    }
}