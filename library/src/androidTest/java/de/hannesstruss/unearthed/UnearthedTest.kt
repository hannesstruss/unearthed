package de.hannesstruss.unearthed

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UnearthedTest {
  @Test
  fun singleDeath() {
    val state = Bundle()

    val unearthed1 = Unearthed(1)
    unearthed1.onActivitySaveInstanceState(state)

    val unearthed2 = Unearthed(2)
    var graveyardOpt: Graveyard? = null
    unearthed2.onProcessRestored { graveyardOpt = it }
    unearthed2.onActivityCreated(state)

    val graveyard = checkNotNull(graveyardOpt)
    assertThat(graveyard.gravestones.first().pid).isEqualTo(1)
  }

  @Test
  fun multipleDeaths() {
    val unearthed1 = Unearthed(1)
    val state1 = Bundle()
    unearthed1.onActivitySaveInstanceState(state1)

    val unearthed2 = Unearthed(2)
    unearthed2.onActivityCreated(state1)
    val state2 = Bundle()
    unearthed2.onActivitySaveInstanceState(state2)

    val unearthed3 = Unearthed(3)
    var graveyardOpt: Graveyard? = null
    unearthed3.onProcessRestored { graveyardOpt = it }
    unearthed3.onActivityCreated(state2)

    val graveyard = checkNotNull(graveyardOpt)
    assertThat(graveyard.gravestones).hasSize(2)
    assertThat(graveyard.gravestones[0].pid).isEqualTo(1)
    assertThat(graveyard.gravestones[1].pid).isEqualTo(2)
  }

  @Test
  fun multipleActivities() {
    val unearthed1 = Unearthed(1)

    val stateActivity1 = Bundle()
    val stateActivity2 = Bundle()

    unearthed1.onActivitySaveInstanceState(stateActivity1)
    unearthed1.onActivitySaveInstanceState(stateActivity2)

    val unearthed2 = Unearthed(2)
    var callbackCalls = 0
    unearthed2.onProcessRestored { callbackCalls += 1 }

    unearthed2.onActivityCreated(stateActivity1)
    unearthed2.onActivityCreated(stateActivity2)

    assertThat(callbackCalls).isEqualTo(1)
  }

  @Test
  fun savesCorrectTime() {
    var now = 23L
    val clock = { now }

    val unearthed1 = Unearthed(1, epochClock = clock)
    val state1 = Bundle()
    unearthed1.onActivitySaveInstanceState(state1)

    now = 123L

    val unearthed2 = Unearthed(2, epochClock = clock)
    var graveyardOpt: Graveyard? = null
    unearthed2.onProcessRestored { graveyardOpt = it }
    unearthed2.onActivityCreated(state1)

    val graveyard = checkNotNull(graveyardOpt)
    assertThat(graveyard.gravestones.first().backgroundedEpochMillis).isEqualTo(23L)
    assertThat(graveyard.gravestones.first().millisToRestore).isEqualTo(123L - 23L)
  }

  @Test
  fun callsCallbackAfterRestore() {
    val unearthed1 = Unearthed(1)
    val state1 = Bundle()
    unearthed1.onActivitySaveInstanceState(state1)

    val unearthed2 = Unearthed(2)
    unearthed2.onActivityCreated(state1)
    var graveyardOpt: Graveyard? = null
    unearthed2.onProcessRestored { graveyardOpt = it }

    assertThat(graveyardOpt).isNotNull()
  }
}
