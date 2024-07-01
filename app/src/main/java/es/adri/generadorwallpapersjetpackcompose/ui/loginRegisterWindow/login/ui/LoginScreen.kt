package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.adri.generadorwallpapersjetpackcompose.R
import es.adri.generadorwallpapersjetpackcompose.data.LoginViewModelProvider
import es.adri.generadorwallpapersjetpackcompose.navigation.AppScreens
import kotlinx.coroutines.launch


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(@PreviewParameter(LoginViewModelProvider::class) viewModel: LoginViewModel) {
    val navController = rememberNavController()
    LoginScreen(viewModel, navController = navController)
}

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController:NavController){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)){
        Login(Modifier.align(Alignment.Center), viewModel, navController)
    }
}
@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController:NavController) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    val showToastMessage = viewModel.showToast.observeAsState()

    if (!showToastMessage.value.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, showToastMessage.value, Toast.LENGTH_SHORT).show()
        viewModel.showToastMessage("")
    }

    if (isLoading) {
        Box(modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier.align(Alignment.Center))
        }
    } else {

        Column(modifier = modifier) {
            HeaderImage(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(5.dp))
            PasswordField(password) { viewModel.onLoginChanged(email, it) }
            Spacer(modifier = Modifier.padding(16.dp))
            ForgotPassword(Modifier.align(Alignment.End)){
                navController.navigate(route = AppScreens.ForgotPasswordScreen.route)}
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(loginEnable) {
                coroutineScope.launch {
                    viewModel.signInWithEmailAndPassword {
                        navController.navigate(route = AppScreens.OptionSelectorScreen.route)
                    }}}
            Spacer(modifier = Modifier.padding(5.dp))
            LoginRegisterButton(navController)
        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier){
    Image(painter = painterResource(id = R.drawable.logo),
        contentDescription = "header",
        modifier = modifier.size(300.dp)
    )
}

@Composable
fun EmailField(email: String, onTextFieldChange:(String) -> Unit){

    TextField(
        value = email,
        onValueChange = {onTextFieldChange(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Correo")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF925904),
            unfocusedTextColor = Color(0xFF925904),
            focusedContainerColor =  Color(0xFFC2C1BB),
            unfocusedContainerColor = Color(0xFFC2C1BB)

        )
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChange:(String) -> Unit){
    TextField(
        value = password,
        onValueChange = {onTextFieldChange(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF925904),
            unfocusedTextColor = Color(0xFF925904),
            focusedContainerColor =  Color(0xFFC2C1BB),
            unfocusedContainerColor = Color(0xFFC2C1BB)

        )
    )
}

@Composable
fun ForgotPassword(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { onClick() },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000DFF)
    )
}
@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected:() -> Unit){
    Button(
        onClick = { onLoginSelected () },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF925904),
            disabledContainerColor = Color(0xFF925904),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFFC2C1BB)
        ),
        enabled = loginEnable,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Inciar Sesion")
    }
}

@Composable
fun LoginRegisterButton(navController:NavController){
    Button(
        onClick = { navController.navigate(route = AppScreens.RegisterScreen.route) },
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
        Text(text = "¿No tienes cuenta? Dale click aqui")
    }
}