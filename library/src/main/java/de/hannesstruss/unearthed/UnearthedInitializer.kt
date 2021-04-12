package de.hannesstruss.unearthed

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class UnearthedInitializer : Initializer<Unearthed> {
  override fun create(context: Context): Unearthed {
    return Unearthed.init(context.applicationContext as Application)
  }

  override fun dependencies(): List<Class<out Initializer<*>>> {
    return emptyList()
  }
}
