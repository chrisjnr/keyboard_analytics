package com.example.db.module

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.TypingAnalyticsMapper
import com.example.db.dao.TypingEventDao
import com.example.db.dao.WordEventDao
import org.koin.dsl.module

val dbModule = module {
    single { provideDatabase(get()) }
    single<TypingEventDao> { get<AppDatabase>().typingEventDao() }
    single<WordEventDao> { get<AppDatabase>().wordEventDao() }
    single { TypingAnalyticsMapper() }
    single<repositories.TypingAnalyticsRepository> { com.example.db.TypingAnalyticsRepository(get(), get(), get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "typing_database"
    ).build()
}