package de.hannesstruss.unearthed

import android.app.Application

class SampleApp : Application() {
  override fun onCreate() {
    super.onCreate()

    Unearthed.init(this)
  }
}
