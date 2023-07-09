package one.njk.celestidesk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import one.njk.celestidesk.data.RolesDataStore
import one.njk.celestidesk.data.auth.AuthApi
import one.njk.celestidesk.data.auth.AuthRepositoryImpl
import one.njk.celestidesk.data.auth.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, pref: RolesDataStore): AuthRepositoryImpl{
        return AuthRepositoryImpl(api, pref)
    }

}
