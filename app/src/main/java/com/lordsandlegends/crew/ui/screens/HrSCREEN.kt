package com.lordsandlegends.crew.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.data.Contract
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import java.util.UUID

@Composable
fun Hrscreen(
    contracts: List<Contract>,
    onContractAdded: (Contract) -> Unit,
    onContractTapped: (Contract) -> Unit,
) {
    val context = LocalContext.current
        /*
        opens Android's native file picking UI, which uses pdfs only.
        When the manager picks one, it is then triggered
        */
    val pickPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let {
            //this allows the app to keep reading the pdf even if the app closes or restarts
            context.contentResolver.takePersistableUriPermission(
                it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
            val name = queryFileName(context, it) ?: "Contract.pdf"
            onContractAdded(Contract(id = UUID.randomUUID().toString(), fileName = name, fileUri = it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LLColors.Parchment2, LLColors.Parchment)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 20.dp),
        ) {
            Eyebrow("Manager tools", color = LLColors.Muted)
            Spacer(Modifier.height(4.dp))
            Text("Contracts", style = LLType.Headline.copy(fontSize = 26.sp), color = LLColors.Ink)

            Spacer(Modifier.height(16.dp))

            CopperButton(
                text = "Upload contract (PDF)",
                onClick = { pickPdfLauncher.launch(arrayOf("application/pdf")) },
            )

            Spacer(Modifier.height(20.dp))
            SectionHeading("Sent to staff")

            if (contracts.isEmpty()) {
                Text(
                    "No contracts uploaded yet.",
                    style = LLType.BodySmall,
                    color = LLColors.Muted,
                    modifier = Modifier.padding(top = 8.dp),
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    contracts.forEach { c -> ContractRow(c, onClick = { onContractTapped(c) }) }
                }
            }
        }
    }
}

@Composable
private fun ContractRow(contract: Contract, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(16.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(LLColors.Parchment, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Description, contentDescription = null, tint = LLColors.CopperDeep, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(contract.fileName, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
            Spacer(Modifier.height(2.dp))
            Text(
                if (contract.signed) "Signed" else "Awaiting signature",
                style = LLType.BodySmall,
                color = if (contract.signed) LLColors.Good else LLColors.Muted,
            )
        }
    }
}

private fun queryFileName(context: android.content.Context, uri: android.net.Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex >= 0) it.getString(nameIndex) else null
    }
}