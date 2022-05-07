package hr.algebra.zatoninfo

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import getZatonRepository
import hr.algebra.zatoninfo.dao.POI_TABLE_NAME
import hr.algebra.zatoninfo.dao.ZatonRepository
import hr.algebra.zatoninfo.model.PointOfInterest
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.zatoninfo.api.provider"
private const val PATH = "poi"
private const val POIS = 10
private const val POI_ID = 20

val ZATON_PROVIDER_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, POIS)
    addURI(AUTHORITY, "$PATH/#", POI_ID)
    this
}

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
        return zatonRepository.query(
            POI_TABLE_NAME,
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
        val id = zatonRepository.insert(POI_TABLE_NAME, values)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return zatonRepository.delete(POI_TABLE_NAME, selection, selectionArgs)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            POIS -> return zatonRepository.update(POI_TABLE_NAME, values, selection, selectionArgs)
            POI_ID -> {
                uri.lastPathSegment?.let {
                    return zatonRepository.update(
                        POI_TABLE_NAME,
                        values,
                        "${PointOfInterest::_id.name}=?",
                        arrayOf(it)
                    )
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}