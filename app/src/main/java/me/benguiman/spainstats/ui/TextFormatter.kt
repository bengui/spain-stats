import android.icu.text.NumberFormat
import me.benguiman.spainstats.domain.models.DataType
import me.benguiman.spainstats.ui.MunicipalityStatUi
import java.util.*
import kotlin.math.roundToInt

fun formatValue(municipalityStat: MunicipalityStatUi): String {
    val locale = getCurrentLocale()
    val numberFormat = NumberFormat.getInstance(locale)
    val currencyFormat = NumberFormat.getInstance(locale)
    currencyFormat.maximumFractionDigits = 0
    val percentageFormat = NumberFormat.getPercentInstance()
    return when (municipalityStat.dataType) {
        DataType.INTEGER -> numberFormat.format(municipalityStat.value.roundToInt())
        DataType.DOUBLE -> numberFormat.format(municipalityStat.value)
        DataType.MONETARY -> "€ " + numberFormat.format((municipalityStat.value.toInt()))
        DataType.PERCENTAGE -> percentageFormat.format(municipalityStat.value / 100)
    }
}

private fun getCurrentLocale(): Locale {
    val currentLocale = Locale.getDefault()
    return if (currentLocale in listOf(Locale.US, Locale.CANADA, Locale.UK)) {
        Locale.UK
    } else {
        Locale("es", "ES")
    }
}