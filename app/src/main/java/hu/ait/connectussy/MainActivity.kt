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
            MaterialDialog(this).show {
                title(R.string.restart)
                message(R.string.restart_game)
                positiveButton(R.string.yes) {
                    dialog -> binding.boardView.resetGame()
                }
                negativeButton(R.string.no)
            }

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
    }

    fun showText(message: String) {
        binding.tvMessage.text = message
    }
}