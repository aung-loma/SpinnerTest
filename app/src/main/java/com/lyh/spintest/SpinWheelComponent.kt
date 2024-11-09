package com.lyh.spintest

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun SpinWheelComponent(spinWheelState: SpinWheelState) {
    val dotCount = 24
    val highlightIndex = (spinWheelState.rotation.value / (360f / dotCount)).toInt() % dotCount
    Box(
        modifier = Modifier.size(230.dp).fillMaxWidth()
    ) {
        GradientWheelBorder()

        DottedBorderWithHighlight(dotCount, 8f,  spinWheelState.rotation.isRunning)

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .aspectRatio(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SpinWheel(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = spinWheelState.rotation.value
                    }, items = spinWheelState.items)
                Box (modifier = Modifier.size(75.dp)) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(75.dp),
                        painter = painterResource(id = spinWheelState.centerImage),
                        contentDescription = null
                    )
                    Text(
                        text = "GO",
                        modifier = Modifier.align(Alignment.Center).padding(top = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700,
                        lineHeight = 18.79.sp,
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpinWheelComponentPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        val items = remember {
            List(10) { index ->
                SpinWheelItem {
                    Text(text = "$$index")
                }

            }.toPersistentList()
        }

        val state = rememberSpinWheelState(
            items = items,
            backgroundImage = R.drawable.ic_spinner_background,
            centerImage = R.drawable.ic_spin_button,
            onSpinningFinished = {},
        )
        SpinWheelComponent(state)
    }
}

@Composable
fun GradientWheelBorder() {
    // Gradient Brush for border
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFDB33A), Color(0xFFE0D5BC), Color(0xFFFDB33A)),
        start = Offset(0f, 0f),
        end = Offset(200f, 200f) // Adjust the offset as per the size of your composable
    )

    Box(
        modifier = Modifier
            .size(300.dp) // Size of the wheel
            .border(
                border = BorderStroke(20.dp, gradientBrush),
                shape = CircleShape
            )
    ) {
        // Wheel Content goes here
    }
}

@Composable
fun DottedBorderWithHighlight(
    dotCount: Int = 24,
    dotRadius: Float = 8f,
    isSpinning: Boolean
) {
    // Infinite transition for the blinking effect on all dots
    val infiniteTransition = rememberInfiniteTransition()

    // Animation controlling the opacity of the dots for blinking
    val blinkingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 200, // Adjust for faster/slower blinking speed
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier
            .size(230.dp)
            .padding(8.dp)
    ) {
        val radius = size.minDimension / 2
        val center = Offset(radius, radius)

        for (i in 0 until dotCount) {
            val angle = (2 * Math.PI * i / dotCount).toFloat()
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)

            // Dot colors for blinking
            val dotColor1 = if (isSpinning) Color.Yellow.copy(blinkingAlpha) else  Color(0xFFE6F0F9)
            val dotColor2 = Color(0xFFFE370A)
            //.copy(alpha = if (isSpinning) blinkingAlpha else 1f)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(dotColor1, dotColor2),
                    center = Offset(x, y),
                    radius = dotRadius
                ),
                radius = dotRadius,
                center = Offset(x, y)
            )
        }
    }
}



/*@Composable
fun DottedBorderWithHighlight(
    dotCount: Int = 24,
    dotRadius: Float = 8f,
    highlightIndex: Int?,
    isSpinning: Boolean
) {
    Canvas(
        modifier = Modifier.size(230.dp).padding(8.dp)
    ) {
        val radius = size.minDimension / 2
        val center = Offset(radius, radius)

        // Adjust the starting angle (top = 12 o'clock or 0 degrees)
        val startAngle = -90f

        for (i in 0 until dotCount) {
            // Calculate angle for each dot, with the start at -90 degrees (12 o'clock)
            val angle = (2 * Math.PI * i / dotCount).toFloat() + Math.toRadians(startAngle.toDouble()).toFloat()
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)

            // If isSpinning is true, highlight the current dot index
            val dotColor1 = if (isSpinning && i == highlightIndex) Color.White else Color(0xFFE6F0F9)
            val dotColor2 = if (isSpinning && i == highlightIndex) Color.White else Color(0xFFFE370A)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(dotColor1, dotColor2),
                    center = Offset(x, y),
                    radius = dotRadius
                ),
                radius = dotRadius,
                center = Offset(x, y)
            )
        }
    }
}*/
