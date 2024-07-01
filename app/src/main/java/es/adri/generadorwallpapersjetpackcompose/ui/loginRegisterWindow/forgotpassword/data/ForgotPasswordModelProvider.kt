package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.ui.ForgotPasswordViewModel

class ForgotPasswordModelProvider : PreviewParameterProvider<ForgotPasswordViewModel> {
    override val values = sequenceOf(
        ForgotPasswordViewModel().apply {
        }
    )
}