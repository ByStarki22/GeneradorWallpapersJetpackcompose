package es.adri.generadorwallpapersjetpackcompose.ui.optionselector.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import es.adri.generadorwallpapersjetpackcompose.navigation.AppScreens
import es.adri.generadorwallpapersjetpackcompose.ui.optionselector.data.OptionSelectorViewModelProvider
import kotlinx.coroutines.delay


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionSelectorScreenPreview(@PreviewParameter(OptionSelectorViewModelProvider::class) viewModel: OptionSelectorViewModel) {
    val navController = rememberNavController()
    OptionSelectorScreen(viewModel , navController = navController)
}
@Composable
fun OptionSelectorScreen(viewModel: OptionSelectorViewModel,navController:NavController){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)){
        OptionSelector(Modifier.align(Alignment.Center), navController)
    }
}

@Composable
fun OptionSelector(modifier: Modifier, navController:NavController){
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.padding(20.dp))
        OptionSelectorImageGeneratorButton(navController)
        Spacer(modifier = Modifier.padding(10.dp))
        OptionSelectorComingSoonButton()
        Spacer(modifier = Modifier.weight(1f))
        SignOutButton(navController = navController)
    }
}

@Composable
fun OptionSelectorImageGeneratorButton(navController: NavController){
    Button(
        onClick = {navController.navigate(route = AppScreens.ImageGeneratorScreen.route)},
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF925904),
            disabledContainerColor = Color(0xFF925904),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Generar Imagenes")
    }
}

@Composable
fun OptionSelectorComingSoonButton(){
    Button(
        onClick = {
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF925904),
            disabledContainerColor = Color(0xFF925904),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Proximamente")
    }
}

@Composable
fun SignOutButton(navController: NavController) {
    val context = LocalContext.current

    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.OptionSelectorScreen.route) {
                    inclusive = true
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF925904),
            disabledContainerColor = Color(0xFF925904),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Cerrar sesión")
    }
}