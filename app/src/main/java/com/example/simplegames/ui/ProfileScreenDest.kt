package com.example.simplegames.ui

import androidx.annotation.StringRes
import com.example.simplegames.R

enum class ProfileScreenDest(@StringRes val title: Int) {
    MAIN(title = R.string.profile_screen_title),
    LANGUAGES(title = R.string.languages),
}