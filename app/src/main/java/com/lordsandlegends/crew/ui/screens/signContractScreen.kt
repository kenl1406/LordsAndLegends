package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.data.Contract
import com.lordsandlegends.crew.data.stampSignatureOntoPdf
import com.lordsandlegends.crew.data.strokesToSignatureBitmap
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.GhostButton
import com.lordsandlegends.crew.ui.components.SignaturePad
import com.lordsandlegends.crew.ui.components.SurfaceCard
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import java.io.File

@Composable
fun SignContractScreen(
    contract: Contract,
    onSigned: () -> Unit,
) {
    val context = LocalContext.current
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var clearSignal by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LLColors.Parchment2, LLColors.Parchment)))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(22.dp)) {
            Eyebrow("Onboarding", color = LLColors.Muted)
            Spacer(Modifier.height(4.dp))
            Text(contract.fileName, style = LLType.Headline, color = LLColors.Ink)

            Spacer(Modifier.height(16.dp))

            SurfaceCard {
                Column {
                    Text("Please review the contract before signing.", style = LLType.Body, color = LLColors.Ink)
                    Spacer(Modifier.height(12.dp))
                    GhostButton(
                        text = "View PDF",
                        onClick = {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                setDataAndType(contract.fileUri, "application/pdf")
                                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        },
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Eyebrow("Sign below", color = LLColors.Muted)
            Spacer(Modifier.height(8.dp))

            SignaturePad(
                onStrokesChanged = { strokes = it },
                clearSignal = clearSignal,
            )

            Spacer(Modifier.height(12.dp))
            GhostButton(text = "Clear", onClick = { clearSignal++; strokes = emptyList() })

            Spacer(Modifier.height(16.dp))
            CopperButton(
                text = "Submit signature",
                enabled = strokes.isNotEmpty(),
                onClick = {
                    val sigBitmap = strokesToSignatureBitmap(strokes, width = 800, height = 400)
                    val outFile = File(context.filesDir, "${contract.id}_signed.pdf")
                    stampSignatureOntoPdf(
                        context = context,
                        sourcePdfUri = contract.fileUri,
                        signatureBitmap = sigBitmap,
                        outputFile = outFile,
                    )
                    contract.signed = true
                    contract.signedAt = System.currentTimeMillis()
                    // production: upload outFile to your backend/storage here
                    onSigned()
                },
            )
        }
    }
}