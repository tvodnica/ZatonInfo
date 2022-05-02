package hr.algebra.zatoninfo.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.zatoninfo.model.PointOfInterest

private const val DB_NAME = "ZatonInfo.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "PointOfInterest"

private val CREATE = "create table $TABLE_NAME(" +
        "${PointOfInterest::_id.name} integer primary key autoincrement, " +
        "${PointOfInterest::name.name} text not null, " +
        "${PointOfInterest::description.name} text not null, " +
        "${PointOfInterest::type.name} text not null, " +
        "${PointOfInterest::lat.name} double not null, " +
        "${PointOfInterest::lon.name} double not null," +
        "${PointOfInterest::pictures.name} text not null," +
        "${PointOfInterest::favorite.name} boolean not null" +
        ")"

private const val DROP = "drop table $TABLE_NAME"

class ZatonSqlHelper(context: Context?)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), ZatonRepository {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?)
            = writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

    override fun insert(values: ContentValues?)
            = writableDatabase.insert(TABLE_NAME, null, values)

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder)


}