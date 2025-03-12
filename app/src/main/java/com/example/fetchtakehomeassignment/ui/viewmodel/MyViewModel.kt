package com.example.fetchtakehomeassignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchtakehomeassignment.model.ApiResult
import com.example.fetchtakehomeassignment.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<ApiResult>>(emptyList())
    val items: StateFlow<List<ApiResult>> = _items.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun fetchApiResults() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val result = apiRepository.fetchResults()

                // Filter out items with null or blank name
                val filteredItems = result.filter { !it.name.isNullOrBlank() }

                // Sort by listId and then name
                val sortedItems =
                    filteredItems.sortedWith(compareBy<ApiResult> { it.listId }.thenBy { it.name })

                _items.value = sortedItems
            } catch (e: Exception) {
                // Handle exceptions like network errors, etc.
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    companion object {
        const val TAG = "ViewModel"
    }
}
