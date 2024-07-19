package me.oikvpqya.apps.music.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import me.oikvpqya.apps.music.app.di.MainActivityComponent
import me.oikvpqya.apps.music.app.di.applicationComponent
import me.oikvpqya.apps.music.app.di.create

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = MainActivityComponent::class.create(applicationComponent)

        setContent {
            component.appContent.Content(
                modifier = Modifier,
            )
        }
    }
}
