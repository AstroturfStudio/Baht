package studio.astroturf.baht.ui.randomizer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.R

@Composable
fun PremiumRandomizerItem(
    @DrawableRes imageRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xFFFFF8E1),
                                    Color(0xFFFFF3C4),
                                ),
                        ),
                ).border(
                    width = 2.dp,
                    brush =
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFFA000),
                                ),
                        ),
                    shape = RoundedCornerShape(16.dp),
                ).padding(16.dp),
    ) {
        // Premium Badge and Image
        Box {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.FillWidth,
            )

            // PRO Badge
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            brush =
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color(0xFFFFD700),
                                            Color(0xFFFFA000),
                                        ),
                                ),
                            shape = RoundedCornerShape(8.dp),
                        ).padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "PRO",
                    style =
                        TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            style =
                TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 23.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF8B5CF6),
                ),
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = description,
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF61758A),
                    ),
                modifier = Modifier.weight(1f).padding(end = 12.dp),
            )

            Row(
                modifier =
                    Modifier
                        .height(32.dp)
                        .background(
                            brush =
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color(0xFF8B5CF6),
                                            Color(0xFF7C3AED),
                                        ),
                                ),
                            shape = RoundedCornerShape(size = 12.dp),
                        ).clickable { onClick() }
                        .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Start",
                    style =
                        TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun PremiumRandomizerItemPreview() {
    PremiumRandomizerItem(
        imageRes = R.drawable.random_pot,
        title = "Tournament",
        description = "Organize various tournaments",
        onClick = {},
    )
} 
