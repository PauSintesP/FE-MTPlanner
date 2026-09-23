# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Room
-dontwarn androidx.room.**
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase

# Hilt
-keep class dagger.hilt.** { *; }
-keep,allowobfuscation,allowshrinking interface dagger.hilt.**

# Osmdroid
-keep class org.osmdroid.** { *; }
-keep class org.metalev.multitouch.controller.** { *; }
