package com.lordsandlegends.crew.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.data.WarningRecord
import com.lordsandlegends.crew.data.strokesToSignatureBitmap
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.SignaturePad
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWarningDialog(
    onDismiss: () -> Unit,
    onSave: (WarningRecord, Bitmap?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Warning") }
    val types = listOf("Warning", "Performance Note")
    var showDatePicker by remember { mutableStateOf(false) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var clearSignal by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Create New Record",
                style = LLType.Headline.copy(fontSize = 20.sp),
                color = LLColors.Ink
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Type of note selection
                Eyebrow("Type", color = LLColors.Muted)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    types.forEach { type ->
                        FilterChip(
                            onClick = { selectedType = type },
                            label = { Text(type, style = LLType.BodySmall) },
                            selected = selectedType == type,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LLColors.Copper,
                                selectedLabelColor = LLColors.Surface
                            )
                        )
                    }
                }


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", style = LLType.BodySmall) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LLType.Body,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LLColors.Copper,
                        unfocusedBorderColor = LLColors.Line
                    )
                )


                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname", style = LLType.BodySmall) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LLType.Body,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LLColors.Copper,
                        unfocusedBorderColor = LLColors.Line
                    )
                )


                OutlinedTextField(
                    value = date,
                    onValueChange = { },
                    label = { Text("Date", style = LLType.BodySmall) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LLType.Body,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Pick Date",
                                tint = LLColors.Copper
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LLColors.Copper,
                        unfocusedBorderColor = LLColors.Line
                    )
                )


                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for note", style = LLType.BodySmall) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4,
                    textStyle = LLType.Body,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LLColors.Copper,
                        unfocusedBorderColor = LLColors.Line
                    )
                )

                // Signature section
                Eyebrow("Manager's Signature (Draw below)", color = LLColors.Muted)
                SignaturePad(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(vertical = 8.dp),
                    onStrokesChanged = { strokes = it },
                    clearSignal = clearSignal
                )

                // Clear signature button
                TextButton(
                    onClick = {
                        clearSignal++
                        strokes = emptyList()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Clear Signature", style = LLType.BodySmall, color = LLColors.Muted)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && surname.isNotBlank() && date.isNotBlank() && reason.isNotBlank()) {
                        val signatureBitmap = if (strokes.isNotEmpty()) {
                            strokesToSignatureBitmap(strokes, width = 400, height = 200)
                        } else null

                        val record = WarningRecord(
                            name = name,
                            surname = surname,
                            date = date,
                            reason = reason,
                            type = selectedType,
                            managerSignature = signatureBitmap
                        )
                        onSave(record, signatureBitmap)
                    }
                },
                enabled = name.isNotBlank() && surname.isNotBlank() && date.isNotBlank() && reason.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LLColors.Copper
                )
            ) {
                Text("Create Record")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LLColors.Muted)
            }
        }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                date = selectedDate
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val date = Date(millis)
                        onDateSelected(dateFormat.format(date))
                    }
                }
            ) {
                Text("OK", color = LLColors.Copper)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LLColors.Muted)
            }
        }
    ) {
        androidx.compose.material3.DatePicker(
            state = datePickerState
        )
    }
}