package gcera.app.eruduyuru.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.UserPref
import gcera.app.eruduyuru.objects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(300)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }
}