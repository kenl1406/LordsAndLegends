package com.lordsandlegends.crew.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.data.WarningRecord
import com.lordsandlegends.crew.ui.components.*
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarningsPerformanceScreen(
    onBack: () -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var records by remember { mutableStateOf(listOf<WarningRecord>()) }
    var selectedRecord by remember { mutableStateOf<WarningRecord?>(null) }
    var showDetailsDialog by remember { mutableStateOf(false) }  // Changed from showPreviewDialog

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .padding(horizontal = 22.dp)
            .padding(bottom = 16.dp)
    ) {
        BackBar(
            title = "Warnings & Performance",
            onBack = onBack,
        )

        Spacer(Modifier.height(12.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Records",
                value = records.size.toString(),
                color = LLColors.Copper
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Warnings",
                value = records.count { it.type == "Warning" }.toString(),
                color = LLColors.Bad
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Performance",
                value = records.count { it.type == "Performance Note" }.toString(),
                color = LLColors.Good
            )
        }

        Spacer(Modifier.height(16.dp))

        // Records list
        if (records.isEmpty()) {
            SurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                background = LLColors.Surface.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Manager tool",
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "No records yet",
                        style = LLType.Headline,
                        color = LLColors.Ink
                    )
                    Text(
                        text = "Tap the button below to create a warning or performance note.",
                        style = LLType.BodySmall,
                        color = LLColors.Muted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(records) { record ->
                    WarningRecordCard(
                        record = record,
                        onViewDetails = {
                            selectedRecord = record
                            showDetailsDialog = true
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        CopperButton(
            text = "Create New Record",
            onClick = { showCreateDialog = true }
        )
    }

    // Create Dialog
    if (showCreateDialog) {
        CreateWarningDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { newRecord, signatureBitmap ->
                records = records + newRecord
                showCreateDialog = false
            }
        )
    }

    // Details Dialog (replacing Preview Dialog)
    if (showDetailsDialog && selectedRecord != null) {
        WarningDetailsDialog(
            record = selectedRecord!!,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    SurfaceCard(
        modifier = modifier,
        background = LLColors.Surface,
        border = BorderStroke(1.dp, LLColors.Line)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = LLType.Headline.copy(fontSize = 24.sp),
                color = color
            )
            Text(
                text = label,
                style = LLType.BodySmall,
                color = LLColors.Muted
            )
        }
    }
}

@Composable
private fun WarningRecordCard(
    record: WarningRecord,
    onViewDetails: () -> Unit
) {
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        background = LLColors.Surface,
        border = BorderStroke(1.dp, LLColors.Line),
        onClick = onViewDetails
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${record.name} ${record.surname}",
                        style = LLType.Title,
                        color = LLColors.Ink
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                if (record.type == "Warning") LLColors.Bad else LLColors.Good,
                                RoundedCornerShape(99.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = record.type,
                            style = LLType.Eyebrow.copy(fontSize = 9.sp),
                            color = LLColors.Surface
                        )
                    }
                }
                Text(
                    text = "${record.date} • ${record.reason}",
                    style = LLType.BodySmall,
                    color = LLColors.Muted,
                    maxLines = 1
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = LLColors.Muted
            )
        }
    }
}


@Composable
private fun WarningDetailsDialog(
    record: WarningRecord,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Record Details",
                style = LLType.Headline.copy(fontSize = 20.sp),
                color = LLColors.Ink
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    Text(
                        "Name: ",
                        style = LLType.Body.copy(fontWeight = FontWeight.Bold),
                        color = LLColors.Ink
                    )
                    Text(
                        "${record.name} ${record.surname}",
                        style = LLType.Body,
                        color = LLColors.Ink
                    )
                }
                Row {
                    Text(
                        "Date: ",
                        style = LLType.Body.copy(fontWeight = FontWeight.Bold),
                        color = LLColors.Ink
                    )
                    Text(
                        record.date,
                        style = LLType.Body,
                        color = LLColors.Ink
                    )
                }
                Row {
                    Text(
                        "Type: ",
                        style = LLType.Body.copy(fontWeight = FontWeight.Bold),
                        color = LLColors.Ink
                    )
                    Text(
                        record.type,
                        style = LLType.Body,
                        color = if (record.type == "Warning") LLColors.Bad else LLColors.Good
                    )
                }
                Column {
                    Text(
                        "Reason:",
                        style = LLType.Body.copy(fontWeight = FontWeight.Bold),
                        color = LLColors.Ink
                    )
                    Text(
                        record.reason,
                        style = LLType.BodySmall,
                        color = LLColors.Muted
                    )
                }
                if (record.managerSignature != null) {
                    Text(
                        "✓ Signed by Manager",
                        style = LLType.BodySmall.copy(fontWeight = FontWeight.Bold),
                        color = LLColors.Good
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LLColors.Copper
                )
            ) {
                Text("Close")
            }
        }
    )
}