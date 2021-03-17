# SPV Channels Android SDK

This repository contains SPV Channels Android SDK. 
It contains the Android client library for interacting with the server. 

## Requirements

This SDK can only be used as part of Android projects, and as such also requires a recent JDK to
build (should be provided by an installation of Android Studio).

In order to connect to a server with a self-signed certificate, put a certificate PEM with name "sslcert" in app/src/main/res/raw.
In order to use firebase create a file named app/firebase.gradle, with contents like following:

```groovy
ext.Firebase = [
    projectId: "<your project id>",
    appId: "<your app id>",
    apiKey: "<your api key>",
]
```

## Building

### Building from Android Studio

When using Android studio using the default build of app module will also build SDK, with no extra actions required.

### Building from terminal

1. Use terminal to navigate to root project directory

2. run `./gradlew :spv-channels:assemble` on *nix OS, or `gradle.bat :spv-channels:assemble` on Windows

You can also optionally use `:spv-channels:assembleDebug` or `:spv-channels:assembleRelease` to build debug or release builds respectively. Both are built if type is skipped.