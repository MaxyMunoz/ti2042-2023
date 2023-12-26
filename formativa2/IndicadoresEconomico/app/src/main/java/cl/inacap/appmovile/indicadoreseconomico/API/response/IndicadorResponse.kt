package cl.inacap.appmovile.indicadoreseconomico.API.response

import com.google.gson.annotations.SerializedName

data class IndicadorResponse(
    @SerializedName("codigo") var codigo:String,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("unidad_medida") var unidadMedida:String,
    @SerializedName("serie") var serie:List<Serie>
)

data class Serie(
    @SerializedName("fecha") var fecha:String,
    @SerializedName("valor") var valor:Double
)