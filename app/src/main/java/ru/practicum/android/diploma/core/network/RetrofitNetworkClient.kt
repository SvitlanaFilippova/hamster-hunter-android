package ru.practicum.android.diploma.core.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancyByIdRequest
import ru.practicum.android.diploma.util.NetworkMonitor

class RetrofitNetworkClient(
    private val context: Context,
    private val hHApiService: HHApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO) {
            val userAgent = USER_AGENT
            val token = "Bearer $TOKEN"

            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> hHApiService.search(
                        userAgent = userAgent, token = token, body = dto,
                    )

                    is VacancyByIdRequest -> hHApiService.getVacancyById(
                        userAgent = userAgent,
                        token = token,
                        vacancyId = dto.id,
                    )

                    else -> Response().apply { resultCode = HTTP_BAD_REQUEST }
                }
                response.apply { resultCode = HTTP_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }


    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_SUCCESS = 200
        const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
        const val USER_AGENT = "HamsterHunter/1.0 (s.rubinets@gmail.com)"
    }
}
