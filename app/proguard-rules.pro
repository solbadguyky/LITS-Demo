# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in O:\APP\android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontobfuscate #if obfuscation is enabled, we get "java.lang.NoSuchFieldException: producerIndex" (wtf?)
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Picasso
-dontwarn com.squareup.okhttp.**

### PICASSO

# Checks for OkHttp versions on the classpath to determine Downloader to use.
-dontnote com.squareup.picasso.Utils
# Downloader used only when OkHttp 2.x is present on the classpath.
-dontwarn com.squareup.picasso.OkHttpDownloader
# Downloader used only when OkHttp 3.x is present on the classpath.
-dontwarn com.squareup.picasso.OkHttp3Downloader


### OKHTTP

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote okhttp3.internal.Platform
-dontnote com.squareup.okhttp.internal.Platform

### OKIO

# java.nio.file.* usage which cannot be used at runtime. Animal sniffer annotation.
-dontwarn okio.Okio
# JDK 7-only method which is @hide on Android. Animal sniffer annotation.
-dontwarn okio.DeflaterSin

-dontwarn commons-logging
-dontwarn org.apache.httpcomponents

# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
-dontwarn com.squareup.okhttp.internal.huc.**
# Ignore warnings: https://github.com/square/okio/issues/60
-dontwarn okio.**
# Ignore warnings:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn com.robotium.**
# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}
# ------------------- TEST DEPENDENCIES -------------------
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**

-keep class org.hamcrest.** {
   *;
}

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**
#----------------------------------------------------------

-dontwarn org.bouncycastle.x509.util.LDAPStoreHelper
-dontwarn org.bouncycastle.jce.provider.X509LDAPCertStoreSpi
-dontwarn org.bouncycastle.util.io.pem.AllTests
-dontwarn org.bouncycastle.util.AllTests
-dontwarn org.bouncycastle.x509.X509V3CertificateGenerator
-dontwarn org.bouncycastle.jce.provider.BouncyCastleProvider
-dontwarn org.objenesis.instantiator.sun.**
-dontwarn org.fest.assertions.internal.**
-dontwarn org.fest.util.Introspection
-dontwarn org.fest.assertions.Introspection
-dontwarn org.mockito
-dontwarn org.slf4j.LoggerFactory
-dontwarn org.slf4j.MDC
-dontwarn org.slf4j.MarkerFactory

#iconic
-keep class .R
-keep class **.R$* {
    <fields>;
}
-keep public enum com.mikepenz.google_material_typeface_library.GoogleMaterial$** {
    **[] $VALUES;
    public *;
}

#---------------- Play Services-------------------
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final *** CREATOR;
}

-keep @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <methods>;
}

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}

-dontwarn android.security.NetworkSecurityPolicy
#---------------------------------------------------------
# Basic ProGuard rules for Firebase Android SDK 2.0.0+
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**

-keep class org.ocpsoft.prettytime.i18n.**