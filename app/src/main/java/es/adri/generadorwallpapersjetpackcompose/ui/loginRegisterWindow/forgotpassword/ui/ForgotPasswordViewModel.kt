package es.adri.generadorwallpapersjetpackcompose.ui.loginRegisterWindow.forgotpassword.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel(){

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _sendMailEnable = MutableLiveData<Boolean>()
    val sendMailEnable : LiveData<Boolean> = _sendMailEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onSendEmailChange (email:String){

        _email.value = email
        _sendMailEnable.value = isValidEmail(email)

    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun RecoveryPassword(home: () -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        try {
            auth.sendPasswordResetEmail(_email.value!!)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        Log.d("Envio", "RecoveryPassword: logueado!!")
                        showToastMessage("Correo enviado")
                        home()
                    } else {
                        Log.d("Envio", "RecoveryPassword: ${task.exception?.message}")
                    }
                }
        } catch (ex: Exception) {
            _isLoading.value = false
            Log.d("Envio", "RecoveryPassword: ${ex.message}")
        }
    }

    fun showToastMessage(message: String) {
        _showToast.value = message
    }

}