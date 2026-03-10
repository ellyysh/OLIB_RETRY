package com.example.olib_retry.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.olib_retry.data.UiState
import com.example.olib_retry.data.model.BookDetail
import com.example.olib_retry.data.model.BookListItem
import com.example.olib_retry.viewmodel.BooksViewModel

@Composable
fun DetailScreenRoute(
    viewModel: BooksViewModel,
    workId: String,
    onBack: () -> Unit
) {
    val selectedBook = viewModel.favourites.find { it.workId == workId }
        ?: (viewModel.listState as? UiState.Success)?.data?.find { it.workId == workId }

    if (selectedBook != null) {
        if ((viewModel.detailState as? UiState.Success)?.data?.workId != workId) {
            viewModel.loadDetail(selectedBook)
        }
    }

    DetailScreen(
        state = viewModel.detailState,
        selectedBook = selectedBook,
        isFavourite = viewModel.isFavourite(workId),
        onBack = onBack,
        onRetry = {
            selectedBook?.let(viewModel::retryDetail)
        },
        onToggleFavourite = {
            selectedBook?.let(viewModel::toggleFavourite)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: UiState<BookDetail>,
    selectedBook: BookListItem?,
    isFavourite: Boolean,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleFavourite,
                        enabled = selectedBook != null
                    ) {
                        if (isFavourite) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Remove favourite")
                        } else {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Add favourite")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            selectedBook == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Book not found")
                }
            }

            state is UiState.Loading || state is UiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    ErrorContent(
                        message = state.message,
                        onRetry = onRetry
                    )
                }
            }

            state is UiState.Success -> {
                val book = state.data
                val imageUrl = book.coverId?.let {
                    "https://covers.openlibrary.org/b/id/$it-L.jpg"
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = book.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Author: ${book.author}")
                    Text(text = "First publish year: ${book.year}")

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = book.description)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Subjects",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (book.subjects.isEmpty()) {
                        Text("No subjects")
                    } else {
                        Text(book.subjects.joinToString())
                    }
                }
            }

            state is UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No detail")
                }
            }
        }
    }
}