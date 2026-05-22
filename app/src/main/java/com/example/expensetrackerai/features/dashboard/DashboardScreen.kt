package com.example.expensetrackerai.features.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetrackerai.core.ui.FintechCard
import com.example.expensetrackerai.core.ui.SectionHeader
import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.model.TransactionType
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onAddTransactionClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            "Welcome back,", 
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Vikas", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAnalyticsClick) {
                        Icon(
                            Icons.Default.BarChart, 
                            contentDescription = "Analytics",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Success -> {
                    DashboardContent(state)
                }
                is DashboardUiState.Error -> {
                    Text(text = state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun DashboardContent(state: DashboardUiState.Success) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BalanceCard(state.totalBalance, state.monthlyIncome, state.monthlyExpense)
        }
        
        item {
            QuickActionsRow()
        }

        item {
            FinancialHealthCard(state.healthScore)
        }
        
        item {
            SpendingTrendCard()
        }
        
        item {
            BudgetCard(0.65f)
        }

        item {
            AIInsightCard(state.aiInsight)
        }

        item {
            SectionHeader(title = "Recent Activity", actionText = "View All", onActionClick = {})
        }

        items(state.transactions.take(5)) { transaction ->
            TransactionItem(transaction)
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun QuickActionsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionItem(Icons.Default.QrCodeScanner, "Scan", Modifier.weight(1f))
        QuickActionItem(Icons.Default.TrendingUp, "Invest", Modifier.weight(1f))
        QuickActionItem(Icons.Default.Savings, "Goals", Modifier.weight(1f))
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    FintechCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun FinancialHealthCard(score: Int) {
    FintechCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Financial Health", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = when {
                        score >= 80 -> "Excellent"
                        score >= 60 -> "Good"
                        else -> "Needs Attention"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = when {
                        score >= 80 -> Color(0xFF10B981)
                        score >= 60 -> Color(0xFFF59E0B)
                        else -> Color(0xFFEF4444)
                    }
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Text(
                    text = "$score", 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SpendingTrendCard() {
    FintechCard {
        Column {
            Text("Spending Trend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                val path = Path()
                val points = listOf(0.1f, 0.4f, 0.3f, 0.7f, 0.5f, 0.9f, 0.8f)
                val width = size.width
                val height = size.height
                val stepX = width / (points.size - 1)

                path.moveTo(0f, height - (points[0] * height))
                for (i in 1 until points.size) {
                    path.lineTo(i * stepX, height - (points[i] * height))
                }
                
                drawPath(
                    path = path,
                    color = Color(0xFF38BDF8),
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }
    }
}

@Composable
fun BudgetCard(progress: Float) {
    FintechCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Monthly Budget", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$1,200 of $2,000 spent", 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun AIInsightCard(insight: String) {
    FintechCard(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AutoAwesome, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = insight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun BalanceCard(totalBalance: Double, income: Double, expense: Double) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

    FintechCard(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Total Balance", 
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Text(
                currencyFormatter.format(totalBalance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BalanceInfoItem("Income", currencyFormatter.format(income), Color(0xFF10B981), MaterialTheme.colorScheme.onPrimary)
                BalanceInfoItem("Expense", currencyFormatter.format(expense), Color(0xFFEF4444), MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun BalanceInfoItem(label: String, amount: String, indicatorColor: Color, textColor: Color) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(indicatorColor, RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = textColor.copy(alpha = 0.7f))
        }
        Text(amount, style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val amountColor = if (transaction.type == TransactionType.INCOME) Color(0xFF10B981) else Color(0xFFEF4444)
    val amountPrefix = if (transaction.type == TransactionType.INCOME) "+" else "-"

    FintechCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Text(
                "$amountPrefix${currencyFormatter.format(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = amountColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
