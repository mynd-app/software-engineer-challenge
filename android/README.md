# Android app (Kotlin Multiplatform)

A small Kotlin Multiplatform project with an Android target. Almost everything lives in the
shared module, the same way our real app is built; `androidApp/` is a thin shell.

```
android/
  shared/       KMP module: DTOs, API client (Ktor), repositories, ViewModels, Compose screens
    src/commonMain/   all app code
    src/androidMain/  the Android HTTP engine (OkHttp)
    src/commonTest/   tests (Ktor MockEngine, kotlin.test, turbine)
  androidApp/   one Activity hosting the shared `App()` composable, Koin start-up
```

## Setup (not part of your 4 hours)

1. Install a current stable Android Studio (one that supports Android Gradle Plugin 9.4) with an
   emulator image. The first sync downloads Gradle 9.8 and the SDK 37 platform, which takes a
   while. Gradle needs JDK 17 or newer; Android Studio's bundled JDK works.
2. Open the `android/` folder (not the repo root) in Android Studio and let Gradle sync.
3. Start the backend first (see `../backend/README.md`). The app expects it on port 8080.
4. Run the `androidApp` configuration on an emulator.

From the command line, if you prefer:

```bash
cd android
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"   # macOS example
./gradlew :androidApp:assembleDebug
```

If Android Studio fights you, the command line is enough to do every task except seeing the
screen: build with Gradle and run the shared tests with `./gradlew :shared:testAndroidHostTest`.

## Base URL and token

- The emulator reaches your machine at `10.0.2.2`, so the default base URL is
  `http://10.0.2.2:8080/api/`. On a physical device, use your machine's LAN IP.
- The app signs in as a seeded user by sending a token in the `X-Mynd-Token` header. The default
  is `member-token`. The backend README lists the other seeded tokens (for example the HR admin).
- Change either in `androidApp/build.gradle.kts`, or pass them on the command line:

```bash
./gradlew :androidApp:installDebug -PapiToken=admin-token -PapiBaseUrl=http://192.168.1.20:8080/api/
```

## Tests

```bash
./gradlew :shared:testAndroidHostTest
```

This runs `commonTest` on the JVM, with no emulator. Network calls are answered by a Ktor
`MockEngine`, so the backend does not need to be running.

## Conventions

- New screens use `MyndScaffold` (`ui/components/`) for their frame and top bar.
- API code goes in `data/api` and `data/dto`, mapping and error handling in `data/repository`,
  screen state in a ViewModel that exposes a `StateFlow`.
- Server feature flags fail closed: missing or unreadable means off.
