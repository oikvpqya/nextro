package me.oikvpqya.apps.music.coil.di

import android.content.Context
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.util.DebugLogger
import me.tatarka.inject.annotations.Provides

interface CoilComponent {

    val imageLoader: ImageLoader

    @Provides
    fun provideImageLoader(
        application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .memoryCache {
            MemoryCache.Builder(application)
                .maxSizePercent(0.25)
                .build()
        }
//        .logger(DebugLogger())
        .build()
}
