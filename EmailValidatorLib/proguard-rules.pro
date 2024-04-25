# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

#============================================================================
#libraryjars  声明lib jar文件
#dontwarn 不提示警告 dontwarn是一个和keep可以说是形影不离,尤其是处理引入的library时.
#引入的library可能存在一些无法找到的引用和其他问题,在build时可能会发出警告,
#如果我们不进行处理,通常会导致build中止.
#因此为了保证build继续,我们需要使用dontwarn处理这些我们无法解决的library的警告.
#dontnote:指定不去输出打印该类产生的错误或遗漏
#keep 保留类和类中的成员，防止被混淆或移除
#keepnames 保留类和类中的成员，防止被混淆，成员没有被引用会被移除
#keepclassmembers 只保留类中的成员，防止被混淆或移除
#keepclassmembernames 只保留类中的成员，防止被混淆，成员没有引用会被移除
#keepclasseswithmembers 保留类和类中的成员，防止被混淆或移除，保留指明的成员
#keepclasseswithmembernames 保留类和类中的成员，防止被混淆，保留指明的成员，成员没有引用会被移除
#============================================================================

-keep class retrofit2.** {*;}

-dontwarn okhttp3**
-keep  class okhttp3.**{*;}
-dontwarn okio**
-keep  class okio.**{*;}
-keep  class rx.**{*;}


#----------------------Android通用-----------------

# 避免混淆Android基本组件，下面是兼容性比较高的规则
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
-keep interface android.support.** {*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
-dontwarn android.support.**

# 保留androidx下的所有类及其内部类
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-keep class com.google.android.material.** {*;}
-dontwarn androidx.**
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

# 保持Activity中与View相关方法不被混淆
-keepclassmembers class * extends android.app.Activity{
        public void *(android.view.View);
}

# 避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
        native <methods>;
}

# 避免混淆自定义控件类的get/set方法和构造函数
-keep public class * extends android.view.View{
        *** get*();
        void set*(***);
        public <init>(android.content.Context);
        public <init>(android.content.Context,android.util.AttributeSet);
        public <init>(android.content.Context,android.util.AttributeSet,int);
}
-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 避免混淆枚举类
-keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
}

# 避免混淆序列化类
# 不混淆Parcelable和它的实现子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
}

# 不混淆Serializable和它的实现子类、其成员变量
-keep public class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
}

# 资源ID不被混淆
-keep class **.R$* {*;}

# 回调函数事件不能混淆
-keepclassmembers class * {
        void *(**On*Event);
        void *(**On*Listener);
}

# Webview 相关不混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
        public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
        public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
        public void *(android.webkit.WebView, java.lang.String);
 }

# 使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
-keepclassmembers class * {
         public <init>(org.json.JSONObject);
}

#不混淆泛型
-keepattributes Signature

#避免混淆注解类
-dontwarn android.annotation
-keepattributes *Annotation*

#避免混淆内部类
-keepattributes InnerClasses

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
        public static *** v(...);
        public static *** d(...);
        public static *** i(...);
        public static *** w(...);
        public static *** e(...);
}

#kotlin 相关
-dontwarn kotlin.**
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepclasseswithmembers @kotlin.Metadata class * { *; }
-keepclassmembers class **.WhenMappings {
    <fields>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-keep class kotlinx.** { *; }
-keep interface kotlinx.** { *; }
-dontwarn kotlinx.**
-dontnote kotlinx.serialization.SerializationKt

-keep class org.jetbrains.** { *; }
-keep interface org.jetbrains.** { *; }
-dontwarn org.jetbrains.**

# Adjust 混淆
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }

#eventBus 表示对注解中的参数进行保留
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
    void *(**On*Event);
    void *(**On*Listener);
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

