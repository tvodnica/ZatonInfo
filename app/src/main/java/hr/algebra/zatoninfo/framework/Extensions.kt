package hr.algebra.zatoninfo.framework

import android.content.Context
import android.database.Cursor
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.model.PointOfInterest


fun Context.fetchPoisWithoutActivities(): MutableList<PointOfInterest> {

    val pointsOfInterest = mutableListOf<PointOfInterest>()
    val cursor = contentResolver?.query(ZATON_PROVIDER_URI, null, null, null, null)

    while (cursor != null && cursor.moveToNext()) {

        if (cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::type.name)) != getString(
                R.string.activity
            )
        ) {
            loadPoiData(cursor, pointsOfInterest)
        }
    }
    return pointsOfInterest
}
fun Context.fetchActivities(): MutableList<PointOfInterest> {

    val pointsOfInterest = mutableListOf<PointOfInterest>()
    val cursor = contentResolver?.query(ZATON_PROVIDER_URI, null, null, null, null)

    while (cursor != null && cursor.moveToNext()) {

        if (cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::type.name)) == getString(
                R.string.activity
            )
        ) {
            loadPoiData(cursor, pointsOfInterest)
        }
    }
    return pointsOfInterest
}
fun Context.fetchAllPointsOfInterest(): MutableList<PointOfInterest> {

    val allPointsOfInterest = mutableListOf<PointOfInterest>()

    fetchPoisWithoutActivities().forEach {
        allPointsOfInterest.add(it)
    }
    fetchActivities().forEach {
        allPointsOfInterest.add(it)
    }

    return allPointsOfInterest
}
fun Context.fetchBusTimetable(): MutableList<BusTimetableItem> {
    val busTimetable = mutableListOf<BusTimetableItem>()
    val cursor = contentResolver?.query(BUS_PROVIDER_URI, null, null, null, null)
    while (cursor != null && cursor.moveToNext()) {
        busTimetable.add(
            BusTimetableItem(
                cursor.getString(cursor.getColumnIndexOrThrow(BusTimetableItem::busNumber.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(BusTimetableItem::time.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(BusTimetableItem::notice.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(BusTimetableItem::direction.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(BusTimetableItem::busStop.name))
            )
        )
    }
    return busTimetable
}

fun loadPoiData(cursor: Cursor, pointsOfInterest: MutableList<PointOfInterest>) {

    val pictures = mutableListOf<String>()

    val localPicturePaths =
        cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::pictures.name))

    localPicturePaths.split("\n").forEach {
        pictures.add(it)
    }

    pointsOfInterest.add(
        PointOfInterest(
            cursor.getLong(cursor.getColumnIndexOrThrow(PointOfInterest::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::name.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::description.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::type.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lat.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lon.name)),
            pictures,
            cursor.getInt(cursor.getColumnIndexOrThrow(PointOfInterest::favorite.name)) == 1
        )
    )

}

fun Context.isGpsEnabled(): Boolean {

    val locationManager =
        getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
fun Context.hasInternetAccess(): Boolean {

    val connectivityManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}
fun Context.showErrorIfGpsDisabled(){
    if (!isGpsEnabled()) {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.error)
            setMessage(getString(R.string.noGpsErrorMessage))
            setPositiveButton(R.string.ok, null)
            show()
        }
    }
}

fun Context.getPreferences() = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.getPoiDataVersion() = getPreferences().getInt(POI_VERSION, 0)
fun Context.getBusDataVersion() = getPreferences().getInt(BUS_VERSION, 0)
fun Context.setPoiDataVersion(int: Int) = getPreferences().edit().putInt(POI_VERSION, int).apply()
fun Context.setBusDataVersion(int: Int) = getPreferences().edit().putInt(BUS_VERSION, int).apply()


fun Context.getPoiDataExists() = getPreferences().getBoolean(POI_DATA_EXISTS, false)
fun Context.getBusDataExists() = getPreferences().getBoolean(BUS_DATA_EXISTS, false)
fun Context.setPoiDataExists(boolean: Boolean) = getPreferences().edit().putBoolean(POI_DATA_EXISTS, boolean).apply()
fun Context.setBusDataExists(boolean: Boolean) = getPreferences().edit().putBoolean(BUS_DATA_EXISTS, boolean).apply()
