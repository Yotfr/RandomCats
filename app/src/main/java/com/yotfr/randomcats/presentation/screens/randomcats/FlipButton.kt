package com.yotfr.randomcats.presentation.screens.randomcats

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FlipButton(
    buttonFace: ButtonFace,
    onClick: (buttonFace: ButtonFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    backIcon: ImageVector,
    frontIcon: ImageVector,
    backIconDescription: String,
    frontIconDescription: String,
    isButtonEnabled: Boolean
) {
    val rotation = animateFloatAsState(
        targetValue = buttonFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )
    FilledIconButton(
        onClick = { onClick(buttonFace) },
        modifier = modifier
            .height(64.dp)
            .width(64.dp)
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            },
        enabled = isButtonEnabled
    ) {
        if (rotation.value <= 90) {
            Icon(
                imageVector = frontIcon,
                contentDescription = frontIconDescription
            )
        } else {
            Icon(
                imageVector = backIcon,
                contentDescription = backIconDescription,
                modifier = Modifier
                    .graphicsLayer {
                        if (axis == RotationAxis.AxisX) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    }
            )
        }
    }
}

enum class ButtonFace(val angle: Float) {
    Front(0f) {
        override val next: ButtonFace
            get() = Back
    },
    Back(180f) {
        override val next: ButtonFace
            get() = Front
    };

    abstract val next: ButtonFace
}

enum class RotationAxis {
    AxisX,
    AxisY
}