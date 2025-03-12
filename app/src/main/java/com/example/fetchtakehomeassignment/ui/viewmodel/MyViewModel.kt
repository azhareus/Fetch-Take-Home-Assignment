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

                // Sort by listId and then by numeric part of the name (e.g., "Item XXX")
                val sortedItems = filteredItems.sortedWith(
                    compareBy<ApiResult> { it.listId }
                        .thenBy { extractNumberFromName(it.name) }
                        .thenBy { it.name }  // Fallback to lexicographical order if name is not numeric
                )

                _items.value = sortedItems
            } catch (e: Exception) {
                // Handle exceptions like network errors, etc.
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    // Function to extract the numeric part from "Item XXX"
    private fun extractNumberFromName(name: String?): Int {
        val regex = "\\d+".toRegex()
        return regex.find(name ?: "")?.value?.toIntOrNull() ?: Int.MAX_VALUE
    }
}
