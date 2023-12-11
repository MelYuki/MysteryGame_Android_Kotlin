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

    companion object {
        const val GAME = "GAME"
        const val SCORE = "SCORE"
        const val PREV_USER = "PREV_USER"
    }

    lateinit var binding : ActivityMainBinding

    private var game : Int = 0
    private var score : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideElements()
        refreshScore()

        // region LISTENER
        binding.btnPlay.setOnClickListener { goGame() }
        binding.btnReset.setOnClickListener { resetGame() }
        binding.btnReplay.setOnClickListener { playAgain() }
        //endregion
    }

    private fun playAgain() {

        val prevUser : String = intent.getStringExtra(PREV_USER).toString()

        val intent : Intent = Intent(this, GameActivity::class.java).apply {
            putExtra(USERNAME, prevUser)
            putExtra(PREV_GAME, game)
            putExtra(PREV_SCORE, score)
        }
        startActivity(intent)
    }

    private fun refreshScore() {

        game = intent.getIntExtra(GAME, 0)
        score = intent.getIntExtra(SCORE, 0)
//        Log.d("TAG", "game: $game / score: $score")

        if (game > 0) {
            binding.tvDefaultScore.text = getString(R.string.tv_main_game_score, game, score)
            binding.tvDefaultResult.text =
                if(score > 0) getString(R.string.tv_main_result_win)
                else getString(R.string.tv_main_result_loose)

            showElements()
        }
    }

    private fun goGame() {

        val username : String = binding.etMainUsername.text.toString()
//        Log.d("TAG", "goGame: $username")

        val intent : Intent = Intent(this, GameActivity::class.java).apply {
            putExtra(USERNAME, username)
        }

        if (username == ""){
//            Log.d("TAG", "goGame: test if")
            Toast.makeText(this, getString(R.string.toast_main_play), Toast.LENGTH_LONG).show()
        }
        else
            startActivity(intent)
    }

    private fun resetGame() {

        binding.tvDefaultScore.text = getString(R.string.tv_main_game_score, game, score)
        hideElements()
    }

    private fun hideElements() {

        binding.tvDefaultResult.visibility = View.GONE
        binding.btnReplay.visibility = View.GONE
        binding.btnReset.visibility = View.GONE

        binding.etMainUsername.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.VISIBLE
    }

    private fun showElements() {

        binding.tvDefaultResult.visibility = View.VISIBLE
        binding.btnReplay.visibility = View.VISIBLE
        binding.btnReset.visibility = View.VISIBLE

        binding.etMainUsername.visibility = View.GONE
        binding.btnPlay.visibility = View.GONE
    }
}