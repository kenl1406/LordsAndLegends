package com.lordsandlegends.crew.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType


enum class Screen { Login,
    Policies,
    OnboardingDetails,
    Overview,
    Academy,
    Performance,
    Profile,
    PASSED,
    Contracts,
    SignContract
}

/* ---------------------------------------------------------- */

@Composable
fun SectionHeading(text: String, sub: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 14.dp, bottom = 10.dp)) {
        Text(text, style = LLType.SectionHeading, color = LLColors.Ink)
        if (sub != null) {
            Spacer(Modifier.height(2.dp))
            Text(sub, style = LLType.BodySmall, color = LLColors.Muted)
        }
    }
}

@Composable
fun Eyebrow(text: String, color: Color = LLColors.CopperDeep, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = LLType.Eyebrow,
        color = color,
        modifier = modifier,
    )
}

/* ---------------------------------------------------------- */

@Composable
fun TopBar(
    title: String? = null,
    greetingTop: String? = null,
    greetingBottom: String? = null,
    leadingIcon: ImageVector? = null,
    onLeading: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailing: (() -> Unit)? = null,
    trailingDot: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // leading
        if (onLeading != null && leadingIcon != null) {
            CircleIconButton(icon = leadingIcon, onClick = onLeading)
        } else {
            Spacer(Modifier.width(40.dp))
        }

        // center / left text
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            if (greetingTop != null) Eyebrow(greetingTop, LLColors.Muted)
            if (greetingBottom != null) {
                Text(greetingBottom, style = LLType.Headline, color = LLColors.Ink)
            }
            if (title != null) {
                Text(title, style = LLType.Title, color = LLColors.Ink)
            }
        }

        // trailing
        if (onTrailing != null && trailingIcon != null) {
            Box {
                CircleIconButton(icon = trailingIcon, onClick = onTrailing)
                if (trailingDot) {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .background(LLColors.Copper, CircleShape)
                            .border(2.dp, LLColors.Surface, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        } else {
            Spacer(Modifier.width(40.dp))
        }
    }
}

@Composable
fun CircleIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(LLColors.Surface, CircleShape)
            .border(1.dp, LLColors.Line, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = LLColors.Ink, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun BackBar(title: String, onBack: () -> Unit, trailingIcon: ImageVector? = null, onTrailing: (() -> Unit)? = null) {
    TopBar(
        title = title,
        leadingIcon = Icons.Outlined.ChevronLeft,
        onLeading = onBack,
        trailingIcon = trailingIcon,
        onTrailing = onTrailing,
    )
}

/* ---------------------------------------------------------- */

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    border: BorderStroke = BorderStroke(1.dp, LLColors.Line),
    background: Color = LLColors.Surface,
    topAccent: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val base = Modifier
        .fillMaxWidth()
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }

    Box(
        modifier = base
            .then(modifier)
            .background(background, shape)
            .border(border, shape)
    ) {
        if (topAccent) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(LLColors.Copper)
            )
        }
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

/* ---------------------------------------------------------- */

@Composable
fun CopperButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .background(LLColors.Copper, RoundedCornerShape(12.dp))
            .let { if (enabled) it.clickable(onClick = onClick) else it }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            color = Color.White,
            style = LLType.Body.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            color = LLColors.CopperDeep,
            style = LLType.Body.copy(fontWeight = FontWeight.Medium),
        )
    }
}

@Composable
fun Chip(
    text: String,
    onClick: (() -> Unit)? = null,
    background: Color = LLColors.Copper,
    foreground: Color = Color.White,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
) {
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(99.dp))
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(contentPadding),
    ) {
        Text(text, color = foreground, style = LLType.BodySmall.copy(fontWeight = FontWeight.SemiBold))
    }
}

/* ---------------------------------------------------------- */

@Composable
fun Pill(text: String, active: Boolean, onClick: () -> Unit) {
    val bg = if (active) LLColors.Ink else LLColors.Surface
    val fg = if (active) LLColors.Bone else LLColors.Ink
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(99.dp))
            .border(1.dp, if (active) LLColors.Ink else LLColors.Line, RoundedCornerShape(99.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(text, color = fg, style = LLType.BodySmall.copy(fontWeight = FontWeight.Medium))
    }
}

@Composable
fun BadgeText(text: String) {
    Box(
        modifier = Modifier
            .background(LLColors.Parchment, RoundedCornerShape(99.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(99.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, style = LLType.BodySmall.copy(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified), color = LLColors.Ink)
    }
}

/* ---------------------------------------------------------- */

/** Render "<word> <em>highlighted</em> <rest>" with italic copper highlight */
@Composable
fun emphasizedSentence(prefix: String, italicCopper: String, suffix: String) =
    buildAnnotatedString {
        append(prefix)
        withStyle(SpanStyle(color = LLColors.CopperDeep, fontWeight = FontWeight.SemiBold)) {
            append(italicCopper)
        }
        append(suffix)
    }