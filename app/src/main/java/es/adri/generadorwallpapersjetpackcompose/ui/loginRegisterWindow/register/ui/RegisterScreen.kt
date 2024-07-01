package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.adri.generadorwallpapersjetpackcompose.R
import es.adri.generadorwallpapersjetpackcompose.navigation.AppScreens
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.data.RegisterViewModelProvider
import kotlinx.coroutines.launch


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview(@PreviewParameter(RegisterViewModelProvider::class) viewModel: RegisterViewModel) {
    val navController = rememberNavController()
    RegisterScreen(viewModel , navController = navController)
}

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)){
        Register(Modifier.align(Alignment.Center), viewModel, navController)
    }
}
@Composable
fun Register(modifier: Modifier, viewModel: RegisterViewModel, navController: NavController) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val repeatPassword: String by viewModel.repeatPassword.observeAsState(initial = "")
    val registerEnable: Boolean by viewModel.registerEnable.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    val showToastMessage = viewModel.showToast.observeAsState()

    if (!showToastMessage.value.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, showToastMessage.value, Toast.LENGTH_SHORT).show()
        viewModel.showToastMessage("")
    }

    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    if (isLoading) {
        Box(modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier.align(Alignment.Center))
        }
    } else {

        Column(modifier = modifier) {
            HeaderImage(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onRegisterChanged(it, password, repeatPassword) }
            Spacer(modifier = Modifier.padding(5.dp))
            PasswordField(password) { viewModel.onRegisterChanged(email, it, repeatPassword) }
            Spacer(modifier = Modifier.padding(5.dp))
            RepeatPasswordField(repeatPassword) { viewModel.onRegisterChanged(email, password, it) }
            Spacer(modifier = Modifier.padding(32.dp))
            RegisterButton(registerEnable) {
                coroutineScope.launch {
                    viewModel.createUserWithEmailAndPassword {
                        navController.navigate(route = AppScreens.LoginScreen.route)
                    }}}
            Spacer(modifier = Modifier.padding(5.dp))
            RegisterLoginButton(navController)
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
fun RepeatPasswordField(repeatPassword: String, onTextFieldChange:(String) -> Unit){
    TextField(
        value = repeatPassword,
        onValueChange = {onTextFieldChange(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Repetir contraseña")},
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
fun RegisterButton(registerEnable: Boolean, onRegisterSelected:() -> Unit){
    Button(
        onClick = { onRegisterSelected () },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF925904),
            disabledContainerColor = Color(0xFF925904),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFFC2C1BB)
        ),
        enabled = registerEnable,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Crear cuenta")
    }
}

@Composable
fun RegisterLoginButton(navController:NavController){
    Button(
        onClick = {
            navController.popBackStack()
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
        Text(text = "¿Ya tienes cuenta? Dale click aqui")
    }
}