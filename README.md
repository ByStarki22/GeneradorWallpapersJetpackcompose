# GeneradorWallpapersJetpackcompose

GeneradorWallpapersJetpackcompose is an Android application that utilizes the OpenAI API to generate custom wallpapers. Users can set these wallpapers, save them to the gallery, donate via PayPal, and view interstitial ads using Google AdMob. 
You need to login to use the application.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [Contributing](#contributing)

## Overview

GeneradorWallpapersJetpackcompose leverages the power of the OpenAI API to create unique wallpapers based on user input. It provides a seamless experience for generating, setting, and saving wallpapers, along with options for donations through PayPal and displaying interstitial ads using Google AdMob.

## Features

- **Image Generation**: Create custom wallpapers using OpenAI.
- **Set Wallpapers**: Set generated images directly as device wallpapers.
- **Save to Gallery**: Save wallpapers to your device's photo gallery.
- **PayPal Integration**: Accept donations for supporting the app development.
- **Interstitial Ads**: Display ads to support the free version of the app.

## Technologies

GeneradorWallpapersJetpackcompose is created with:
- Kotlin
- Android Studio
- OpenAI API
- Google AdMob
- PayPal SDK

## Setup

To run AutoPaper locally, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/ByStarki22/GeneradorWallpapersJetpackcompose.git
    ```

2. **Open in Android Studio**:
    - Open the project in Android Studio.

3. **Add API Keys**:
    - Add your OpenAI API Key and PayPal Client ID to the corresponding variables in the code:
    ```kotlin
    private val stringAPIKey = "Your OpenAI API Key"
    val payPalManager = remember { PayPalManager(context, "Your PayPal api") }
    ```

4. **Configure AdMob**:
    - Configure your AdMob account to obtain an interstitial ad ID and replace it in the code:
    ```kotlin
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
    ```

5. **Build and Run**:
    - Build and run AutoPaper on your device or emulator.

## Usage

1. **Enter Text**:
    - Enter the text for image generation in the input field.

2. **Generate Image**:
    - Click the "Generate Image" button to create the wallpaper.

3. **Set Wallpaper**:
    - Use the "Set Wallpaper" button to set the generated image as your device wallpaper.

## Contributing

Contributions are welcome!

---

Thank you for using GeneradorWallpapersJetpackcompose!
