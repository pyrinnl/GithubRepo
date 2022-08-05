package com.pyrinnl.githubrepo.utills

import android.util.Base64
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.StandardCharsets


fun String.isASCII(): Boolean {

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

fun String.toMarkdown(): String {
    val data = Base64.decode(this, Base64.DEFAULT)
    return String(data, StandardCharsets.UTF_8)
}