package es.adri.generadorwallpapersjetpackcompose.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui.LoginViewModel

class LoginViewModelProvider : PreviewParameterProvider<LoginViewModel> {
    override val values = sequenceOf(
        LoginViewModel().apply {
        }
    )
}