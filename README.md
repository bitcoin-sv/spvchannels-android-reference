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

## Distribution

Distribution that is currently set up is for maven local. You can publish a build by running `./gradlew :spv-channels:publishToMavenLocal` on *nix OS, or `gradle.bat :spv-channels:publishToMavenLocal` on Windows.
This will distribute the following:
- spv-channels-<version>.aar
- spv-channels-<version>-javadoc.jar
- spv-channels-<version>-sources.jar
- spv-channels-<version>.pom

After installing you can use the `mavenLocal()` declaration in repositories block:

```groovy
repositories {
    // Other repositories
    mavenCentral()
    mavenLocal()  // <- add this, best to add it to the end, so transitive dependencies are loaded from elsewhere
}
```

After adding the repository, the SDK can be used like any other gradle dependency:

```groovy
dependencies {
    // ... other libraries
    implementation 'io.bitcoinsv:spv-channels:<version>'
}
```

## SDK usage

In order to receive notifications when app is in background the SDK uses Firebase.
To enable this functionality to an app that does not already use FCM add the following to your
AndroidManifest.xml, inside the application tag:

```xml
<service
    android:name="io.bitcoinsv.spvchannels.firebase.DefaultSpvMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

For apps that already use FCM the above is not required.

The entry point for using SPV channels is always SpvChannelsSdk. This class can be used to create [Channel](#channel) 
object for channel management and [Messaging](#messaging) for using Channel and Messages APIs respectively.

In order to create an instance of the SpvChannelsSdk you will need:
- context
- firebaseConfig (to setup notifications)
- base url to the API you want to connect to

FirebaseConfig can be created by using the API key, Application ID and Project ID, available from google-services.json.
An example of initialization can be seen here:

```kotlin
val firebaseConfig = FirebaseConfig.Builder(context)
    .apiKey(BuildConfig.FIREBASE_API_KEY)
    .applicationId(BuildConfig.FIREBASE_APP_ID)
    .projectId(BuildConfig.FIREBASE_PROJECT_ID)
    .build()
```

Note that if your app is already using FCM, you can also use the existing messaging configuration for
a simpler initialization:

```kotlin
val firebaseConfig = FirebaseConfig.Builder(context)
    .messaging(Firebase.messaging)
    .build()
```

Finally, create an instance of SpvChannelsSdk:

```kotlin
val sdkInstance = SpvChannelsSdk(context, firebaseConfig, baseUrl)
```

Note that you can have multiple instances of SDK, in case you're connecting to multiple different URLs.

### Channel

After creating an instance of SpvChannelsSdk you can create a Channel object, using the
accountId, username and password:

```kotlin
val channel = sdkInstance.channelWithCredentials(accountId, username, password)
```

The Channel object contains methods specified in the channels documentation. All methods are declared as
suspending and are safe to call from the main thread by default.

### Messaging

After creating an instance of SpvChannelsSdk you can create a Messaging object, using the channelId,
token and (optionally) the encryption::

```kotlin
val messaging = sdkInstance.messagingWithToken(channelId, token)
```

As with the [Channel](#channel), methods here are declared in the official documentation and are declared
as suspending, and are safe to call from the main thread by default.


## Encryption

When creating an instance of Messaging you can optionally specify encryption, by default, NoOpEncryption is
used (resulting in cleartext messages). The SDK also specifies [LibSodiumEncryption](#libsodiumencryption)
If you want to specify a custom encryption simply implement the Encryption interface, specifying the
encrypt and decrypt functions. Both of these take full message payloads as a parameter.

### LibSodiumEncryption

LibSodiumEncryption can be created using the builder pattern. When creating a new instance the following methods are
available:

- withKeyPair: lets you specify public and secret key as raw bytes
- withSerializedKeyPair: JSON string, containing privateKey and publicKey fields as arrays. This corresponds
to the format used in the TS implementation
- encryptionKey: the encryption key to use. If it's not provided the publicKey will be used.
- generate: this will generate a new keypair to use. 

After creating an instance several additional methods are available:

- getEncryptionKey: returns the encryption key used in format "libsodium sealed_box <base 64 encoded key bytes>"
- exportKeys: returns a json with publicKey and privateKey in JSON format.