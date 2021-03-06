package com.saad.base.ui.auth.onboarding.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.base.R
import com.saad.base.databinding.RowIndecatorBinding
import com.saad.base.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.saad.base.ui.base.adapters.BaseViewHolder

class IndecatorRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Boolean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowIndecatorBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowIndecatorBinding) :
        BaseViewHolder<Boolean>(binding.root) {

        override fun bind(item: Boolean) {
            if (item) {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.white))
            } else {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.shimmer_background))
            }
        }
    }
}