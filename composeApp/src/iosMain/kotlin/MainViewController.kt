import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import dev.johnoreilly.climatetrace.remote.ClimateTraceApi
import dev.johnoreilly.climatetrace.remote.Country
import dev.johnoreilly.climatetrace.remote.CountryAssetEmissionsInfo
import dev.johnoreilly.climatetrace.remote.CountryEmissionsInfo
import dev.johnoreilly.climatetrace.ui.CountryInfoDetailedView
import dev.johnoreilly.climatetrace.ui.CountryListView

fun CountryListViewController(onCountryClicked: (country: Country) -> Unit) = ComposeUIViewController {
    val climateTraceApi = remember { ClimateTraceApi() }
    var countryList by remember { mutableStateOf(emptyList<Country>()) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(true) {
        countryList = climateTraceApi.fetchCountries().sortedBy { it.name }
    }

    CountryListView(countryList, selectedCountry) {
        selectedCountry = it
        onCountryClicked(it)
    }
}


fun CountryInfoDetailedViewController(country: Country) = ComposeUIViewController {
    val climateTraceApi = remember { ClimateTraceApi() }
    var countryEmissionInfo by remember { mutableStateOf<CountryEmissionsInfo?>(null) }
    var countryAssetEmissons by remember { mutableStateOf<List<CountryAssetEmissionsInfo>?>(null) }

    LaunchedEffect(country) {
        val countryEmissionInfoList = climateTraceApi.fetchCountryEmissionsInfo(country.alpha3)
        countryEmissionInfo = countryEmissionInfoList[0]
        countryAssetEmissons = climateTraceApi.fetchCountryAssetEmissionsInfo(country.alpha3)[country.alpha3]
    }

    CountryInfoDetailedView(country, countryEmissionInfo, countryAssetEmissons)
}
