package me.benguiman.spainstats.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val DATA_METADATA = "DATOS_METADATAOPERACION"

enum class Language {
    ES, EN
}

/**
 * M = Adds metadata
 * A = Friendly response
 * AM = Both M and A.
 */
enum class Type {
    M,
    A,
    AM
}

interface IneService {

    // https://servicios.ine.es/wstempus/js/ES/DATOS_METADATAOPERACION/353?p=12&g1=19:&nult=3&page=1
    @GET("{language}/$DATA_METADATA/{operation}")
    fun getDataByGeographicLocation(
        @Path("operation") language: Language = Language.ES,
        @Path("operation") operation: String,
        @Query("g1") locationIdentifier: String,
        @Query("tip") type: Type = Type.M,
        @Query("nult") dataSeries: Int = 1,
        @Query("p") pValue: Int = 12
    )
}