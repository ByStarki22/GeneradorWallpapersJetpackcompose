package es.adri.generadorwallpapersjetpackcompose.ui.paginabienvenida

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaginaBienvenidaScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)
    ) {
        PaginaBienvenida(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun PaginaBienvenida(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        WelcomeText()
    }
}

@Composable
fun WelcomeText() {
    Text(
        text = "AutoPaper",
        fontSize = 70.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive
    )
}