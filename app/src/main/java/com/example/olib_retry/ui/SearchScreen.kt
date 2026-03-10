package com.example.olib_retry.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.olib_retry.data.UiState
import com.example.olib_retry.data.model.BookListItem
import com.example.olib_retry.viewmodel.BooksViewModel

@Composable
fun SearchScreenRoute(
    viewModel: BooksViewModel,
    onOpenDetail: (String) -> Unit,
    onOpenFavourites: () -> Unit
) {
    SearchScreen(
        query = viewModel.query,
        state = viewModel.listState,
        isFavourite = viewModel::isFavourite,
        onQueryChange = viewModel::updateQuery,
        onSearchClick = viewModel::searchBooks,
        onRetryClick = viewModel::retrySearch,
        onToggleFavourite = viewModel::toggleFavourite,
        onBookClick = { onOpenDetail(it.workId) },
        onOpenFavourites = onOpenFavourites
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: String,
    state: UiState<List<BookListItem>>,
    isFavourite: (String) -> Boolean,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRetryClick: () -> Unit,
    onToggleFavourite: (BookListItem) -> Unit,
    onBookClick: (BookListItem) -> Unit,
    onOpenFavourites: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OLIB Works") },
                actions = {
                    IconButton(onClick = onOpenFavourites) {
                        Icon(Icons.Default.List, contentDescription = "Favourites")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Search books") },
                    singleLine = true
                )

                Button(
                    onClick = onSearchClick,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Search")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                UiState.Idle -> Unit
                UiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                UiState.Empty -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nothing found")
                }

                is UiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = onRetryClick
                )

                is UiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data, key = { it.workId }) { book ->
                            BookListCard(
                                book = book,
                                favourite = isFavourite(book.workId),
                                onToggleFavourite = { onToggleFavourite(book) },
                                onClick = { onBookClick(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookListCard(
    book: BookListItem,
    favourite: Boolean,
    onToggleFavourite: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val imageUrl = book.coverId?.let {
                "https://covers.openlibrary.org/b/id/$it-M.jpg"
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = book.title,
                modifier = Modifier.size(width = 72.dp, height = 100.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = book.author)
                Text(text = book.year)
            }

            IconButton(onClick = onToggleFavourite) {
                if (favourite) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Remove favourite")
                } else {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Add favourite")
                }
            }
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}