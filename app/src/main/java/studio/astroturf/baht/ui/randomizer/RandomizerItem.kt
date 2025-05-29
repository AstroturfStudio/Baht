package studio.astroturf.baht.ui.randomizer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun RandomizerItem(
    @DrawableRes imageRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
    ) {
        // Image with 200.dp height
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.FillWidth,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            style =
                TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 23.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF121417),
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
                        .background(color = Color(0xFF3D99F5), shape = RoundedCornerShape(size = 12.dp))
                        .clickable { onClick() }
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
fun RandomizerItemPreview() {
    RandomizerItem(
        imageRes = R.drawable.random_pot,
        title = "Yazı Tura/Zar",
        description = "Simulate a coin flip or roll a die.",
        onClick = {},
    )
}
