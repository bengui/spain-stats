package me.benguiman.spainstats.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val DATA_METADATA = "DATOS_METADATAOPERACION"
const val VARIABLE_VALUES = "VALORES_VARIABLE"

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

    companion object {
        const val MUNICIPALITY_RESPONSE_TOTAL_PAGES = 17
    }

    // https://servicios.ine.es/wstempus/js/ES/DATOS_METADATAOPERACION/353?p=12&g1=19:&nult=3&page=1
    @GET("{language}/$DATA_METADATA/{operation}")
    suspend fun getDataByOperationFilterByVariable(
        @Path("language") language: Language = Language.ES,
        @Path("operation") operation: Int,
        @Query("g1") locationIdentifier: String,
        @Query("tip") type: Type = Type.M,
        @Query("nult") dataSeries: Int = 1,
        @Query("p") pValue: Int = 12
    ): List<DataEntryDto>


    //https://servicios.ine.es/wstempus/js/ES/VALORES_VARIABLE/8
    @GET("{language}/$VARIABLE_VALUES/{variableId}")
    suspend fun getVariableValues(
        @Path("language") language: Language = Language.ES,
        @Path("variableId") variableId: Int,
        @Query("page") page : Int = 1
    ): List<VariableValueDto>
}