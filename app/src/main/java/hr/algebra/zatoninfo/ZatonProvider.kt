package hr.algebra.zatoninfo

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import getZatonRepository
import hr.algebra.zatoninfo.dao.ZatonRepository

private const val AUTHORITY = "hr.algebra.zatoninfo.api.provider"
private const val PATH = "all"

val ZATON_PROVIDER_URI = Uri.parse("content://$AUTHORITY")

class ZatonProvider : ContentProvider() {

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
        return zatonRepository.query(projection, selection, selectionArgs, sortOrder)
    }

    override fun getType(uri: Uri): String? {
        TODO()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        zatonRepository.insert(values)
        return Uri.EMPTY
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 1
    }


}