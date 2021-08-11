package com.example.tests.bitmap

import android.graphics.Bitmap
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.yu.hu.libcommon.adapter.BtnAdapter
import com.yu.hu.libcommon.util.BitmapUtil
import com.yu.hu.libcommon.util.ScreenUtil

class BitmapAdapter : BtnAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        if (viewType == TYPE_IMG) {
            val img = ImageView(parent.context)
            val width = ScreenUtil.dp2px(parent.context, DEFAULT_IMG_WIDTH)
            val height = ScreenUtil.dp2px(parent.context, DEFAULT_IMG_HEIGHT)
            val layoutParam = LinearLayout.LayoutParams(width, height)
            layoutParam.gravity = Gravity.CENTER_HORIZONTAL
            img.layoutParams = layoutParam
            return ImgItemHolder(img)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is ImgItem) return TYPE_IMG
        return super.getItemViewType(position)
    }

    data class ImgItem(
        @DrawableRes val idRes: Int,
        var showType: Int = 0
    ) : IBaseItem

    class ImgItemHolder(private val img: ImageView) : BaseHolder(img) {
        override fun bindItem(baseItem: IBaseItem) {
            val imgConfig: ImgItem = baseItem as ImgItem
            val width = ScreenUtil.dp2px(img.context, DEFAULT_IMG_WIDTH)
            val height = ScreenUtil.dp2px(img.context, DEFAULT_IMG_HEIGHT)
            val bitmap: Bitmap? = when (imgConfig.showType) {
                1 -> BitmapUtil.decodeResource(img.context, imgConfig.idRes, width, height)
                2 -> {
                    val temp =
                        BitmapUtil.decodeResource(img.context, imgConfig.idRes, width, height)
                    val res = BitmapUtil.transform2Gray(temp!!)
                    temp.recycle()
                    res
                }
                3 -> BitmapUtil.decodeResource(
                    img.context,
                    imgConfig.idRes,
                    width,
                    height,
                    ScreenUtil.dp2px(img.context, 20)
                )
                else -> null
            }
            img.setImageBitmap(bitmap)
        }

    }

    companion object {
        const val TYPE_IMG = 1

        const val DEFAULT_IMG_WIDTH = 250
        const val DEFAULT_IMG_HEIGHT = 150
    }

}