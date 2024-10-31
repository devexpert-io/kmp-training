package io.devexpert.kmptraining

import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider

object AppInitializer {
    fun onApplicationStart() {
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = BuildConfig.GOOGLE_SERVER_ID
            )
        )
    }
}