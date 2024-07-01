package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.adri.generadorwallpapersjetpackcompose.R
import es.adri.generadorwallpapersjetpackcompose.navigation.AppScreens
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.data.ForgotPasswordModelProvider
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui.EmailField
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui.HeaderImage
import kotlinx.coroutines.launch


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview(@PreviewParameter(ForgotPasswordModelProvider::class) viewModel: ForgotPasswordViewModel) {
    val navController = rememberNavController()
    ForgotPasswordScreen(viewModel, navController = navController)
}

@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel, navController: NavController){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)){
        ForgotPassword(Modifier.align(Alignment.Center), viewModel, navController)
    }
}
@Composable
fun ForgotPassword(modifier: Modifier, viewModel: ForgotPasswordViewModel, navController:NavController) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val sendMailEnable: Boolean by viewModel.sendMailEnable.observeAsState(initial = false)
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
            EmailField(email) { viewModel.onSendEmailChange(it) }
            Spacer(modifier = Modifier.padding(16.dp))
            SendMailButton(sendMailEnable) {
                coroutineScope.launch {
                    viewModel.RecoveryPassword {
                        navController.navigate(route = AppScreens.LoginScreen.route)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            back(navController)
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
        placeholder = { Text(text = "Correo") },
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
fun SendMailButton(sendMailEnable: Boolean, onLoginSelected:() -> Unit){
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
        enabled = sendMailEnable,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Mandar email")
    }
}

@Composable
fun back(navController:NavController){
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
        Text(text = "Volver")
    }
}