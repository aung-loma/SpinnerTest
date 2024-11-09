package com.lyh.spintest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.lyh.spintest.util.toBrush
import com.lyh.spintest.util.toColor
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun SpinWheel(
    modifier: Modifier = Modifier,
    items: List<SpinWheelItem>,
) {
    BoxWithConstraints(modifier = modifier.background(color = Color(0xFFFE370A), shape = CircleShape )) {

        val degreesPerItems = items.getDegreesPerItem()
        val size = min(this.maxHeight, this.maxWidth)
        items.forEachIndexed { index, item ->
            SpinWheelSlice(
                modifier = Modifier.rotate(degrees = degreesPerItems * index),
                size = size,
                color = if (index % 2 == 0) Color(0xFFFE370A) else Color(0xFFFFC83B),
                degree = degreesPerItems,
                topPadding = if (index % 2 == 0) 0.dp else 2.dp,
                content = item.content
            )
        }
    }
}


@Preview
@Composable
private fun SpinWheelPreview(
) {
    Box(modifier = Modifier.size(300.dp)) {
        SpinWheel(
            modifier = Modifier.fillMaxSize(),
            items = List(10) { index ->
                SpinWheelItem {
                    Text(text = "$index$", modifier = Modifier.rotate(270f))
                }
            },
        )
    }
}