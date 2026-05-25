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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FileUpload
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.GhostButton
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType


@Composable
fun OnboardingDetailsScreen(
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    // Form state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var taxNumber by remember { mutableStateOf("") }
    var idFileName by remember { mutableStateOf<String?>(null) }
    var bankFileName by remember { mutableStateOf<String?>(null) }

    val isFormValid = firstName.isNotBlank()
            && lastName.isNotBlank()
            && address.isNotBlank()
            && phone.isNotBlank()
            && email.isNotBlank()
            && taxNumber.isNotBlank()
            && idFileName != null
            && bankFileName != null

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
                Eyebrow("Employee Onboarding", color = LLColors.CopperSoft)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Your Details",
                    style = LLType.Headline.copy(color = LLColors.Bone),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "This information is kept securely on file",
                    style = LLType.BodySmall.copy(color = LLColors.SteelSoft),
                )
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {


                FormCard(title = "Personal Information") {
                    FormField(
                        label = "First Name",
                        value = firstName,
                        onValueChange = { firstName = it },
                        placeholder = "e.g. Sipho",
                    )
                    Spacer(Modifier.height(14.dp))
                    FormField(
                        label = "Last Name / Surname",
                        value = lastName,
                        onValueChange = { lastName = it },
                        placeholder = "e.g. Dlamini",
                    )
                    Spacer(Modifier.height(14.dp))
                    FormField(
                        label = "Residential Address",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Street, City, Province, Code",
                        singleLine = false,
                        minLines = 2,
                    )
                }

                Spacer(Modifier.height(16.dp))


                FormCard(title = "Contact Details") {
                    FormField(
                        label = "Contact Number",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "e.g. 082 000 0000",
                        keyboard = KeyboardType.Phone,
                    )
                    Spacer(Modifier.height(14.dp))
                    FormField(
                        label = "Email Address",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "e.g. name@email.com",
                        keyboard = KeyboardType.Email,
                    )
                    Spacer(Modifier.height(14.dp))
                    FormField(
                        label = "Tax Number (SARS)",
                        value = taxNumber,
                        onValueChange = { taxNumber = it },
                        placeholder = "10-digit tax reference number",
                        keyboard = KeyboardType.Number,
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ── Documents card ───────────────────────
                FormCard(title = "Required Documents") {
                    Text(
                        text = "Please attach a copy of each document. " +
                                "These are required before your first shift.",
                        style = LLType.BodySmall,
                        color = LLColors.Muted,
                    )
                    Spacer(Modifier.height(16.dp))

                    UploadField(
                        label = "Copy of ID / Passport",
                        icon = Icons.Outlined.AccountBox,
                        fileName = idFileName,
                        onTap = {
                            // In production: launch file picker intent here
                            idFileName = "ID_document.pdf"
                        },
                    )

                    Spacer(Modifier.height(12.dp))

                    UploadField(
                        label = "Proof of Bank Details",
                        icon = Icons.Outlined.CreditCard,
                        hint = "Bank-stamped letter or statement header",
                        fileName = bankFileName,
                        onTap = {
                            // In production: launch file picker intent here
                            bankFileName = "Bank_proof.pdf"
                        },
                    )
                }

                Spacer(Modifier.height(24.dp))

                CopperButton(
                    text = "Submit & Continue",
                    onClick = onSubmit,
                    enabled = isFormValid,
                )

                Spacer(Modifier.height(8.dp))

                GhostButton(text = "Back", onClick = onBack)

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun FormCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LLColors.Surface, RoundedCornerShape(16.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Text(title, style = LLType.SectionHeading, color = LLColors.Ink)
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = LLColors.Line)
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboard: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    Column {
        Eyebrow(label, color = LLColors.Muted)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LLColors.Parchment2, RoundedCornerShape(12.dp))
                .border(
                    width = if (value.isNotEmpty()) 1.5.dp else 1.dp,
                    color = if (value.isNotEmpty()) LLColors.Copper else LLColors.Line,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            if (value.isEmpty()) {
                Text(placeholder, style = LLType.Body, color = LLColors.SteelSoft)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                minLines = minLines,
                textStyle = LLType.Body.copy(color = LLColors.Ink),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun UploadField(
    label: String,
    icon: ImageVector,
    fileName: String?,
    onTap: () -> Unit,
    hint: String? = null,
) {
    val uploaded = fileName != null
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (uploaded) LLColors.Parchment else LLColors.Parchment2,
                RoundedCornerShape(12.dp),
            )
            .border(
                width = if (uploaded) 1.5.dp else 1.dp,
                color = if (uploaded) LLColors.Copper else LLColors.LineStrong,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable { onTap() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (uploaded) icon else Icons.Outlined.FileUpload,
            contentDescription = null,
            tint = if (uploaded) LLColors.Copper else LLColors.Steel,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Eyebrow(label, color = if (uploaded) LLColors.CopperDeep else LLColors.Muted)
            if (uploaded && fileName != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = fileName,
                    style = LLType.BodySmall,
                    color = LLColors.Good,
                )
            } else {
                if (hint != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(hint, style = LLType.BodySmall, color = LLColors.Muted)
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Tap to attach file",
                    style = LLType.BodySmall,
                    color = LLColors.Steel,
                )
            }
        }
    }
}
