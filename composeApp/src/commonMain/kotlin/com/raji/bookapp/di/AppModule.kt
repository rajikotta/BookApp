package com.raji.bookapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.raji.bookapp.data.BookRepositoryImpl
import com.raji.bookapp.data.local.BookDatabase
import com.raji.bookapp.data.local.DatabaseFactory
import com.raji.bookapp.data.remote.RemoteDataSource
import com.raji.bookapp.data.remote.RemoteDataSourceImpl
import com.raji.bookapp.data.remote.network.HttpClientFactory
import com.raji.bookapp.domain.BookRepository
import com.raji.bookapp.presentation.bookdetail.BookDetailViewModel
import com.raji.bookapp.presentation.booklist.BookListViewModel
import com.raji.bookapp.presentation.booklist.SharedViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    viewModelOf(::BookListViewModel)
    viewModelOf(::SharedViewModel)
    singleOf(::BookRepositoryImpl).bind<BookRepository>()
    singleOf(::RemoteDataSourceImpl).bind<RemoteDataSource>()
    viewModelOf(::BookDetailViewModel)

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<BookDatabase>().bookDao }
}