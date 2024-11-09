package com.lyh.spintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.lyh.spintest.ui.theme.SpinTestTheme
import com.lyh.spintest.util.noRippleClickable
import com.lyh.spintest.util.toColor
import kotlinx.collections.immutable.toPersistentList
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLastIndex by remember { mutableStateOf(false) }
            SpinTestTheme {
                val items = remember {
                    listOf(
                        "VIP 1", "1$", "5$", "10$", "VIP 2",
                        "50$", "VIP 3", "75$", "100$", "AA"
                        ).map { item ->
                        SpinWheelItem {
                            Text(
                                text = "$item",
                                style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.W700, lineHeight = 17.31.sp),
                                modifier = Modifier.rotate(270f)
                            )
                        }

                    }.toPersistentList()
                }

                val spinState = rememberSpinWheelState(
                    items = items,
                    backgroundImage = R.drawable.ic_spinner_background,
                    centerImage = R.drawable.ic_spin_button,
                    onSpinningFinished = null,
                    rotationPerSecond = 2f
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.padding(24.dp).size(230.dp), contentAlignment = Alignment.Center) {
                            SpinWheelComponent(spinState)
                        }
                        Button(onClick = {
                            if(isLastIndex) {
                                spinState.spinWithCustomTimeAndStop(9,10f)
                                isLastIndex = false
                            } else {
                                spinState.spinWithCustomTimeAndStop(Random.nextInt(0,8),10f)
                            }
                        }, modifier = Modifier.padding(top = 24.dp)) {
                            Text(text = "Go")
                        }
                    }

                    Box(modifier = Modifier.padding(24.dp).size(30.dp).clip(CircleShape).background(color = Color.Transparent, shape = CircleShape).align(Alignment.BottomStart).noRippleClickable(false) {
                        isLastIndex = true
                    })
                }
            }
        }
    }
}

fun getDegreeFromSection(items: List<SpinWheelItem>, section: Int): Float {
    val pieDegree = 360f / items.size
    return pieDegree * section.times(-1)
}

fun getDegreeFromSectionWithRandom(items: List<SpinWheelItem>, section: Int): Float {
    val pieDegree = 360f / items.size
    val exactDegree = pieDegree * section.times(-1)

    val pieReduced = pieDegree * 0.9f //to avoid stop near border
    val multiplier = if (Random.nextBoolean()) 1f else -1f //before or after exact degree
    val randomDegrees = Random.nextDouble(0.0, pieReduced / 2.0)
    return exactDegree + (randomDegrees.toFloat() * multiplier)
}