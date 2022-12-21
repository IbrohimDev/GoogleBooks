package com.example.googlebooks.di.network

import android.content.Context
import androidx.paging.PagingConfig
import com.example.googlebooks.BuildConfig
import com.example.googlebooks.data.remote.api.BooksApi
import com.example.googlebooks.util.addLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {

    @[Provides Singleton]
    fun booksApi(retrofit: Retrofit): BooksApi =
        retrofit.create(BooksApi::class.java)

    @[Provides Singleton]
    fun getConfig(): PagingConfig = PagingConfig(10)

    @[Provides Singleton]
    fun getRetrofitNews(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    @[Provides Singleton]
    fun getOkHTTPClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addLoggingInterceptor(context)
            .build()

}