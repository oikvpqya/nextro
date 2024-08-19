package me.oikvpqya.apps.music.ui.component.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.AppIcons

val AppIcons.History: ImageVector
    get() {
        if (_History != null) {
            return _History!!
        }
        _History = ImageVector.Builder(
            name = "History",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(13f, 11.6f)
                lineTo(15.5f, 14.1f)
                quadTo(15.775f, 14.375f, 15.775f, 14.8f)
                quadTo(15.775f, 15.225f, 15.5f, 15.5f)
                quadTo(15.225f, 15.775f, 14.8f, 15.775f)
                quadTo(14.375f, 15.775f, 14.1f, 15.5f)
                lineTo(11.3f, 12.7f)
                quadTo(11.15f, 12.55f, 11.075f, 12.362f)
                quadTo(11f, 12.175f, 11f, 11.975f)
                verticalLineTo(8f)
                quadTo(11f, 7.575f, 11.288f, 7.287f)
                quadTo(11.575f, 7f, 12f, 7f)
                quadTo(12.425f, 7f, 12.713f, 7.287f)
                quadTo(13f, 7.575f, 13f, 8f)
                close()
                moveTo(12f, 21f)
                quadTo(8.975f, 21f, 6.575f, 19.212f)
                quadTo(4.175f, 17.425f, 3.35f, 14.55f)
                quadTo(3.225f, 14.1f, 3.438f, 13.7f)
                quadTo(3.65f, 13.3f, 4.1f, 13.2f)
                quadTo(4.525f, 13.1f, 4.863f, 13.387f)
                quadTo(5.2f, 13.675f, 5.325f, 14.1f)
                quadTo(5.975f, 16.3f, 7.838f, 17.65f)
                quadTo(9.7f, 19f, 12f, 19f)
                quadTo(14.925f, 19f, 16.962f, 16.962f)
                quadTo(19f, 14.925f, 19f, 12f)
                quadTo(19f, 9.075f, 16.962f, 7.037f)
                quadTo(14.925f, 5f, 12f, 5f)
                quadTo(10.275f, 5f, 8.775f, 5.8f)
                quadTo(7.275f, 6.6f, 6.25f, 8f)
                horizontalLineTo(8f)
                quadTo(8.425f, 8f, 8.713f, 8.287f)
                quadTo(9f, 8.575f, 9f, 9f)
                quadTo(9f, 9.425f, 8.713f, 9.712f)
                quadTo(8.425f, 10f, 8f, 10f)
                horizontalLineTo(4f)
                quadTo(3.575f, 10f, 3.288f, 9.712f)
                quadTo(3f, 9.425f, 3f, 9f)
                verticalLineTo(5f)
                quadTo(3f, 4.575f, 3.288f, 4.287f)
                quadTo(3.575f, 4f, 4f, 4f)
                quadTo(4.425f, 4f, 4.713f, 4.287f)
                quadTo(5f, 4.575f, 5f, 5f)
                verticalLineTo(6.35f)
                quadTo(6.275f, 4.75f, 8.113f, 3.875f)
                quadTo(9.95f, 3f, 12f, 3f)
                quadTo(13.875f, 3f, 15.513f, 3.712f)
                quadTo(17.15f, 4.425f, 18.363f, 5.637f)
                quadTo(19.575f, 6.85f, 20.288f, 8.487f)
                quadTo(21f, 10.125f, 21f, 12f)
                quadTo(21f, 13.875f, 20.288f, 15.512f)
                quadTo(19.575f, 17.15f, 18.363f, 18.362f)
                quadTo(17.15f, 19.575f, 15.513f, 20.288f)
                quadTo(13.875f, 21f, 12f, 21f)
                close()
            }
        }.build()

        return _History!!
    }

private var _History: ImageVector? = null
