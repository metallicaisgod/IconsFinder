package com.kirillm.iconsapi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kirillm.iconsapi.models.IconDTO
import com.kirillm.iconsapi.models.ResponseDTO
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface IconsApi {

    @GET("search")
    suspend fun getIcons(
        @Query("query") query: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int
    ): Result<ResponseDTO<IconDTO>>
}

fun IconsApi(
    baseUrl: String,
//    apiKey: String? = null,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json { ignoreUnknownKeys = true },
): IconsApi {
    return retrofit(baseUrl, /*apiKey, */okHttpClient, json).create()
}

private fun retrofit(
    baseUrl: String,
//    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val modifiedOkHttpClient =
        (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
           // .addInterceptor(NewsApiKeyInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY})
            .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}