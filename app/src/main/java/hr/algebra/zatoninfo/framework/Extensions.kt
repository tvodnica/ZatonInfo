package hr.algebra.zatoninfo.framework

import android.content.Context
import android.location.LocationManager
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.model.PointOfInterest


fun Context.fetchItems(): MutableList<PointOfInterest> {
    val pointsOfInterest = mutableListOf<PointOfInterest>()
    val cursor = contentResolver?.query(ZATON_PROVIDER_URI, null, null, null, null)
    while (cursor != null && cursor.moveToNext()) {
        pointsOfInterest.add(
            PointOfInterest(
                cursor.getLong(cursor.getColumnIndexOrThrow(PointOfInterest::_id.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::name.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::description.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::type.name)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lat.name)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lon.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::pictures.name)),
                cursor.getInt(cursor.getColumnIndexOrThrow(PointOfInterest::favorite.name)) == 1
            )
        )
    }
    return pointsOfInterest
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

fun Context.isGpsEnabled(): Boolean {

    val locationManager =
        getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}