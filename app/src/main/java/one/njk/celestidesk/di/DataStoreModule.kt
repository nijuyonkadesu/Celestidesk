package one.njk.celestidesk.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import one.njk.celestidesk.data.RolesDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideRolesDatastore(
        @ApplicationContext context: Context,
    ): RolesDataStore = RolesDataStore(context)

}