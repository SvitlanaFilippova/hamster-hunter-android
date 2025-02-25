package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.api.FiltersRepository
import ru.practicum.android.diploma.filter.domain.model.FilterParameters

class FiltersInteractorImpl(private val repository: FiltersRepository) : FiltersInteractor {
    override fun save(filters: FilterParameters) {
        repository.save(filters)
    }

    override fun read(): FilterParameters {
        return repository.read()
    }

    override fun clear() {
        repository.clear()
    }
}
