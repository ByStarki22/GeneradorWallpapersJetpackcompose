package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.ui.RegisterViewModel

class RegisterViewModelProvider : PreviewParameterProvider<RegisterViewModel> {
    override val values = sequenceOf(
        RegisterViewModel().apply {
        }
    )
}