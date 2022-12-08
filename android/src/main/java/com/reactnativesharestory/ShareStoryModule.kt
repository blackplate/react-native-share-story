package com.reactnativesharestory

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.facebook.react.bridge.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ShareStoryModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private val instagramScheme = "com.instagram.android"
  private val MEDIA_TYPE_IMAGE = "image/*"
  private val UNKNOWN_ERROR = "An unknown error occured in RNStoryShare"

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

  fun getBitmapFromView(bmp: Bitmap?): Uri? {
    var bmpUri: Uri?
    try {
      val providerName = this.reactApplicationContext.packageName + ".fileprovider"
      val file = File(currentActivity?.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
      val out = FileOutputStream(file)
      bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
      out.close()
      bmpUri = FileProvider.getUriForFile(currentActivity!!.applicationContext, providerName, file)
    } catch (e: IOException) {
      e.printStackTrace()
      return null
    }
    return bmpUri
  }

  fun getBitmapFromURL (src: String?): Bitmap? {
    try {
      val url = URL(src)
      return BitmapFactory.decodeStream(url.openConnection().getInputStream())
    } catch (e: IOException) {
      println(e)
      return null
    }
  }

  @ReactMethod
  fun isInstagramAvailable(promise: Promise) {
    canOpenUrl(instagramScheme, promise)
  }

  @ReactMethod
  fun shareInstagramStory(config: ReadableMap, promise: Promise) {
    try {
      val intent = Intent("com.instagram.share.ADD_TO_STORY")
      val imageUrl =
        if (config.hasKey("imageUrl")) config.getString("imageUrl") else null
      val attributionLink = if (config.hasKey("attributionLink")) config.getString("attributionLink") else null

      if(imageUrl == null) {
        throw Exception("RNShareStory: No url provided");
      }

      val bitmap = getBitmapFromURL(imageUrl)

      if(bitmap == null) {
        throw Exception("Could not get bitmap from URL in RNShareStory");
      }

      val image = getBitmapFromView(bitmap)

      if(image == null) {
        throw Exception("Could not get bitmap URI");
      }
      
      if(attributionLink != null){
        intent.putExtra("content_url", attributionLink);
      }

      intent.setDataAndType(image, MEDIA_TYPE_IMAGE);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

      if (currentActivity?.packageManager?.resolveActivity(intent, 0) != null) {
        currentActivity!!.startActivityForResult(intent, 0)
        promise.resolve("success")
      } else {
        throw Exception("Couldn't open intent")
      }
    }catch (e: Exception){
      promise.reject(UNKNOWN_ERROR, e);
    }
  }
}
