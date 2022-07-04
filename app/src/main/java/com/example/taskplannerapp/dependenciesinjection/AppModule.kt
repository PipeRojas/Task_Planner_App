package com.example.taskplannerapp.dependenciesinjection


import android.content.Context
import android.content.SharedPreferences
import com.example.taskplannerapp.service.AuthService
import com.example.taskplannerapp.service.JWTInterceptor
import com.example.taskplannerapp.service.TaskService
import com.example.taskplannerapp.storage.LocalStorage
import com.example.taskplannerapp.storage.SharedPreferencesLocalStorage
import com.example.taskplannerapp.utils.SHARED_PREFERENCES_FILE_NAME
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import kotlin.text.Typography.dagger

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesLocalStorage(@ApplicationContext context: Context): LocalStorage{
        return SharedPreferencesLocalStorage(context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE))
    }

    @Provides
    fun provideJWTInterceptor(localStorage: LocalStorage): JWTInterceptor{
        return JWTInterceptor(localStorage)
    }

    @Provides
    fun providesRetrofit(jwtInterceptor: JWTInterceptor): Retrofit{
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(jwtInterceptor)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES).build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create()

        return Retrofit.Builder()
            .baseUrl("https://tasks-planner-api.herokuapp.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    fun providesTaskService(retrofit: Retrofit):TaskService{
        return retrofit.create(TaskService::class.java)
    }

    @Provides
    fun providesAuthService(retrofit: Retrofit):AuthService{
        return retrofit.create(AuthService::class.java)
    }
}