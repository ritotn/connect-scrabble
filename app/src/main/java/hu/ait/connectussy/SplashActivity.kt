package hu.ait.connectussy

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import hu.ait.connectussy.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)

        anim.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent()
                    intent.setClass(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAnimationStart(animation: Animation?) {}
            }
        )

        binding.imgSplash.startAnimation(anim)

    }
}