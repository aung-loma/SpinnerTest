package com.lyh.spintest.util

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

/**
 * Created by aungb on 11/9/2024.
 */

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(showRipple: Boolean = true, onClick: () -> Unit): Modifier =
    composed(
        inspectorInfo = {
            name = "clickableOnce"
            value = onClick
        }
    ) {
        var enableAgain by remember { mutableStateOf(true) }
        LaunchedEffect(enableAgain, block = {
            if (enableAgain) return@LaunchedEffect
            delay(timeMillis = 500L)
            enableAgain = true
        })
        Modifier.clickable(indication = if (!showRipple) null else rememberRipple(
            color = Color.Transparent, bounded = true
        ),
            interactionSource = remember { MutableInteractionSource() }) {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    }
