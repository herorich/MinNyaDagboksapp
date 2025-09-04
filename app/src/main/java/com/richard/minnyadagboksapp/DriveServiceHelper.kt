package com.richard.minnyadagboksapp

import com.google.api.client.http.ByteArrayContent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DriveServiceHelper(account: GoogleSignInAccount) {

    private val driveService: Drive

    init {
        val credential = GoogleAccountCredential.usingOAuth2(
            AppContextProvider.appContext,
            listOf("https://www.googleapis.com/auth/drive.file")
        )
        credential.selectedAccount = account.account

        driveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("MinNyaDagboksapp")
            .build()
    }

    suspend fun createTextFile(fileName: String, content: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val metadata = File().apply {
                    name = fileName
                    mimeType = "text/plain"
                }

                val fileContent = ByteArrayContent.fromString("text/plain", content)

                val createdFile = driveService.files()
                    .create(metadata, fileContent)
                    .setFields("id")
                    .execute()

                Log.d("DriveServiceHelper", "Fil skapad med id: ${createdFile.id}")
                createdFile.id
            } catch (e: Exception) {
                Log.e("DriveServiceHelper", "Fel vid filuppladdning: ${e.message}")
                null
            }
        }
    }
}
