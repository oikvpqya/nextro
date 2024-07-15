package me.oikvpqya.apps.music.app.di

import me.oikvpqya.apps.music.ui.di.UiComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope

@Scope
annotation class ActivityScope

@ActivityScope
@Component
abstract class MainActivityComponent(
//    @get:Provides val activity: Activity,
    @Component val applicationComponent: ApplicationComponent,
) : UiComponent