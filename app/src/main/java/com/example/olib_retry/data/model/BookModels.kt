package com.example.olib_retry.data.model



import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("docs")
    val docs: List<BookDoc> = emptyList()
)

data class BookDoc(
    @SerializedName("key")
    val key: String, // example: /works/OL45883W

    @SerializedName("title")
    val title: String?,

    @SerializedName("author_name")
    val authorNames: List<String>?,

    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,

    @SerializedName("cover_i")
    val coverId: Int?
)

data class WorkDetailDto(
    @SerializedName("key")
    val key: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: JsonElement?,

    @SerializedName("subjects")
    val subjects: List<String>?
)

data class BookListItem(
    val workId: String,
    val title: String,
    val author: String,
    val year: String,
    val coverId: Int?
)

data class BookDetail(
    val workId: String,
    val title: String,
    val description: String,
    val subjects: List<String>,
    val coverId: Int?,
    val author: String,
    val year: String
)