package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.login.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onLoginChanged (email:String , password:String){

        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)

    }

    private fun isValidPassword(password: String): Boolean = password.length > 6

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun signInWithEmailAndPassword(home: () -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        try {
            auth.signInWithEmailAndPassword(_email.value!!, _password.value!!)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        Log.d("Logueo", "signInWithEmailAndPassword: logueado!!")
                        showToastMessage("Inicio de sesion exitoso")
                        home()
                    } else {
                        Log.d("Logueo", "signInWithEmailAndPassword: ${task.exception?.message}")
                        showToastMessage("Correo o contraseña incorrectos")
                    }
                }
        } catch (ex: Exception) {
            _isLoading.value = false
            Log.d("Logueo", "signInWithEmailAndPassword: ${ex.message}")
        }
    }

    fun showToastMessage(message: String) {
        _showToast.value = message
    }

}