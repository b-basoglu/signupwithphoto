package com.bbasoglu.signup.ui.signup.adapter.row

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bbasoglu.signup.R
import com.bbasoglu.signup.databinding.RowSignupHeaderBinding
import com.bbasoglu.signup.ui.signup.adapter.CustomAdapterEvent
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpHeaderData
import com.bbasoglu.uimodule.adapter.BaseAdapter
import com.bbasoglu.uimodule.adapter.BaseAdapterRow
import com.bbasoglu.uimodule.adapter.BaseViewHolder
import com.bbasoglu.uimodule.extensions.dp2px
import com.bbasoglu.uimodule.extensions.gone
import com.bbasoglu.uimodule.extensions.invisible
import com.bbasoglu.uimodule.extensions.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class SignUpHeaderRow :
    BaseAdapterRow<SignUpHeaderRow.ViewHolder, SignUpHeaderData>() {

    override fun getViewType() = R.layout.row_signup_header

    override fun getDataType() = SignUpHeaderData::class

    override fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: BaseAdapter,
        adapterClick: ((Any) -> Unit)?
    ): ViewHolder {
        val binding = RowSignupHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, adapterClick)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        data: SignUpHeaderData,
        position: Int
    ) {
        holder.bind(data, position)
    }

    inner class ViewHolder(
        binding: RowSignupHeaderBinding,
        val adapterClick: ((Any) -> Unit)?
    ) :
        BaseViewHolder<SignUpHeaderData, RowSignupHeaderBinding>(
            binding
        ) {
        override fun bindHolder(data: SignUpHeaderData, position: Int) {
            binding.header.text = data.title
            binding.subtitle.text = data.subTitle
            binding.addAvatar.text = data.avatarText
            data.path?.let {
                binding.addAvatar.gone()
                Glide.with(binding.root.context)
                    .asBitmap()
                    .load(data.path)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Bitmap>?,
                            p3: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            p0: Bitmap?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Bitmap>?,
                            p3: DataSource?,
                            p4: Boolean
                        ): Boolean {

                            return false
                        }
                    })
                    .placeholder(R.drawable.bg_empty_image)
                    .error(R.drawable.bg_empty_image)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(12.dp2px)
                    )
                    .into(binding.image)
            }?:let {
                binding.addAvatar.visible()
            }

            binding.image.setOnClickListener {
                adapterClick?.invoke(data)
            }
            binding.root.setOnClickListener {
                adapterClick?.invoke(CustomAdapterEvent.HIDE_KEYBOARD)
            }
        }
    }
}