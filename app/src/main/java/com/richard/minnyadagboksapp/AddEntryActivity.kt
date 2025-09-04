import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.richard.minnyadagboksapp.databinding.ActivityAddEntryBinding // Viktig import!
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEntryActivity : AppCompatActivity() {

    // Skapa en binding-variabel
    private lateinit var binding: ActivityAddEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Använd binding för att sätta upp layouten
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nu kan du komma åt dina vyer säkert via binding-objektet
        binding.buttonSave.setOnClickListener {
            val textToSave = binding.editTextEntry.text.toString()


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

        CoroutineScope(Dispatchers.IO).launch {
            val result = driveServiceHelper.createTextFile(fileName, text)

            withContext(Dispatchers.Main) {
                if (result != null) {
                    Toast.makeText(this@AddEntryActivity, "Inlägg sparat!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEntryActivity, "Ett fel uppstod.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}