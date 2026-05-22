package com.example.expensetrackerai.di

import android.content.Context
import androidx.room.Room
import com.example.expensetrackerai.core.database.ExpenseDatabase
import com.example.expensetrackerai.core.security.EncryptionManager
import com.example.expensetrackerai.data.local.dao.BudgetDao
import com.example.expensetrackerai.data.local.dao.TransactionDao
import com.example.expensetrackerai.data.local.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        encryptionManager: EncryptionManager
    ): ExpenseDatabase {
        val passphrase = encryptionManager.getPassphrase().toByteArray()
        val factory = SupportFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: ExpenseDatabase): TransactionDao = database.transactionDao()

    @Provides
    @Singleton
    fun provideWalletDao(database: ExpenseDatabase): WalletDao = database.walletDao()

    @Provides
    @Singleton
    fun provideBudgetDao(database: ExpenseDatabase): BudgetDao = database.budgetDao()
}
