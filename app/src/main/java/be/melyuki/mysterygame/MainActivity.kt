package be.melyuki.mysterygame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import be.melyuki.mysterygame.GameActivity.Companion.PREV_GAME
import be.melyuki.mysterygame.GameActivity.Companion.PREV_SCORE
import be.melyuki.mysterygame.GameActivity.Companion.USERNAME
import be.melyuki.mysterygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Création d'un objet compagnion,
    // qui servira à transporter les données d'une activité à une autre.
    companion object {
        const val GAME = "GAME"
        const val SCORE = "SCORE"
        const val PREV_USER = "PREV_USER"
    }

    // Binding des éléments xml du layout et de l'activity.
    // PREALABLEMENT l'activer dans le build.gradle(Module:app)!
    lateinit var binding : ActivityMainBinding

    // Création des variables globales.
    private var game : Int = 0
    private var score : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Adaptation de l'incorporation du layout suite au binding ci-dessus.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fonction qui cache des éléments au départ.
        hideElements()
        // Fonction qui rafraichit les scores.
        refreshScore()

        // region LISTENER
        // Création des callback (listener) et des actions à produire suite au click des boutons.
        binding.btnPlay.setOnClickListener { goGame() }
        binding.btnReset.setOnClickListener { resetGame() }
        binding.btnReplay.setOnClickListener { playAgain() }
        //endregion
    }

    private fun playAgain() {

        // Création d'une variable qui va contenir le nom de l'utilisateur précedent.
        val prevUser : String = intent.getStringExtra(PREV_USER)!!

        // Création de l'intent de navigation, munit des données nécessaires.
        val intent : Intent = Intent(this, GameActivity::class.java).apply {
            putExtra(USERNAME, prevUser)
            putExtra(PREV_GAME, game)
            putExtra(PREV_SCORE, score)
        }
        // Lancement de l'intent vers l'autre activité.
        startActivity(intent)
    }

    private fun resetGame() {

        // Remise à zéro des scores.
        binding.tvDefaultScore.text = getString(R.string.tv_main_game_score, game, score)

        // On cache à nouveau les éléments de départ.
        hideElements()
    }

    private fun goGame() {

        // Création d'une variable qui va contenir la valeur reçue de l'EditText de l'xml.
        val username : String = binding.etMainUsername.text.toString()
        // Log.d("TAG", "goGame: $username")

        // Création de l'intent de navigation vers une autre activité.
        val intent : Intent = Intent(this, GameActivity::class.java).apply {
            // putExtra() permet d'envoyer le nom du joueur vers l'autre actitivé.
            putExtra(USERNAME, username)
        }

        // Condition d'accès au jeu!
        if (username == ""){
            // Log.d("TAG", "goGame: test if")

            // Création d'un toast alertant l'utilisateur.
            Toast.makeText(this, getString(R.string.toast_main_play), Toast.LENGTH_LONG).show()
        }
        else
            // Lancement de l'intent vers l'autre activité.
            startActivity(intent)
    }

    private fun refreshScore() {

        // Récupération des scores de l'intent, venant de l'autre activité.
        game = intent.getIntExtra(GAME, 0)
        score = intent.getIntExtra(SCORE, 0)
        // Log.d("TAG", "game: $game / score: $score")

        // Condition de rafraichissement des scores.
        if (game > 0) {
            // Affichage des nouveaux scores dans l'élément xml dédié.
            binding.tvDefaultScore.text = getString(R.string.tv_main_game_score, game, score)
            // Affichage du résultat (gagné ou perdu) du jeu dernièrement joué.
            binding.tvDefaultResult.text =
                if(score > 0) getString(R.string.tv_main_result_win)
                else getString(R.string.tv_main_result_loose)

            // Fonction qui affiche les éléments cachés au départ.
            showElements()
        }
    }

    private fun hideElements() {

        // On cache des éléments
        binding.tvDefaultResult.visibility = View.GONE
        binding.btnReplay.visibility = View.GONE
        binding.btnReset.visibility = View.GONE

        // On affiche des éléments
        binding.etMainUsername.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.VISIBLE
    }

    private fun showElements() {

        // On affiche des éléments
        binding.tvDefaultResult.visibility = View.VISIBLE
        binding.btnReplay.visibility = View.VISIBLE
        binding.btnReset.visibility = View.VISIBLE

        // On cache des éléments
        binding.etMainUsername.visibility = View.GONE
        binding.btnPlay.visibility = View.GONE
    }
}