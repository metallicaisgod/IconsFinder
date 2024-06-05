package com.kirillm.iconsfinder

import com.kirillm.iconsapi.IconsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIconsApi(okHttpClient: OkHttpClient?): IconsApi = IconsApi(
        okHttpClient = okHttpClient,
        baseUrl = BuildConfig.ICONS_API_BASE_URL,
    )
}