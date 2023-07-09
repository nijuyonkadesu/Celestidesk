package one.njk.celestidesk.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import one.njk.celestidesk.data.RolesDataStore
import one.njk.celestidesk.data.auth.AuthApi
import one.njk.celestidesk.data.auth.AuthRepository
import one.njk.celestidesk.data.auth.AuthRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "https://celestidesk.onrender.com/api/employee/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
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
    fun provideAuthRepository(api: AuthApi, pref: RolesDataStore): AuthRepository{
        return AuthRepositoryImpl(api, pref)
    }

}
