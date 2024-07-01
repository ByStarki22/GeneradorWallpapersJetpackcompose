package es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.ui.ImageGeneratorViewModel

class ImageGeneratorViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageGeneratorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageGeneratorViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
