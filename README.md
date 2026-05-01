# 8raya

Initial Android scaffold for the 8raya educational app.

## Stack

- Kotlin 2.2.21
- Android Gradle Plugin 8.13.2
- Jetpack Compose-ready app module
- Room 2.8.4 with KSP
- Hilt 2.57.2
- Retrofit 3.0.0 with Kotlin Serialization

## Current Scope

This pass implements the clean domain layer, Room entities/DAOs/database configuration, Hilt database bindings, mapper extensions, and tests. Feature UI, repository implementations, and Retrofit AI service implementation are intentionally left for later passes.

## Local Build

This machine has Android Studio's JBR installed, but `java` is not on `PATH`. For command-line Gradle runs, set `JAVA_HOME` first:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio1\jbr'
.\gradlew.bat testDebugUnitTest
```

Android Studio can also open the project directly and use its configured Gradle JDK.
