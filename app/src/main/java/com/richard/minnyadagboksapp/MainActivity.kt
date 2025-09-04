package com.richard.minnyadagboksapp // <-- ÄNDRA TILL DITT PACKAGE NAME

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.richard.minnyadagboksapp.databinding.ActivityMainBinding // <-- ÄNDRA TILL DITT PACKAGE NAME

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    // Detta är den nya, moderna sättet att hantera resultat från en annan aktivitet (som inloggningsfönstret)
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Inloggningen lyckades!
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(this, "Inloggad som ${account.displayName}", Toast.LENGTH_SHORT).show()
                updateUI(account)
            } catch (e: ApiException) {
                // Inloggningen misslyckades
                Log.w("SignIn", "signInResult:failed code=" + e.statusCode)
                Toast.makeText(this, "Inloggning misslyckades", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Konfigurera Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // Be om användarens e-post
            .requestServerAuthCode(getString(R.string.server_client_id)) // Be om en kod för server-access
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 2. Sätt klick-lyssnare för knapparna
        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.fabAddEntry.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        // Kolla om en användare redan är inloggad när appen startar
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        // Starta inloggnings-aktiviteten och få tillbaka ett resultat
        signInLauncher.launch(signInIntent)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // Användaren är inloggad: Göm inloggningsknappen och visa appens innehåll
            binding.signInButton.visibility = View.GONE
            binding.recyclerViewEntries.visibility = View.VISIBLE
            binding.fabAddEntry.visibility = View.VISIBLE
        } else {
            // Användaren är inte inloggad: Visa inloggningsknappen och göm appens innehåll
            binding.signInButton.visibility = View.VISIBLE
            binding.recyclerViewEntries.visibility = View.GONE
            binding.fabAddEntry.visibility = View.GONE
        }
    }
}