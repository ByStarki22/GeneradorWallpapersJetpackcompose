package es.adri.generadorwallpapersjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.data.ImageGeneratorViewModelFactory
import es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.ui.ImageGeneratorScreen
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.ui.ForgotPasswordScreen
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.ui.ForgotPasswordViewModel
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui.LoginScreen
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui.LoginViewModel
import es.adri.generadorwallpapersjetpackcompose.ui.optionselector.ui.OptionSelectorScreen
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.ui.RegisterScreen
import es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.ui.RegisterViewModel
import es.adri.generadorwallpapersjetpackcompose.ui.optionselector.ui.OptionSelectorViewModel

@Composable
fun AppNavigation(startDestination: String){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = AppScreens.LoginScreen.route){
            LoginScreen(viewModel = remember { LoginViewModel() }, navController = navController)
        }
        composable(route = AppScreens.RegisterScreen.route){
            RegisterScreen(viewModel = remember { RegisterViewModel() }, navController = navController)
        }
        composable(route = AppScreens.ForgotPasswordScreen.route){
            ForgotPasswordScreen(viewModel = remember { ForgotPasswordViewModel() }, navController = navController)
        }
        composable(route = AppScreens.OptionSelectorScreen.route){
            OptionSelectorScreen(viewModel = remember { OptionSelectorViewModel() }, navController = navController)
        }
        composable(route = AppScreens.ImageGeneratorScreen.route){
            val factory = ImageGeneratorViewModelFactory(LocalContext.current)
            ImageGeneratorScreen(viewModel = viewModel(factory = factory))
        }
    }
}