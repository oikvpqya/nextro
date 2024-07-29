package me.oikvpqya.apps.music.ui.component.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.AppIcons

val AppIcons.TrendingUp: ImageVector
    get() {
        if (_TrendingUp != null) {
            return _TrendingUp!!
        }
        _TrendingUp = ImageVector.Builder(
            name = "TrendingUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(2.7f, 17.3f)
                quadTo(2.425f, 17.025f, 2.425f, 16.612f)
                quadTo(2.425f, 16.2f, 2.7f, 15.9f)
                lineTo(8.7f, 9.85f)
                quadTo(8.85f, 9.725f, 9.025f, 9.65f)
                quadTo(9.2f, 9.575f, 9.4f, 9.575f)
                quadTo(9.6f, 9.575f, 9.788f, 9.65f)
                quadTo(9.975f, 9.725f, 10.1f, 9.85f)
                lineTo(13.4f, 13.15f)
                lineTo(18.6f, 8f)
                horizontalLineTo(17f)
                quadTo(16.575f, 8f, 16.288f, 7.713f)
                quadTo(16f, 7.425f, 16f, 7f)
                quadTo(16f, 6.575f, 16.288f, 6.287f)
                quadTo(16.575f, 6f, 17f, 6f)
                horizontalLineTo(21f)
                quadTo(21.425f, 6f, 21.712f, 6.287f)
                quadTo(22f, 6.575f, 22f, 7f)
                verticalLineTo(11f)
                quadTo(22f, 11.425f, 21.712f, 11.712f)
                quadTo(21.425f, 12f, 21f, 12f)
                quadTo(20.575f, 12f, 20.288f, 11.712f)
                quadTo(20f, 11.425f, 20f, 11f)
                verticalLineTo(9.4f)
                lineTo(14.1f, 15.3f)
                quadTo(13.95f, 15.45f, 13.775f, 15.512f)
                quadTo(13.6f, 15.575f, 13.4f, 15.575f)
                quadTo(13.2f, 15.575f, 13.025f, 15.512f)
                quadTo(12.85f, 15.45f, 12.7f, 15.3f)
                lineTo(9.4f, 12f)
                lineTo(4.075f, 17.325f)
                quadTo(3.8f, 17.6f, 3.4f, 17.6f)
                quadTo(3f, 17.6f, 2.7f, 17.3f)
                close()
            }
        }.build()

        return _TrendingUp!!
    }

private var _TrendingUp: ImageVector? = null
