package com.dnl.exchangeratetracker.di

import android.app.Application
import androidx.room.Room
import com.dnl.exchangeratetracker.data.ExchangeRateRepositoryImpl
import com.dnl.exchangeratetracker.data.local.CurrencyExchangeDatabase
import com.dnl.exchangeratetracker.data.local.FavoritesLocalDataSource
import com.dnl.exchangeratetracker.data.local.RatesLocalDataSource
import com.dnl.exchangeratetracker.data.remote.OpenExchangeRatesApi
import com.dnl.exchangeratetracker.domain.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://openexchangerates.org/api/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOpenExchangeApi(okHttpClient: OkHttpClient): OpenExchangeRatesApi {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(OpenExchangeRatesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        ratesLocalDataSource: RatesLocalDataSource,
        favoritesLocalDataSource: FavoritesLocalDataSource,
        api: OpenExchangeRatesApi
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(ratesLocalDataSource, favoritesLocalDataSource, api)
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(app: Application): CurrencyExchangeDatabase {
        return Room.databaseBuilder(
            app,
            CurrencyExchangeDatabase::class.java,
            CurrencyExchangeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoritesDataSource(db: CurrencyExchangeDatabase) =
        FavoritesLocalDataSource(db.favoritesDao())

    @Provides
    @Singleton
    fun provideRatesDataSource(db: CurrencyExchangeDatabase) =
        RatesLocalDataSource(db.exchangeRateDao())
}