package one.njk.celestidesk.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import one.njk.celestidesk.data.RolesDataStore
import one.njk.celestidesk.database.RequestDatabase
import one.njk.celestidesk.network.ApiService
import one.njk.celestidesk.network.auth.AuthRepository
import one.njk.celestidesk.network.auth.AuthRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "https://celestidesk.onrender.com/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): ApiService {
        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService, pref: RolesDataStore): AuthRepository {
        return AuthRepositoryImpl(api, pref)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)
    = Room.databaseBuilder(
        context = context,
        RequestDatabase::class.java, "celestidesk"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideRequestsDao(db: RequestDatabase) = db.requestsDao
}
