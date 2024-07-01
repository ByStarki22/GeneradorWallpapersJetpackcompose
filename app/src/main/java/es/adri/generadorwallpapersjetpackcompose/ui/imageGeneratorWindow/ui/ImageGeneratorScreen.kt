package es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.ui

import android.app.Activity
import android.graphics.Bitmap
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.lifecycle.viewmodel.compose.viewModel
import es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.data.ImageGeneratorViewModelFactory
import es.adri.generadorwallpapersjetpackcompose.ui.imageGeneratorWindow.data.PayPalManager
import java.math.BigDecimal

@Composable
fun ImageGeneratorPreview() {
    ImageGeneratorScreen(viewModel())
}

@Composable
fun ImageGeneratorScreen(viewModel: ViewModel) {
    val context = LocalContext.current
    val viewModel: ImageGeneratorViewModel = viewModel(factory = ImageGeneratorViewModelFactory(context))
    val imageBitmap by viewModel.imageBitmap.observeAsState()
    val error by viewModel.error.observeAsState()
    var prompt by remember { mutableStateOf(TextFieldValue("")) }
    val payPalManager = remember { PayPalManager(context, "Your PayPal api") }

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }

    // Load Ad
    LaunchedEffect(Unit) {
        loadAd(context) { ad ->
            interstitialAd = ad
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackgroundImageGenerated(
                modifier = Modifier.weight(1f),
                imageBitmap = imageBitmap
            )
            Spacer(modifier = Modifier.height(16.dp))
            promptField(modifier = Modifier.fillMaxWidth(), prompt = prompt) {
                prompt = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            generateImagenButton(modifier = Modifier.fillMaxWidth()) {
                interstitialAd?.show(context as Activity) // Show ad before generating image
                viewModel.buttonAction(prompt.text)
            }
            Spacer(modifier = Modifier.height(6.dp))
            selectGalleryImageButton(modifier = Modifier.fillMaxWidth(), viewModel = viewModel)

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SaveToGalleryButton(viewModel = viewModel)
            DonationButton(payPalManager)
            SetAsWallpaperButton(viewModel = viewModel)
        }
    }
}

@Composable
fun BackgroundImageGenerated(modifier: Modifier = Modifier, imageBitmap: Bitmap?) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        imageBitmap?.let { bitmap ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bitmap)
                    .build(),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Image(
                painter = ColorPainter(Color.Transparent),
                contentDescription = "Default Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SaveToGalleryButton(viewModel: ImageGeneratorViewModel) {
    Button(
        onClick = {
            viewModel.imageBitmap.value?.let { bitmap ->
                viewModel.saveImageToGallery(bitmap)
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFFFFFF)
        ),
        border = BorderStroke(1.dp, Color(0xFF925904)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Guardar")
    }
}

@Composable
fun DonationButton(payPalManager: PayPalManager) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFFFFFF)
        ),
        border = BorderStroke(1.dp, Color(0xFF925904)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Donacion")
    }

    if (showDialog) {
        showDonationDialog(context, payPalManager) { showDialog = false }
    }
}

@Composable
fun showDonationDialog(context: Context, payPalManager: PayPalManager, onDismiss: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Donación") },
        text = {
            Column {
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text(text = "Ingrese el monto de la donación") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (amount.isNotEmpty()) {
                        val donationAmount = BigDecimal(amount.trim())
                        payPalManager.startPayment(donationAmount)
                        onDismiss()
                    } else {
                        Toast.makeText(context, "cantidad invalida", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Pagar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SetAsWallpaperButton(viewModel: ImageGeneratorViewModel) {
    Button(
        onClick = {
            viewModel.imageBitmap.value?.let { bitmap ->
                viewModel.setAsWallpaper(bitmap)
            }
        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFFFFFF)
        ),
        border = BorderStroke(1.dp, Color(0xFF925904)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Establecer como fondo")
    }
}

@Composable
fun promptField(modifier: Modifier, prompt: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    TextField(
        value = prompt,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Escribe el prompt", color = Color.White) },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF925904),
            unfocusedTextColor = Color(0xFF925904),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White
        )
    )
}

@Composable
fun generateImagenButton(modifier: Modifier, buttonAction: () -> Unit) {
    Button(
        onClick = { buttonAction() },
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFFFFFF)
        ),
        border = BorderStroke(1.dp, Color(0xFF925904)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Generar Imagen")
    }
}

@Composable
fun selectGalleryImageButton(viewModel: ImageGeneratorViewModel, modifier: Modifier) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.loadSelectedImage(it)
        }
    }

    Button(
        onClick = {
            pickMedia.launch("image/*")
        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFF925904)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Seleccionar de galería")
    }
}

fun loadAd(context: Context, onAdLoaded: (InterstitialAd?) -> Unit) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(
        context,
        "ca-app-pub-3940256099942544/1033173712",
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                onAdLoaded(null)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                onAdLoaded(interstitialAd)
            }
        }
    )
}
