package be.melyuki.mysterygame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.melyuki.mysterygame.MainActivity.Companion.GAME
import be.melyuki.mysterygame.MainActivity.Companion.PREV_USER
import be.melyuki.mysterygame.MainActivity.Companion.SCORE
import be.melyuki.mysterygame.databinding.ActivityGameBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class GameActivity : AppCompatActivity() {

    // Création d'un objet compagnion,
    // qui servira à transporter les données d'une activité à une autre.
    companion object {
        const val USERNAME = "USERNAME"
        const val PREV_GAME = "PREV_GAME"
        const val PREV_SCORE = "PREV_SCORE"
    }

    // Binding des éléments xml du layout et de l'activity.
    lateinit var binding : ActivityGameBinding

    // Création des variables globales.
    private lateinit var username : String
    private var cpu : Int = 0
    private var attempt : Int = 3
    private var game : Int = 0
    private var score : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Adaptation de l'incorporation du layout suite au binding ci-dessus.
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fonction qui choisi un numéro aléatoire.
        cpuRandom()

        // Récupération du nom du joueur via l'intent, venant de l'autre activité.
        username = intent.getStringExtra(USERNAME).toString()

        // Affichage du nom du joueur dans l'élément xml dédié.
        binding.tvGameUsername.text = getString(R.string.welcome_username, username)
        // On cache la réponse, non désirée pour l'instant, d'après choix du joueur.
        binding.tvGameWrong.visibility = View.GONE

        // Création du callback (listener) et de l'action à produire suite au click du bouton.
        binding.btnGameSend.setOnClickListener { cpuVsUser() }

    }

    // le @SuppressLint(...),
    // permet de cacher l'erreur produite à cause de l'utilisation de la fonction hideKeyboard().
    @SuppressLint("RestrictedApi")
    private fun cpuVsUser() {

        // Création d'une variable qui va contenir le numéro du joueur.
        val user : Int = binding.etGameUser.text.toString().toInt()
        // Log.d("TAG", "cpuVsUser: $user")

        // Application de plusieurs choses sur l'EditText du choix du joueur.
        binding.etGameUser.apply {
            // On nettoie l'EditText
            text.clear()
            // On enlève le focus sur l'EditText
            clearFocus()

            // On cache le clavier du mobile.
            hideKeyboard(this.rootView)
        }

        // Conditions et Comparaisons du choix du joueur et du numéro random.
        if (user < 0 || user > 10) {
            // Gestion d'erreur d'utilisation du joueur.
            Toast.makeText(this, getString(R.string.toast_game_error), Toast.LENGTH_LONG).show()
        }
        else {
            if (user < cpu) {
                // Création d'une variable contenant l'indice à afficher.
                val msgHigher : String = getString(R.string.game_number_is_higher)
                // Fonction qui rafraichit le nombre d'essais du joueur.
                refreshAttempts(msgHigher)
            }
            else if (user > cpu) {
                // Création d'une variable contenant l'indice à afficher.
                val msgLower : String = getString(R.string.game_number_is_lower)
                // Fonction qui rafraichit le nombre d'essais du joueur.
                refreshAttempts(msgLower)
            }
            // Fonction appelée une fois le bon numéro trouvé.
            else backToTheFuture()
        }
    }

    private fun refreshAttempts(message : String) {
        // Message envoyé dans la fenêtre d'indice si le numéro du joueur est plus petit.
        binding.tvGameWrong.text = message
        // Affichage de la fenêtre d'indice au joueur.
        binding.tvGameWrong.visibility = View.VISIBLE

        // On enlève un essai au joueur.
        attempt--

        // On rafraichit les essais
        binding.tvGameAttempts.text =
            if (attempt > 1) getString(R.string.tv_game_show_attempts, attempt)
            else getString(R.string.tv_game_show_attempt, attempt)

        // Fonction appelée une fois que le joueur n'a plus d'essai.
        if(attempt == 0) backToTheFuture()
    }

    private fun backToTheFuture() {

        // Fonction qui rajoute les scores précédents.
        addPrevScore()

        // Log.d("TAG", "backToTheFuture: game = $game / score = $attempt")

        // Création de l'intent qui nous ramènera à la MainActivity, avec les données nécessaires.
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            putExtra(GAME, game)
            putExtra(SCORE, score)
            putExtra(PREV_USER, username)
        }
        // Lancement de l'intent vers la MainActivity.
        startActivity(intent)
    }

    private fun addPrevScore() {

        // Récupération du nombre de jeu déjà joués.
        val prevGame : Int = intent.getIntExtra(PREV_GAME, 0)
        // Addition de ce jeu-ci et des précédents.
        game = prevGame + 1

        // Récupération des scores obtenus précédemment.
        val prevScore : Int = intent.getIntExtra(PREV_SCORE, 0)
        // Addition des score de cette partie-ci et des précédents.
        score = prevScore + attempt
    }

    private fun cpuRandom() {
        // Méthode JAVA
        // val rand = Math.random().toInt()
        // cpu = (rand * 10)

        // Méthode pure Kotlin
        val rand = (0..10).shuffled().first()
        cpu = rand

        Log.d("TAG", "cpuRandom: $cpu")
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // En commentant la ligne ci-dessous, on interdit l'utilisation du bouton back du mobile!
        // super.onBackPressed()

        // Log.d("TAG", "onBackPressed: disable")
    }
}