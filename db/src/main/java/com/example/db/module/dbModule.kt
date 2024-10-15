package com.example.db.module

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.AnalyticsRepositoryImpl
import com.example.db.TypingAnalyticsMapper
import com.example.db.dao.TypingEventDao
import com.example.db.dao.WordEventDao
import org.koin.dsl.module
import repositories.TypingAnalyticsRepository

val dbModule = module {
    single { provideDatabase(get()) }
    single<TypingEventDao> { get<AppDatabase>().typingEventDao() }
    single<WordEventDao> { get<AppDatabase>().wordEventDao() }
    single { TypingAnalyticsMapper() }
    single<TypingAnalyticsRepository> { AnalyticsRepositoryImpl(get(), get(), get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "typing_database"
    ).build()
}