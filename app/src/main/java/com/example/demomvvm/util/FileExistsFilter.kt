package com.turnzero.utils

import android.content.Context
import com.dattingtimo.meetlandia.R
import com.dattingtimo.meetlandia.utils.FileUtil

import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item
import java.io.File
import java.util.*

internal class FileExistsFilter : Filter() {

    public override fun constraintTypes(): Set<MimeType> {
        return EnumSet.allOf(MimeType::class.java)
        /*return object : HashSet<MimeType>() {
            init {
                add(MimeType.JPEG)
                add(MimeType.PNG)
                add(MimeType.BMP)
                add(MimeType.WEBM)
                add(MimeType.WEBP)
                add(MimeType.MKV)
                add(MimeType.MP4)
                add(MimeType.MPEG)
                add(MimeType.AVI)
            }
        }*/
    }

    override fun filter(context: Context, item: Item): IncapableCause? {
        if (!needFiltering(context, item))
            return null

        if (!File(FileUtil.getRealPathFromURI(context, item.contentUri)).exists())
            return IncapableCause(IncapableCause.DIALOG, context.getString(R.string.msgSelectedItemNotAvailable))

        return null
    }

}