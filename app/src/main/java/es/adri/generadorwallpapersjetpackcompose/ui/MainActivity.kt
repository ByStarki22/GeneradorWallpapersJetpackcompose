package es.adri.generadorwallpapersjetpackcompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import es.adri.generadorwallpapersjetpackcompose.navigation.AppNavigation
import es.adri.generadorwallpapersjetpackcompose.navigation.AppScreens
import es.adri.generadorwallpapersjetpackcompose.ui.paginabienvenida.PaginaBienvenidaScreen
import es.adri.generadorwallpapersjetpackcompose.ui.theme.GeneradorWallpapersJetpackcomposeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeneradorWallpapersJetpackcomposeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    var showWelcomeScreen by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf(AppScreens.LoginScreen.route) }

    LaunchedEffect(key1 = true) {
        delay(2000)
        showWelcomeScreen = false

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        startDestination = if (currentUser != null) {
            AppScreens.OptionSelectorScreen.route
        } else {
            AppScreens.LoginScreen.route
        }
    }

    Surface {
        Crossfade(
            targetState = showWelcomeScreen,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        ) { showing ->
            if (showing) {
                PaginaBienvenidaScreen()
            } else {
                AppNavigation(startDestination = startDestination)
            }
        }
    }
}
