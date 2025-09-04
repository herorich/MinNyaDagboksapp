import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.richard.minnyadagboksapp.R

class AddEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        // Dessa två rader MÅSTE finnas här för att lösa fel 1 & 2
        val editText = findViewById<EditText>(R.id.editText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val textToSave = editText.text.toString()

            if (textToSave.isNotEmpty()) {
                uploadTextToDrive(textToSave)
            } else {
                Toast.makeText(this, "Texten kan inte vara tom", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadTextToDrive(text: String) {
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account == null) {
            Toast.makeText(this, "Du är inte inloggad!", Toast.LENGTH_SHORT).show()
            return
        }

        val driveServiceHelper = DriveServiceHelper(account)
        val fileName = "dagboksinlagg_${System.currentTimeMillis()}.txt"

        // Använd Coroutines för att köra på en bakgrundstråd
        CoroutineScope(Dispatchers.IO).launch {
            val result = driveServiceHelper.createTextFile(fileName, text)

            // Växla tillbaka till huvudtråden för att visa resultat för användaren
            withContext(Dispatchers.Main) {
                if (result != null) {
                    Toast.makeText(this@AddEntryActivity, "Inlägg sparat!", Toast.LENGTH_LONG).show()
                    finish() // Stäng aktiviteten och återgå till huvudskärmen
                } else {
                    Toast.makeText(this@AddEntryActivity, "Ett fel uppstod.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}