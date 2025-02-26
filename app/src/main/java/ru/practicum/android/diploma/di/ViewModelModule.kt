package ru.practicum.android.diploma.di


import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)

    viewModelOf(::VacancyViewModel)

    viewModelOf(::FavoritesViewModel)

    viewModelOf(::FilterViewModel)
}
