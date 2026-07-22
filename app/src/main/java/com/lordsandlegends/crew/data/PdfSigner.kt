package com.lordsandlegends.crew.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.io.File
// Opens the original PDF, grabs its last page, and turns the signature bitmap
// into an image PdfBox can use. Then draws that signature onto the bottom-right
// corner of the page without erasing anything already on it, and saves the
// result as a new file so the original PDF stays untouched.
fun stampSignatureOntoPdf(
    context: Context,
    sourcePdfUri: Uri,
    signatureBitmap: Bitmap,
    outputFile: File,
) {
    context.contentResolver.openInputStream(sourcePdfUri)?.use { input ->
        val document = PDDocument.load(input)
        try {
            val lastPage: PDPage = document.getPage(document.numberOfPages - 1)
            val pdImage = LosslessFactory.createFromImage(document, signatureBitmap)

            val pageWidth = lastPage.mediaBox.width
            val sigWidth = 160f
            val sigHeight = sigWidth * signatureBitmap.height / signatureBitmap.width

            PDPageContentStream(
                document, lastPage, PDPageContentStream.AppendMode.APPEND, true, true,
            ).use { stream ->
                stream.drawImage(pdImage, pageWidth - sigWidth - 40f, 40f, sigWidth, sigHeight)
            }

            document.save(outputFile)
        } finally {
            document.close()
        }
    }
}