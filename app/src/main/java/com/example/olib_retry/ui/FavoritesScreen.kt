package com.example.olib_retry.ui



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.olib_retry.viewmodel.BooksViewModel

@Composable
fun FavouritesScreenRoute(
    viewModel: BooksViewModel,
    onBack: () -> Unit,
    onOpenDetail: (String) -> Unit
) {
    FavouritesScreen(
        favourites = viewModel.favourites,
        isFavourite = viewModel::isFavourite,
        onBack = onBack,
        onToggleFavourite = viewModel::toggleFavourite,
        onBookClick = onOpenDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favourites: List<com.example.olib_retry.data.model.BookListItem>,
    isFavourite: (String) -> Boolean,
    onBack: () -> Unit,
    onToggleFavourite: (com.example.olib_retry.data.model.BookListItem) -> Unit,
    onBookClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourites") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (favourites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No favourites yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favourites, key = { it.workId }) { book ->
                    BookListCard(
                        book = book,
                        favourite = isFavourite(book.workId),
                        onToggleFavourite = { onToggleFavourite(book) },
                        onClick = { onBookClick(book.workId) }
                    )
                }
            }
        }
    }
}