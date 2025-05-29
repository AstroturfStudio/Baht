package studio.astroturf.baht.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.R

val PlusJakartaSans =
    FontFamily(
        Font(R.font.plus_jakarta_sans, FontWeight.Normal),
        Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
        Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    )

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
    )
