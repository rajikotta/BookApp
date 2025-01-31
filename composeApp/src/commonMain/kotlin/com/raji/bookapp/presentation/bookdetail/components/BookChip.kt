package com.raji.bookapp.presentation.bookdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.raji.bookapp.presentation.LightBlue

enum class ChipSize {
    SMALL,
    REGULAR
}


@Composable
fun BookChip(
    modifier: Modifier = Modifier, size: ChipSize = ChipSize.REGULAR,
    content: @Composable () -> Unit
) {
    Box(
        modifier.widthIn(min = if (size == ChipSize.REGULAR) 80.dp else 50.dp).clip(
            RoundedCornerShape(16.dp)
        ).background(LightBlue)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}