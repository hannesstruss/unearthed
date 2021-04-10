package de.hannesstruss.unearthed.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.hannesstruss.unearthed.Unearthed

class MainActivity : AppCompatActivity() {

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val txtContent = findViewById<TextView>(R.id.txt_content)
    txtContent.text = "Fresh"

    Unearthed.onProcessRestored { graveyard ->
      txtContent.text = "Process restored ${graveyard.gravestones.size} times."
    }
  }
}
