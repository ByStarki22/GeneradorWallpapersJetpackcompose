package es.adri.generadorwallpapersjetpackcompose.navigation

sealed class AppScreens(val route: String) {

    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")
    object ForgotPasswordScreen: AppScreens("forgotpassword_screen")
    object OptionSelectorScreen: AppScreens("optionselector_screen")
    object ImageGeneratorScreen: AppScreens("imagegeenrator_screen")
}