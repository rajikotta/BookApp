package com.raji.bookapp.presentation.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.raji.bookapp.presentation.LightBlue

@Composable
fun Dot(scale: State<Float>) {
    Box(
        Modifier.padding(5.dp)
            .size(8.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .background(LightBlue, shape = CircleShape)
    )
}


@Composable
fun ComposableAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale1 =
        infiniteTransition.animateFloat(
            0.2f,
            1f,
            // No offset for the 1st animation
            animationSpec = infiniteRepeatable(animation = tween(600), RepeatMode.Reverse)
        )
    val scale2 =
        infiniteTransition.animateFloat(
            0.2f,
            1f,
            infiniteRepeatable(
                tween(600),
                RepeatMode.Reverse,
                // Offsets the 2nd animation by starting from 150ms of the animation
                // This offset will not be repeated.
                initialStartOffset = StartOffset(offsetMillis = 150, StartOffsetType.FastForward)
            )
        )
    val scale3 =
        infiniteTransition.animateFloat(
            0.2f,
            1f,
            infiniteRepeatable(
                tween(600),
                RepeatMode.Reverse,
                // Offsets the 3rd animation by starting from 300ms of the animation. This
                // offset will be not repeated.
                initialStartOffset = StartOffset(offsetMillis = 300, StartOffsetType.FastForward)
            )
        )
    Row {
        Dot(scale1)
        Dot(scale2)
        Dot(scale3)
    }
}


