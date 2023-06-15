package com.bbasoglu.uimodule

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bbasoglu.uimodule.extensions.dp2px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    /**
     * @param remoteUrlStr url of image to load
     * @param useShimmerEffect if use shimmer effect as placeholder (only for loading)
     * @param placeHolderResource resource id of placeholder for error(if useShimmerEffect is false
     * then it will be used for loading also)
     * @param cornerRadius radius as dp for corners
     * @param scaleType scaleType of image
     * */
    fun setUrlImage(
        remoteUrlStr: String,
        useShimmerEffect: Boolean = true,
        placeHolderResource: Int=R.color.transparent,
        cornerRadius: Int = 15,
        scaleType: ScaleType = ScaleType.CenterCrop
    ) {
        if (TextUtils.isEmpty(remoteUrlStr)) {
            if (placeHolderResource!=0){
                setImageResource(placeHolderResource)
            }
        } else {
            Glide.with(context)
                .load(remoteUrlStr)
                .apply(createGlideRequestOptions(useShimmerEffect, placeHolderResource, cornerRadius, scaleType))
                .into(this)
        }
    }

    fun setImageResource(
        remoteUrlStr: Int,
        useShimmerEffect: Boolean = true,
        placeHolderResource: Int=R.color.transparent,
        cornerRadius: Int = 15,
        scaleType: ScaleType = ScaleType.CenterCrop
    ) {
        Glide.with(context)
            .load(remoteUrlStr)
            .apply(createGlideRequestOptions(useShimmerEffect, placeHolderResource, cornerRadius, scaleType))
            .into(this)
    }

    /**
     * Change image scaleType if you need others
     */
    private fun createGlideRequestOptions(useShimmerEffect: Boolean, placeHolderResource: Int, cornerRadius: Int, scaleType: ScaleType): RequestOptions {
        val shimmerDrawable: ShimmerDrawable? = createShimmerEffectPlaceHolder(useShimmerEffect)
        val requestOptions: RequestOptions = if (placeHolderResource == -1){
            RequestOptions()
        }else{
            RequestOptions()
                .placeholder(
                    if (useShimmerEffect) {
                        shimmerDrawable
                    } else {
                        ContextCompat.getDrawable(context, placeHolderResource)
                    }
                )
                .error(placeHolderResource)
        }


        val bitmapScaleType = when (scaleType) {
            ScaleType.CenterCrop -> CenterCrop()
            ScaleType.CenterInside -> CenterInside()
            ScaleType.None -> null
        }

        return when {
            scaleType == ScaleType.None -> {
                requestOptions
            }
            cornerRadius != 0 -> {
                requestOptions.transform(bitmapScaleType, RoundedCorners(cornerRadius.dp2px))
            }
            else -> {
                requestOptions.transform(bitmapScaleType)
            }
        }
    }

    private fun createShimmerEffectPlaceHolder(useShimmerEffect: Boolean): ShimmerDrawable? {
        return if (useShimmerEffect) {
            ShimmerDrawable().apply {
                setShimmer(
                    Shimmer.AlphaHighlightBuilder() // The attributes for a ShimmerDrawable is set by this builder
                        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                        .setBaseAlpha(0.7f) // the alpha of the underlying children
                        .setHighlightAlpha(0.6f) // the shimmer alpha amount
                        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                        .setAutoStart(true)
                        .build()
                )
            }
        } else {
            null
        }
    }

    /**
     * If you figure out that Glide is using too many resources for large data sets
     * in recyclerviews then call this method from following method of recyclerview
     *  @Override
     *  public void onViewRecycled(RecyclerView.ViewHolder holder)
     *  You will reach this imageview from provided holder
     * */
    fun clear() {
        Glide.with(context).clear(this)
    }

    /**
     * If glide does not cut rounded corners try CenterCrop (In my research I found
     * that it works with CenterCrop but there were also developers who says it works with
     * CenterInside but be aware of ScaleType.
     * Attention!!! If you give None I disable rounded corners transform
     * you can follow from method createGlideRequestOptions related cases)
     * */
    enum class ScaleType {
        CenterCrop,
        CenterInside,
        None
    }
}
