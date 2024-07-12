package me.oikvpqya.apps.music.app

import android.graphics.Color
import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import me.oikvpqya.apps.music.app.di.MainActivityComponent
import me.oikvpqya.apps.music.app.di.applicationComponent
import me.oikvpqya.apps.music.app.di.create

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT).also { style ->
            if (VERSION.SDK_INT < 26) enableEdgeToEdge(style) else enableEdgeToEdge(style, style)
        }
        if (VERSION.SDK_INT >= 29) {
            window.isNavigationBarContrastEnforced = false
        }

        val component = MainActivityComponent::class.create(applicationComponent)

        setContent {
            component.appContent.Content(
                modifier = Modifier,
            )
        }
    }
}
