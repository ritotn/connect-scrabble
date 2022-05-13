package hu.ait.connectussy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            binding.boardView.playTurn()
        }
    }

    fun showText(message: String) {
        binding.tvMessage.text = message
    }
}