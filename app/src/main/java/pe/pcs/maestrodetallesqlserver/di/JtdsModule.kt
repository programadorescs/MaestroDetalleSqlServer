package pe.pcs.maestrodetallesqlserver.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.pcs.maestrodetallesqlserver.data.database.JtdsConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JtdsModule {

    @Singleton
    @Provides
    fun provideJtdsConnection(): JtdsConnection {
        return JtdsConnection()
    }

}