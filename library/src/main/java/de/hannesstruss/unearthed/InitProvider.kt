package de.hannesstruss.unearthed

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

internal class InitProvider : ContentProvider() {
  override fun onCreate(): Boolean {
    val ctx: Context = context ?: return false
    Unearthed.init(ctx.applicationContext as Application)
    return true
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    throw AssertionError("Not implemented")
  }

  override fun query(
    uri: Uri,
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    throw AssertionError("Not implemented")
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<String>?
  ): Int {
    throw AssertionError("Not implemented")
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    throw AssertionError("Not implemented")
  }

  override fun getType(uri: Uri): String? {
    throw AssertionError("Not implemented")
  }
}
