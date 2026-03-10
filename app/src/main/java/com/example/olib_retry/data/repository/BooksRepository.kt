package com.example.olib_retry.data.repository



import com.example.olib_retry.data.model.BookDetail
import com.example.olib_retry.data.model.BookDoc
import com.example.olib_retry.data.model.BookListItem
import com.example.olib_retry.data.remote.OpenLibraryApi
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class BooksRepository(
    private val api: OpenLibraryApi
) {

    suspend fun searchBooks(query: String): List<BookListItem> {
        val response = retryRequest {
            api.searchWorks(query = query)
        }

        return response.docs.map { it.toListItem() }
    }

    suspend fun getBookDetail(
        workId: String,
        fallbackCoverId: Int?,
        fallbackAuthor: String,
        fallbackYear: String
    ): BookDetail {
        val dto = retryRequest {
            api.getWorkDetail(workId)
        }

        return BookDetail(
            workId = workId,
            title = dto.title.orEmpty().ifBlank { "No title" },
            description = extractDescription(dto.description),
            subjects = dto.subjects.orEmpty().take(10),
            coverId = fallbackCoverId,
            author = fallbackAuthor,
            year = fallbackYear
        )
    }

    private fun BookDoc.toListItem(): BookListItem {
        return BookListItem(
            workId = key.removePrefix("/works/"),
            title = title.orEmpty().ifBlank { "No title" },
            author = authorNames?.joinToString().orEmpty().ifBlank { "Unknown author" },
            year = firstPublishYear?.toString() ?: "Unknown year",
            coverId = coverId
        )
    }

    private fun extractDescription(element: JsonElement?): String {
        if (element == null || element.isJsonNull) return "No description"

        return when {
            element.isJsonPrimitive -> element.asString
            element.isJsonObject -> {
                val obj = element as JsonObject
                obj.get("value")?.asString ?: "No description"
            }
            else -> "No description"
        }
    }
}