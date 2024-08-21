package com.example.simplegames.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.simplegames.R

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}

@Composable
fun ShowHideSettingsButton(
    onClick: () -> Unit,
    isHidden: Boolean,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Button(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.Gray
            )
        ) {
            if(isHidden) {
                Text(text = stringResource(R.string.show_settings))
            } else {
                Text(text = stringResource(R.string.hide_settings))
            }
        }
    }
}