package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.register.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword : LiveData<String> = _repeatPassword

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable : LiveData<Boolean> = _registerEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun onRegisterChanged(email: String, password: String, repeatPassword: String){

        _email.value = email
        _password.value = password
        _repeatPassword.value = repeatPassword
        _registerEnable.value = isValidEmail(email) && isValidPassword(password) && isValidRepeatPassword(repeatPassword)

    }

    private fun isValidPassword(password: String): Boolean = password.length > 6

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidRepeatPassword(repeatPassword: String): Boolean = repeatPassword == password.value


    fun createUserWithEmailAndPassword(home: () -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        try {
            auth.createUserWithEmailAndPassword(_email.value!!, _password.value!!)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        Log.d("Logueo", "createUserWithEmailAndPassword: registrado exitosamente")
                        showToastMessage("Cuenta creada exitosamente")
                        home()
                    } else {
                        Log.d("Logueo", "createUserWithEmailAndPassword: ${task.exception?.message}")
                    }
                }
        } catch (ex: Exception) {
            _isLoading.value = false
            Log.d("Logueo", "createUserWithEmailAndPassword: ${ex.message}")
        }
    }

    fun showToastMessage(message: String) {
        _showToast.value = message
    }

}
