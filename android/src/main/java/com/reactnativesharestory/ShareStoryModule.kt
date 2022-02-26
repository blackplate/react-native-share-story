package com.reactnativesharestory

import android.content.pm.PackageManager
import com.facebook.react.bridge.*

class ShareStoryModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private val instagramScheme = "com.instagram.android"

  override fun getName(): String {
    return "ShareStory"
  }

  private fun canOpenUrl(packageScheme: String, promise: Promise) {
    try {
      val activity = currentActivity
      activity?.packageManager?.getPackageInfo(packageScheme, PackageManager.GET_ACTIVITIES)
      promise.resolve(true)
    } catch (e: PackageManager.NameNotFoundException) {
      promise.resolve(false)
    } catch (e: Exception) {
      promise.reject(
        JSApplicationIllegalArgumentException(
          "Could not check if URL '" + packageScheme + "' can be opened: " + e.message
        )
      )
    }
  }

  @ReactMethod
  fun isInstagramAvailable(promise: Promise) {
    canOpenUrl(instagramScheme, promise)
  }

  @ReactMethod
  fun shareInstagramStory(config: ReadableMap, promise: Promise) {
    promise.resolve(true)
  }
}
