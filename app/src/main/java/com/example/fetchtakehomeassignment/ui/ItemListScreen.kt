package com.example.fetchtakehomeassignment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fetchtakehomeassignment.ui.viewmodel.MyViewModel

@Composable
fun ItemListScreen(viewModel: MyViewModel = hiltViewModel()) {
    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchApiResults() }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(items) { item ->
                ItemCard(item)
            }
        }
    }
}

