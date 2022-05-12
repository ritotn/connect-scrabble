package hu.ait.connectussy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.connectussy.databinding.ActivityMainBinding
import hu.ait.connectussy.dialog.GameoverDialog

class MainActivity : AppCompatActivity(), GameoverDialog.GameoverHandler {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRestart.setOnClickListener {
            binding.boardView.resetGame()
            binding.tvMessage.text = "Boohoo"
        }
        binding.btnSubmit.setOnClickListener {
            binding.boardView.changePlayer()
        }
    }

    override fun gameoverCreated() {
        TODO("Not yet implemented")
    }

    public fun showText(message: String) {
        binding.tvMessage.text = message
    }
}