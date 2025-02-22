package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyByIdResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun search(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Query("text") text: String,
        @Query("page") page: Int
    ): VacanciesSearchResponse

    @GET("vacancies/{vacancy_id}")
    fun getVacancyById(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Path("vacancy_id") vacancyId: String, // Path parameter
    ): VacancyByIdResponse
}
