package com.pyrinnl.githubrepo.utills

import android.content.Context
import android.util.Base64
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.StandardCharsets


internal fun String.isASCII(): Boolean {

    val byteArray = this.toByteArray()
    val decoder = StandardCharsets.US_ASCII.newDecoder()

    val isASCII = try {
        decoder.decode(ByteBuffer.wrap(byteArray))
        true
    } catch (e: CharacterCodingException) {
        false
    }
    return isASCII
}

internal fun String.toMarkdown(): String {
    val data = Base64.decode(this, Base64.DEFAULT)
    return String(data, StandardCharsets.UTF_8)
}

internal fun TextView.setTextColor(context: Context, @ColorRes color: Int) {
    setTextColor(
        ContextCompat.getColor(
            context,
            color
        )
    )
}