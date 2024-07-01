package es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.ui

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ImageGeneratorViewModel(private val context: Context) : ViewModel() {

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val stringURLEndPoint = "https://api.openai.com/v1/images/generations"
    private val stringAPIKey = "Your OpenAi API"

    fun buttonAction(prompt: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("prompt", prompt)
            jsonObject.put("n", 1) // Número de imágenes a generar
            jsonObject.put("size", "1024x1792") // Tamaño de la imagen
            jsonObject.put("model", "dall-e-3") // Modelo a utilizar
        } catch (e: JSONException) {
            _error.value = "Error creating JSON request"
            return
        }

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            stringURLEndPoint,
            jsonObject,
            Response.Listener { response ->
                try {
                    val imageUrl = response.getJSONArray("data").getJSONObject(0).getString("url")
                    fetchImageBitmap(imageUrl)
                } catch (e: JSONException) {
                    _error.value = "Error parsing response"
                }
            },
            Response.ErrorListener { error ->
                _error.value = error.networkResponse?.let {
                    val statusCode = it.statusCode
                    val data = it.data?.let { data -> String(data) } ?: "No additional data"
                    "Error $statusCode: $data"
                } ?: "Unknown error occurred"
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val mapHeader: MutableMap<String, String> = HashMap()
                mapHeader["Authorization"] = "Bearer $stringAPIKey"
                mapHeader["Content-Type"] = "application/json"
                return mapHeader
            }
        }

        val intTimeOutPeriod = 60000 // 60 segundos
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(
            intTimeOutPeriod,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.retryPolicy = retryPolicy

        Volley.newRequestQueue(context).add(jsonObjectRequest)

    }

    private fun fetchImageBitmap(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val bitmap = BitmapFactory.decodeStream(url.openStream())
                _imageBitmap.postValue(bitmap)
            } catch (e: MalformedURLException) {
                _error.postValue("Malformed URL")
            } catch (e: IOException) {
                _error.postValue("IO Exception")
            }
        }
    }


    fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(path, filename)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(file)
            context.sendBroadcast(mediaScanIntent)

            Toast.makeText(context, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    fun setAsWallpaper(bitmap: Bitmap) {
        try {
            WallpaperManager.getInstance(context).setBitmap(bitmap)
            _error.postValue(null) // Limpia cualquier error previo
            Toast.makeText(context, "Imagen establecida exitosamente", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            _error.postValue("Error al establecer el fondo de pantalla")
        }
    }

    fun loadSelectedImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _imageBitmap.postValue(bitmap)
            } catch (e: IOException) {
                _error.postValue("Error al cargar la imagen desde la galería")
            }
        }
    }


}
