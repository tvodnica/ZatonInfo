import android.content.Context
import hr.algebra.zatoninfo.dao.ZatonSqlHelper

fun getZatonRepository(context: Context?) = ZatonSqlHelper(context)