package hu.ait.connectussy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import hu.ait.connectussy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRestart.setOnClickListener {
            binding.boardView.resetGame()
        }
        binding.btnSubmit.setOnClickListener {
            binding.letterBoxView.playTurn()
            binding.boardView.playTurn()
        }

        binding.btnInstructions.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.instructions)
                message(R.string.instr_body)
                positiveButton(R.string.okay)
            }
        }
        /* TODO: Dialog should pop up for Instructions button
        *        Mention that words are from https://www.thefreedictionary.com/4-letter-words.htm
        */
        // TODO: Dialog should pop up for when the game is over
    }

    fun showText(message: String) {
        binding.tvMessage.text = message
    }
}