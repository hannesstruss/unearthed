package de.hannesstruss.unearthed

import android.content.ComponentName
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
    val componentName = ComponentName("package", "class")

    val unearthed1 = Unearthed(1)
    unearthed1.onActivitySaveInstanceState(state, componentName)

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
    val componentName1 = ComponentName("package1", "class1")
    unearthed1.onActivitySaveInstanceState(state1, componentName1)

    val unearthed2 = Unearthed(2)
    unearthed2.onActivityCreated(state1)
    val state2 = Bundle()
    val componentName2 = ComponentName("package2", "class2")
    unearthed2.onActivitySaveInstanceState(state2, componentName2)

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
    val componentName1 = ComponentName("package1", "class1")
    val stateActivity2 = Bundle()
    val componentName2 = ComponentName("package2", "class2")

    unearthed1.onActivitySaveInstanceState(stateActivity1, componentName1)
    unearthed1.onActivitySaveInstanceState(stateActivity2, componentName2)

    val unearthed2 = Unearthed(2)
    var callbackCalls = 0
    unearthed2.onProcessRestored { callbackCalls += 1 }

    unearthed2.onActivityCreated(stateActivity1)
    unearthed2.onActivityCreated(stateActivity2)

    assertThat(callbackCalls).isEqualTo(1)
  }

  @Test
  fun savesCorrectTimeAndComponentName() {
    var now = 23L
    val clock = { now }

    val unearthed1 = Unearthed(1, epochClock = clock)
    val state1 = Bundle()
    val componentName1 = ComponentName("package1", "class1")
    unearthed1.onActivitySaveInstanceState(state1, componentName1)

    now = 123L

    val unearthed2 = Unearthed(2, epochClock = clock)
    var graveyardOpt: Graveyard? = null
    unearthed2.onProcessRestored { graveyardOpt = it }
    unearthed2.onActivityCreated(state1)

    val graveyard = checkNotNull(graveyardOpt)
    assertThat(graveyard.gravestones.first().backgroundedEpochMillis).isEqualTo(23L)
    assertThat(graveyard.gravestones.first().millisToRestore).isEqualTo(123L - 23L)
    assertThat(graveyard.gravestones.first().millisToRestore).isEqualTo(123L - 23L)
    assertThat(graveyard.gravestones.first().componentName).isEqualTo(componentName1)
  }

  @Test
  fun callsCallbackAfterRestore() {
    val unearthed1 = Unearthed(1)
    val state1 = Bundle()
    val componentName1 = ComponentName("package1", "class1")
    unearthed1.onActivitySaveInstanceState(state1, componentName1)

    val unearthed2 = Unearthed(2)
    unearthed2.onActivityCreated(state1)
    var graveyardOpt: Graveyard? = null
    unearthed2.onProcessRestored { graveyardOpt = it }

    assertThat(graveyardOpt).isNotNull()
  }
}
