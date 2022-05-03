package hr.algebra.zatoninfo.dao

import android.content.ContentValues
import android.database.Cursor

interface ZatonRepository {

    fun delete(database: String, selection: String?, selectionArgs: Array<String>?): Int

    fun update(
        database: String,
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun insert(database: String, values: ContentValues?): Long

    fun query(
        database: String,
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor
}