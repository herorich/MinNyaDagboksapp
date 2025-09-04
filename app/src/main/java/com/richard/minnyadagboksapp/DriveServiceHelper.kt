import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.ByteArrayInputStream
import com.google.api.client.http.InputStreamContent
import java.util.Collections

class DriveServiceHelper(private val googleSignInAccount: GoogleSignInAccount) {

    private val driveService: Drive

    init {
        val credential = GoogleAccountCredential.usingOAuth2(
            // Context, men vi behöver den inte här för detta ändamål
            // Eftersom vi skickar in ett redan inloggat konto
            // Om du får problem, skicka med Context från din Activity
            // och använd den här:
            // GoogleAccountCredential.usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE))
            // Men för nu provar vi den enklare metoden.
            null, Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = googleSignInAccount.account

        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()
        driveService = Drive.Builder(
            transport,
            jsonFactory,
            credential
        )
            .setApplicationName("Dagboksapp")
            .build()
    }

    // Funktion för att skapa en textfil
    fun createTextFile(fileName: String, content: String): File? {
        val metadata = File().apply {
            name = fileName
            mimeType = "text/plain"
        }

        val contentStream = InputStreamContent(
            "text/plain",

            ByteArrayInputStream(content.toByteArray())
        )

        return try {
            driveService.files().create(metadata, contentStream).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}