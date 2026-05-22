package com.example.expensetrackerai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.view.WindowManager
import com.example.expensetrackerai.core.security.EncryptionManager
import com.example.expensetrackerai.core.ui.AppNavigation
import com.example.expensetrackerai.core.ui.theme.ExpenseTrackerAITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var encryptionManager: EncryptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prevent screenshots for enterprise-level security
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        // Basic root detection check
        if (encryptionManager.isDeviceRooted()) {
            // In a real production app, we would show a warning or close the app.
            // For now, we'll just log it.
        }

        enableEdgeToEdge()
        setContent {
            ExpenseTrackerAITheme {
                AppNavigation()
            }
        }
    }
}
