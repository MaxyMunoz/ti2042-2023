package cl.inacap.appmovile.indicadoreseconomico.API

import cl.inacap.appmovile.indicadoreseconomico.API.response.IndicadorResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface IndicadoresAPI {
    @GET("{tipo}/{fecha}")
    fun getIndicador(
        @Path("tipo") tipo:String,
        @Path("fecha") fecha:String
    ): Call<IndicadorResponse>
}