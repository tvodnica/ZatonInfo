package hr.algebra.zatoninfo

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import getZatonRepository
import hr.algebra.zatoninfo.dao.BUS_TABLE_NAME
import hr.algebra.zatoninfo.dao.POI_TABLE_NAME
import hr.algebra.zatoninfo.dao.ZatonRepository

private const val AUTHORITY = "hr.algebra.zatoninfo.api.provider.bus"

val BUS_PROVIDER_URI = Uri.parse("content://$AUTHORITY")

class BusTimetableProvider : ContentProvider() {

    private lateinit var zatonRepository: ZatonRepository

    override fun onCreate(): Boolean {
        zatonRepository = getZatonRepository(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return zatonRepository.query(
            BUS_TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

    override fun getType(uri: Uri): String? {
        TODO()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        zatonRepository.insert(BUS_TABLE_NAME, values)
        return Uri.EMPTY
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return zatonRepository.delete(BUS_TABLE_NAME, selection, selectionArgs)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return zatonRepository.update(BUS_TABLE_NAME, values, selection, selectionArgs)
    }


}