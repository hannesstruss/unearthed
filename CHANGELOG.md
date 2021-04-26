# Changes

## 0.3.0

_(2020-04-26)_

- BREAKING: Unearthed will initialize itself again, this time via
  [AndroidX App Startup](https://developer.android.com/topic/libraries/app-startup).
  Call `Unearthed.initManuallyWithDisabledAndroidXStartup(app)` if you disable AndroidX
  App Startup.

## 0.2.0

_(2021-04-10)_

- BREAKING: Unearthed won't initialize itself via a dummy `ContentProvider`
  anymore, instead `Unearthed.init(app)` now has to be called explicitly

## 0.1.2

_(2020-02-18)_

- Include Javadoc artifact

## 0.1.1

_(2020-02-17)_

- Target JDK 1.8
- Compile/target SDK 29
- Include sources artifact
- Don't generate BuildConfig

## 0.1.0 

_(2020-02-17)_

- Initial release
