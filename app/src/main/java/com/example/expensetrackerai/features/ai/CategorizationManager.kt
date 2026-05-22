package com.example.expensetrackerai.features.ai

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategorizationManager @Inject constructor(
    private val context: Context
) {
    private var interpreter: Interpreter? = null

    init {
        try {
            // Load model from assets (placeholder for actual model file)
            // val model = loadModelFile("categorization_model.tflite")
            // interpreter = Interpreter(model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadModelFile(modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = java.io.FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Categorizes a transaction using AI model or heuristic fallback.
     * In a production app, this would perform TFLite inference.
     */
    fun categorizeTransaction(title: String): String {
        val lowerTitle = title.trim().lowercase()
        
        // Mock AI Model Inference logic
        // This represents what the TFLite model would have been trained to recognize
        val aiPredictedCategory = when {
            lowerTitle.matches(Regex(".*(starbucks|coffee|cafe|tea).*")) -> "Food \u0026 Drink"
            lowerTitle.matches(Regex(".*(mcdonald|burger|pizza|restaurant|dining).*")) -> "Food \u0026 Drink"
            lowerTitle.matches(Regex(".*(shell|gas|petrol|fuel|chevron).*")) -> "Transport"
            lowerTitle.matches(Regex(".*(train|bus|metro|subway|uber|lyft).*")) -> "Transport"
            lowerTitle.matches(Regex(".*(walmart|target|tesco|grocery|market).*")) -> "Groceries"
            lowerTitle.matches(Regex(".*(netflix|spotify|hulu|hbo|movie|cinema).*")) -> "Entertainment"
            lowerTitle.matches(Regex(".*(apple|amazon|ebay|shopping|mall).*")) -> "Shopping"
            lowerTitle.matches(Regex(".*(rent|mortgage|apartment).*")) -> "Housing"
            lowerTitle.matches(Regex(".*(electric|water|gas bill|internet|wifi).*")) -> "Utilities"
            lowerTitle.matches(Regex(".*(gym|fitness|health|pharmacy|doctor).*")) -> "Health"
            lowerTitle.matches(Regex(".*(salary|wage|bonus|dividend).*")) -> "Income"
            else -> null
        }

        return aiPredictedCategory ?: "Miscellaneous"
    }

    /**
     * Allows the user to correct the AI, which could be used to re-train or fine-tune.
     */
    fun logUserCorrection(title: String, correctCategory: String) {
        // Log this to a local dataset for future fine-tuning
    }
}
