package xyz.ggeorge.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    icon: ImageVector = Icons.Filled.Info,
    iconSize: Dp = 46.dp,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    visible: Boolean = true
) {

    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    var showIcon by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(150)
            showIcon = true
            delay(200)
            showTitle = true
            delay(200)
            showMessage = true
        } else {
            showIcon = false
            showTitle = false
            showMessage = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500, easing = FastOutSlowInEasing)) + scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialScale = 0.9f
        ),
        exit = fadeOut(tween(300)) + scaleOut(tween(300))
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            color = containerColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .animateContentSize(
                        animationSpec = tween(500, easing = LinearOutSlowInEasing)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showIcon,
                    enter = scaleIn(
                        animationSpec = tween(600, easing = OvershootEasing),
                        initialScale = 0.5f
                    ) + fadeIn(tween(400)),
                    exit = fadeOut(tween(200))
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier
                            .size(iconSize)
                            .clip(CircleShape)
                            .padding(2.dp)
                            .background(
                                color = accentColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(8.dp)
                    )
                }

                AnimatedVisibility(
                    visible = showTitle,
                    enter = fadeIn(tween(400, easing = FastOutSlowInEasing)) + slideInVertically(
                        tween(400, easing = FastOutSlowInEasing), initialOffsetY = { -40 }
                    ),
                    exit = fadeOut(tween(200))
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                AnimatedVisibility(
                    visible = showMessage,
                    enter = fadeIn(tween(400, easing = FastOutSlowInEasing)) + slideInVertically(
                        tween(400, easing = FastOutSlowInEasing), initialOffsetY = { 40 }
                    ),
                    exit = fadeOut(tween(200))
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        softWrap = true
                    )
                }
            }
        }
    }
}

private val OvershootEasing = Easing { fraction ->
    val tension = 2.0f
    val t = fraction - 1.0f
    t * t * ((tension + 1) * t + tension) + 1.0f
}