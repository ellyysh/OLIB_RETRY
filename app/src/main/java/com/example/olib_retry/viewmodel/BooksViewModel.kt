package com.example.olib_retry.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olib_retry.data.UiState
import com.example.olib_retry.data.model.BookDetail
import com.example.olib_retry.data.model.BookListItem
import com.example.olib_retry.data.remote.NetworkModule
import com.example.olib_retry.data.repository.BooksRepository
import kotlinx.coroutines.launch

class BooksViewModel : ViewModel() {

    private val repository = BooksRepository(NetworkModule.api)

    var query by mutableStateOf("kotlin")
        private set

    var listState by mutableStateOf<UiState<List<BookListItem>>>(UiState.Idle)
        private set

    var detailState by mutableStateOf<UiState<BookDetail>>(UiState.Idle)
        private set

    private val _favourites = mutableStateListOf<BookListItem>()
    val favourites: List<BookListItem> = _favourites

    fun updateQuery(newValue: String) {
        query = newValue
    }

    fun searchBooks() {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            listState = UiState.Empty
            return
        }

        viewModelScope.launch {
            listState = UiState.Loading
            try {
                val books = repository.searchBooks(trimmed)
                listState = if (books.isEmpty()) UiState.Empty else UiState.Success(books)
            } catch (e: Exception) {
                listState = UiState.Error(e.message ?: "Failed to load books")
            }
        }
    }

    fun retrySearch() {
        searchBooks()
    }

    fun loadDetail(book: BookListItem) {
        viewModelScope.launch {
            detailState = UiState.Loading
            try {
                val detail = repository.getBookDetail(
                    workId = book.workId,
                    fallbackCoverId = book.coverId,
                    fallbackAuthor = book.author,
                    fallbackYear = book.year
                )
                detailState = UiState.Success(detail)
            } catch (e: Exception) {
                detailState = UiState.Error(e.message ?: "Failed to load details")
            }
        }
    }

    fun retryDetail(book: BookListItem) {
        loadDetail(book)
    }

    fun toggleFavourite(book: BookListItem) {
        val exists = _favourites.any { it.workId == book.workId }
        if (exists) {
            _favourites.removeAll { it.workId == book.workId }
        } else {
            _favourites.add(book)
        }
    }

    fun isFavourite(workId: String): Boolean {
        return _favourites.any { it.workId == workId }
    }

    init {
        searchBooks()
    }
}