package com.example.taskplannerapp.dependenciesinjection


import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.taskplannerapp.BuildConfig
import com.example.taskplannerapp.data.AppDatabase
import com.example.taskplannerapp.data.dao.TaskDao
import com.example.taskplannerapp.service.AuthService
import com.example.taskplannerapp.service.JWTInterceptor
import com.example.taskplannerapp.service.TaskService
import com.example.taskplannerapp.storage.LocalStorage
import com.example.taskplannerapp.storage.SharedPreferencesLocalStorage
import com.example.taskplannerapp.utils.DATABASE_NAME
import com.example.taskplannerapp.utils.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME
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
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesLocalStorage(@ApplicationContext context: Context): LocalStorage{
        //return SharedPreferencesLocalStorage(context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE))
        //Provide secure shared preferences
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return SharedPreferencesLocalStorage(EncryptedSharedPreferences.create(
            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ))

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
            .baseUrl(BuildConfig.TASK_API_BASE_URL)
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

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    fun providesTaskDao(appDatabase: AppDatabase): TaskDao{
        return appDatabase.taskDao()
    }
}