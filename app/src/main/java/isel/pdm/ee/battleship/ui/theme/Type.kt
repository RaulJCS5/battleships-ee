package isel.pdm.ee.battleship.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import isel.pdm.ee.battleship.R

// license: Freeware
// link: https://www.fontspace.com/sunny-spells-font-f68393
// license: Freeware, commercial use requires donation
// link: https://www.fontspace.com/octopus-game-font-f103153
// https://www.fontspace.com/rusty-hooks-font-f87577

private val Battleship = FontFamily(
    Font(R.font.octopusgame_rpwv3)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Battleship,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Battleship,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Battleship,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
)