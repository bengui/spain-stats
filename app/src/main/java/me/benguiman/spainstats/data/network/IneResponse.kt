package me.benguiman.spainstats.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataEntryDto(
    @field:Json(name = "Nombre") val name: String,
    @field:Json(name = "MetaData") val metadata: List<MetadataDto>,
    @field:Json(name = "Data") val dataDto: List<DataDto>
)

@JsonClass(generateAdapter = true)
data class MetadataDto(
    @field:Json(name = "Id") val id: Int = 0,
    @field:Json(name = "Variable") val variable: VariableDto,
    @field:Json(name = "Nombre") val name: String,
    @field:Json(name = "Codigo") val code: String
)

@JsonClass(generateAdapter = true)
data class VariableDto(
    @field:Json(name = "Id") val id: Int = 0,
    @field:Json(name = "Nombre") val name: String,
    @field:Json(name = "Codigo") val code: String
)

@JsonClass(generateAdapter = true)
data class DataDto(
    @field:Json(name = "Fecha") val date: Long?,
    @field:Json(name = "Anyo") val year: Int?,
    @field:Json(name = "Valor") val value: Double?,
)

@JsonClass(generateAdapter = true)
data class VariableValueDto(
    @field:Json(name = "Id") val id: Int,
    @field:Json(name = "Nombre") val name: String,
    @field:Json(name = "Codigo") val code: String
)