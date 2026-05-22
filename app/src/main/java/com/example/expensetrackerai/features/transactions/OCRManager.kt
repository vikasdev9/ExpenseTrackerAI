package com.example.expensetrackerai.features.transactions

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OCRManager @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognizeText(
        bitmap: Bitmap,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                onSuccess(visionText.text)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Extracts structured transaction data from raw OCR text.
     */
    fun extractTransactionData(text: String): TransactionData {
        val lines = text.split("\n")
        var amount: Double? = null
        val date = Date() // Default to today
        val merchant: String?

        // Regex for currency amounts
        val amountRegex = Regex("(?:total|sum|amount)?[:\\s]*[$\\u00A3\\u20AC]?\\s*(\\d+[.,]\\d{2})", RegexOption.IGNORE_CASE)
        
        for (line in lines) {
            val match = amountRegex.find(line)
            if (match != null) {
                val foundAmount = match.groups[1]?.value?.replace(",", ".")?.toDoubleOrNull()
                if (foundAmount != null) {
                    val currentAmount = amount
                    if (currentAmount == null || foundAmount > currentAmount) {
                        amount = foundAmount
                    }
                }
            }
        }

        merchant = lines.firstOrNull { it.isNotBlank() }?.trim()

        return TransactionData(
            merchant = merchant ?: "Unknown Merchant",
            amount = amount ?: 0.0,
            date = date
        )
    }

    data class TransactionData(
        val merchant: String,
        val amount: Double,
        val date: Date
    )
}
