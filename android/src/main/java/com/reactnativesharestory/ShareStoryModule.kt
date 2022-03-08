package com.reactnativesharestory

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.facebook.react.bridge.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
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
    var bmpUri: Uri? = null
    try {
      val providerName = this.reactApplicationContext.packageName + ".fileprovider"
      val file = File(currentActivity?.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
      val out = FileOutputStream(file)
      bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
      out.close()
      bmpUri = FileProvider.getUriForFile(currentActivity!!.applicationContext, providerName, file)
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return bmpUri
  }

  fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
      val url = URL(src)
      val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
      connection.setDoInput(true)
      connection.connect()
      val input: InputStream = connection.getInputStream()
      BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
      // Log exception
      null
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
      val bitmap = getBitmapFromURL("https://images-staging.pushthatbutton.co.uk/7a98265c-34d9-4135-88cd-11fbe4a4a58e.jpeg")

      intent.setDataAndType(getBitmapFromView(bitmap), MEDIA_TYPE_IMAGE);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

      if (currentActivity?.packageManager?.resolveActivity(intent, 0) != null) {
        currentActivity!!.startActivityForResult(intent, 0)
        promise.resolve("success")
      } else {
        throw java.lang.Exception("Couldn't open intent")
      }
    }catch (e: Exception){
      promise.reject(UNKNOWN_ERROR, e);
    }
  }
}

//  fun shareImageFromURI(url: String?) {
//    try {
//    Picasso.get().load(url).into(object : Target {
//      override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//        val intent = Intent("com.instagram.share.ADD_TO_STORY")
//        intent.setDataAndType(getBitmapFromView(bitmap), MEDIA_TYPE_IMAGE);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        if (currentActivity?.getPackageManager()?.resolveActivity(intent, 0) != null) {
//          currentActivity!!.startActivityForResult(intent, 0)
//        } else {
//          throw java.lang.Exception("Couldn't open intent")
//        }
//      }
//      override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }
//      override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
//        throw java.lang.Exception("Couldn't open image")
//      }
//    })
//    }catch (ex: IOException) {
//      ex.printStackTrace();
//    }
//  }
