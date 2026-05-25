package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType


private data class PolicySection(
    val number: Int,
    val title: String,
    val bullets: List<String>,
    val footer: String? = null,
)

private val POLICIES = listOf(
    PolicySection(
        number = 1,
        title = "Behaviour",
        bullets = listOf(
            "No foul language.",
            "No cell phones.",
            "Do not argue with customers or with co-workers.",
            "No shouting across the restaurant.",
            "Courtesy towards customers and fellow staff members.",
            "Show respect and listen to your managers.",
            "Do not deface company property.",
            "No physical fighting.",
        ),
    ),
    PolicySection(
        number = 2,
        title = "Appearance",
        bullets = listOf(
            "The appearance of restaurant employees reflects directly on the restaurant's standards.",
            "The employee must be dressed in the correct uniform — Lords shirt, black pants or skirt and black shoes.",
            "Uniform to be neat and clean.",
            "Men's hair to be kept short.",
            "Ladies' hair to be tied back.",
            "No excessive jewellery.",
            "No excessive makeup.",
            "We live in a humid environment — shower before coming to work.",
            "Use deodorant.",
            "No chewing of gum or sweets.",
        ),
        footer = "If you do not follow the appearance rules you will be sent home. Any employee, whether they handle food or not, must take their personal hygiene and cleanliness very seriously. Repeated offenses will result in warnings.",
    ),
    PolicySection(
        number = 3,
        title = "Time Keeping",
        bullets = listOf(
            "Each employee is responsible for knowing and adhering to the precise times written on the schedule.",
            "You are to be at work 15 minutes before your shift starts.",
            "Shift changes may only be done by a manager.",
            "If you are a waiter or barman and you cannot make your shift, you need to find a replacement to cover it.",
            "It is your responsibility to make sure you have clocked in and clocked out. You will not be paid for a time that does not reflect on the time sheet.",
        ),
        footer = "Barmen, waiters and runners who do not show up for a shift or who do not get permission for missing it will not be scheduled for 1 week.",
    ),
    PolicySection(
        number = 4,
        title = "Cash and Stock Shortages",
        bullets = listOf(
            "When cashing up after your shift you must have the correct amount of money with all card slips, vouchers and pay-outs.",
            "You are to wait until the manager has checked your cash up before leaving the premises.",
            "If your cash up is more than R50 short you will receive a warning.",
            "Bar cash ups that are short or over will result in a warning.",
            "Bar stock shortages may not exceed R150 a week per employee; failure to comply will result in a warning.",
            "You are responsible for the tables you serve — if a customer walks out before paying the bill, you are liable to pay the full amount.",
            "Cash shortages will be deducted from your weekly salary.",
        ),
    ),
)


@Composable
fun PoliciesScreen(onNext: () -> Unit) {
    var agreed by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LLColors.Parchment2, LLColors.Parchment)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LLColors.Navy)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Eyebrow("Lords and Legends", color = LLColors.CopperSoft)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Policies & Procedures",
                    style = LLType.Headline.copy(color = LLColors.Bone),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Please read carefully before proceeding",
                    style = LLType.BodySmall.copy(color = LLColors.SteelSoft),
                )
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                POLICIES.forEach { section ->
                    PolicyCard(section)
                }

                Spacer(Modifier.height(4.dp))


                AgreementRow(
                    checked = agreed,
                    onCheckedChange = { agreed = it },
                )

                Spacer(Modifier.height(8.dp))

                CopperButton(
                    text = "I Agree — Continue",
                    onClick = onNext,
                    enabled = agreed,
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}


@Composable
private fun PolicyCard(section: PolicySection) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LLColors.Surface, RoundedCornerShape(16.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        // Section number + title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(LLColors.Navy, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = section.number.toString(),
                    style = LLType.Eyebrow.copy(color = LLColors.Bone, letterSpacing = 0.sp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = section.title,
                style = LLType.SectionHeading,
                color = LLColors.Ink,
            )
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = LLColors.Line)
        Spacer(Modifier.height(12.dp))

        // Bullets
        section.bullets.forEach { bullet ->
            BulletRow(text = bullet)
            Spacer(Modifier.height(7.dp))
        }

        // Optional footer note
        if (section.footer != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = section.footer,
                style = LLType.BodySmall.copy(
                    color = LLColors.Muted,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

@Composable
private fun BulletRow(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp)
                .background(LLColors.Copper, CircleShape),
        )
        Spacer(Modifier.width(10.dp))
        Text(text, style = LLType.Body, color = LLColors.Ink)
    }
}

@Composable
private fun AgreementRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LLColors.Parchment, RoundedCornerShape(14.dp))
            .border(
                width = if (checked) 1.5.dp else 1.dp,
                color = if (checked) LLColors.Copper else LLColors.LineStrong,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Custom checkbox
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) LLColors.Copper else LLColors.Surface)
                .border(
                    1.5.dp,
                    if (checked) LLColors.Copper else LLColors.SteelSoft,
                    RoundedCornerShape(6.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = LLColors.Surface,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = "I have read and understood the Lords and Legends Policies and Procedures, " +
                    "and I agree to abide by these rules as a condition of my employment.",
            style = LLType.Body.copy(color = LLColors.Ink),
            modifier = Modifier.weight(1f),
        )
    }
}