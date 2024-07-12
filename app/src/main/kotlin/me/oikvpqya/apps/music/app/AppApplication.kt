package me.oikvpqya.apps.music.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import me.oikvpqya.apps.music.app.di.ApplicationComponent
import me.oikvpqya.apps.music.app.di.ApplicationComponentProvider
import me.oikvpqya.apps.music.app.di.create

class AppApplication : Application(), ApplicationComponentProvider, ImageLoaderFactory {

    override val component by lazy(LazyThreadSafetyMode.NONE) {
        ApplicationComponent::class.create(this)
    }

    override fun newImageLoader(): ImageLoader = component.imageLoader
}
