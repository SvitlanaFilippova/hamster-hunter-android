package ru.practicum.android.diploma.core.data.network

import CountriesResponse
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.filter.data.network.model.CountriesRequest
import ru.practicum.android.diploma.filter.data.network.model.RegionsRequest
import ru.practicum.android.diploma.filter.data.network.model.RegionsResponse
import ru.practicum.android.diploma.search.data.mapper.toQueryMap
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.util.Constants.HTTP_BAD_REQUEST
import ru.practicum.android.diploma.util.Constants.HTTP_SERVER_ERROR
import ru.practicum.android.diploma.util.Constants.HTTP_SUCCESS
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val hHApiService: HHApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        // val token = context.getString(R.string.bearer_token, TOKEN)

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> searchVacancies(dto)
                    is VacancyByIdRequest -> hHApiService.getVacancyById(
                        userAgent = USER_AGENT,
                        vacancyId = dto.id,
                    )

                    is CountriesRequest -> getCountries()
                    is RegionsRequest -> getRegions(dto.countryId)
                    else -> Response().apply { resultCode = HTTP_BAD_REQUEST }
                }
                response.apply { resultCode = HTTP_SUCCESS }
            } catch (e: HttpException) {
                logError(e)
                Response().apply { resultCode = HTTP_SERVER_ERROR }
            }
        }
    }

    private suspend fun searchVacancies(request: VacanciesSearchRequest): VacanciesSearchResponse {
        val titleSearchField = if (request.onlyInTitles == true) {
            hHApiService.getDictionaries(userAgent = USER_AGENT)
                .vacancySearchFields
                .find { it.id == "name" }
                ?.id
        } else {
            null
        }
        val queryMap = request.toQueryMap(titleSearchField)
        return hHApiService.search(USER_AGENT, queryMap)
    }

    private suspend fun getCountries(): CountriesResponse {
        val dtoList = hHApiService.getCountries()
        return CountriesResponse().apply {
            countriesList = dtoList
        }
    }

    private suspend fun getRegions(countryId: String): RegionsResponse {
        return hHApiService.getRegions(countryId)
    }

    private fun logError(e: Exception) {
        Log.d("DEBUG", "Ошибка в методе doRequest: ${e.message}")
    }

    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    private companion object {
        private const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
        private const val USER_AGENT =
            "HamsterHunter/1.0 (sergey_sh97@mail.ru)"
    }
}
