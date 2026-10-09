package com.abcode.quran.util

import android.content.Intent
import com.abcode.quran.audio.AppContext

actual fun shareText(text: String) {
    val context = AppContext.requireContext()
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val chooser = Intent.createChooser(sendIntent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}
