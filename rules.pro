# Tinylog
# Source: https://tinylog.org/v2/configuration/#proguard
-keepnames interface org.tinylog.**
-keepnames class * implements org.tinylog.**
-keepclassmembers class * implements org.tinylog.** { <init>(...); }
-dontwarn dalvik.system.VMStack
-dontwarn java.lang.**
-dontwarn javax.naming.**
-dontwarn sun.reflect.Reflection

# Tinylog <-> SLF4J bridge
-keep class org.tinylog.** { *; }

# Java native access
# Source: https://github.com/java-native-access/jna/issues/1187#issuecomment-626251894
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

# PeParse
-dontwarn org.eclipse.**
-dontwarn io.netty.**
-dontwarn ch.qos.logback.**
-dontwarn javafx.application.**
-dontwarn com.sun.javafx.**
-dontwarn net.jodah.typetools.**
-dontwarn org.lwjgl.**
-dontwarn org.bouncycastle.**
-dontwarn com.fasterxml.**
-dontwarn android.os.**
-dontwarn android.util.**

# Kotlin serialization
# Source: https://github.com/Kotlin/kotlinx.serialization/tree/33104957873448189eb242413646b58883623e0a#android

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class **
-keepclasseswithmembers class acidicoala.koalageddon.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Ktor
-keep class io.ktor.client.engine.cio.**
