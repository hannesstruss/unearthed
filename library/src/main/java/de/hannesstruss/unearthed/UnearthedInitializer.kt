package de.hannesstruss.unearthed

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class UnearthedInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    Unearthed.init(context.applicationContext as Application)
  }

  override fun dependencies(): List<Class<out Initializer<*>>> {
    return emptyList()
  }
}
