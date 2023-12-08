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

    companion object {
        const val USERNAME = "USERNAME"
        const val PREV_GAME = "PREV_GAME"
        const val PREV_SCORE = "PREV_SCORE"
    }

    lateinit var binding : ActivityGameBinding

    private lateinit var username : String
    private var cpu : Int = 0
    private var attempt : Int = 3
    private var game : Int = 0
    private var score : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cpuRandom()

        username = intent.getStringExtra(USERNAME).toString()

        binding.tvGameUsername.text = getString(R.string.welcome_username, username)
        binding.tvGameWrong.visibility = View.GONE

        binding.btnGameSend.setOnClickListener { cpuVsUser() }

    }

    @SuppressLint("RestrictedApi")
    private fun cpuVsUser() {
        val user : Int = binding.etGameUser.text.toString().toInt()
//        Log.d("TAG", "cpuVsUser: $user")

        binding.etGameUser.apply {
            text.clear()
            clearFocus()
            val view : View = this.rootView
            hideKeyboard(view)
        }

        if (user < 0 || user > 10) {
            Toast.makeText(this, getString(R.string.toast_game_error), Toast.LENGTH_LONG).show()
        }
        else {
            if (user < cpu) {
                binding.tvGameWrong.text = getString(R.string.game_number_is_higher)
                refreshAttempts()
            }
            else if (user > cpu) {
                binding.tvGameWrong.text = getString(R.string.game_number_is_lower)
                refreshAttempts()
            }
            else backToTheFuture()
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun refreshAttempts() {
        binding.tvGameWrong.visibility = View.VISIBLE
        attempt--

        binding.tvGameAttempts.text =
            if (attempt > 1) getString(R.string.tv_game_show_attempts, attempt)
            else getString(R.string.tv_game_show_attempt, attempt)

        if(attempt == 0) backToTheFuture()
    }

    private fun backToTheFuture() {

        addPrevScore()

//        Log.d("TAG", "backToTheFuture: game = $game / score = $attempt")

        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            putExtra(GAME, game)
            putExtra(SCORE, score)
            putExtra(PREV_USER, username)
        }
        startActivity(intent)
    }

    private fun addPrevScore() {

        val prevGame : Int = intent.getIntExtra(PREV_GAME, 0)
        game = prevGame + 1
        // game ++

        val prevScore : Int = intent.getIntExtra(PREV_SCORE, 0)
        score = prevScore + attempt
    }

    private fun cpuRandom() {
        // Méthode JAVA
//        val rand = Math.random().toInt()
//        cpu = (rand * 10)

        // Méthode pure Kotlin
        val rand = (0..10).shuffled().first()
        cpu = rand

        Log.d("TAG", "cpuRandom: $cpu")
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        super.onBackPressed()

//        Log.d("TAG", "onBackPressed: disable")
    }
}