# Unearthed

A library for Android that notifies you when your app was restored after a
process death.

![CI](https://github.com/hannesstruss/unearthed/workflows/CI/badge.svg)

Should you still save your instance state in a world of phones with 12GB of
RAM? Maybe! Since knowing is better than guessing, Unearthed lets you track how
often your app is sent to the background, dies, and gets restored. It's up to
you what to do with that information, but why not log an event to your
analytics?


## Usage

Unearthed will initialize itself via [AndroidX App Startup][androidx-startup].

Wherever convenient, add a listener to `Unearthed` and get notified of
restoration after process death.

```kotlin
Unearthed.onProcessRestored { graveyard ->
  val timesRestored = graveyard.gravestones.size
  Log.d("MainActivity", "App has been restored $timesRestored times.")
  trackProcessDeathToAnalytics()
}
```

### Disabling AndroidX App Startup

It's possible to [disable AndroidX App Startup][androidx-startup]. When doing that,
make sure to call `Unearthed.initManuallyWithDisabledAndroidXStartup(app)` in your
`Application.onCreate`.

## Download

Find artifacts on [Maven
Central](https://search.maven.org/search?q=de.hannesstruss.unearthed)
at `de.hannesstruss.unearthed:unearthed:0.2.0`.

# License

    Copyright 2020 Hannes Struss
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[androidx-startup]: https://developer.android.com/topic/libraries/app-startup
