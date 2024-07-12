package me.oikvpqya.apps.music.coil.di

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import me.tatarka.inject.annotations.Provides

interface CoilComponent {

    val imageLoader: ImageLoader

    @Provides
    fun provideImageLoader(
        application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
//        .logger(DebugLogger())
        .build()
}
