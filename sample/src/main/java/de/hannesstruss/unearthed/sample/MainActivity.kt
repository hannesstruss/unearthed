package de.hannesstruss.unearthed.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.hannesstruss.unearthed.Unearthed
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt_content.text = "Fresh"

        Unearthed.onProcessRestored {
            txt_content.text = "Process restored"
        }
    }
}
