package hr.algebra.zatoninfo.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.model.PointOfInterest

private const val DB_NAME = "ZatonInfo.db"
private const val DB_VERSION = 2
public const val POI_TABLE_NAME = "PointOfInterest"
public const val BUS_TABLE_NAME = "BusTimetable"

private val CREATE_TABLE_POI = "create table $POI_TABLE_NAME(" +
        "${PointOfInterest::_id.name} integer primary key autoincrement, " +
        "${PointOfInterest::name.name} text not null, " +
        "${PointOfInterest::description.name} text not null, " +
        "${PointOfInterest::type.name} text not null, " +
        "${PointOfInterest::lat.name} double not null, " +
        "${PointOfInterest::lon.name} double not null," +
        "${PointOfInterest::pictures.name} text not null," +
        "${PointOfInterest::favorite.name} boolean not null" +
        ")"

private val CREATE_TABLE_BUS =
        "create table $BUS_TABLE_NAME(" +
        "${BusTimetableItem::busNumber.name} text not null," +
        "${BusTimetableItem::time.name} text not null," +
        "${BusTimetableItem::notice.name} text not null," +
        "${BusTimetableItem::direction.name} text not null," +
        "${BusTimetableItem::busStop.name} text not null" +
        ")"


private const val DROP1 = "drop table $POI_TABLE_NAME"
private const val DROP2 = "drop table $BUS_TABLE_NAME"

class ZatonSqlHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    ZatonRepository {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_POI)
        db.execSQL(CREATE_TABLE_BUS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP1)
        db.execSQL(DROP2)
        onCreate(db)
    }

    override fun delete(database: String, selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(database, selection, selectionArgs)

    override fun update(
        database: String,
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(database, values, selection, selectionArgs)

    override fun insert(database: String, values: ContentValues?) = writableDatabase.insert(database, null, values)

    override fun query(
        database: String,
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        database,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )


}