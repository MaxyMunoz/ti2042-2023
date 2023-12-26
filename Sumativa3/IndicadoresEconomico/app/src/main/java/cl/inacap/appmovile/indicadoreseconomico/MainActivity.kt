package cl.inacap.appmovile.indicadoreseconomico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import cl.inacap.appmovile.indicadoreseconomico.API.IndicadoresApiAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    lateinit var dolarField: EditText
    lateinit var  euroField:EditText
    lateinit var  ufField: EditText
    lateinit var  utmField:EditText

    private  var indicadoresApi = IndicadoresApiAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dolarField= findViewById(R.id.Dolar_label)
        euroField = findViewById(R.id.Euro_label)
        ufField = findViewById(R.id.Fomento_label)
        utmField = findViewById(R.id.Mensual_label)

        runBlocking {
            showIndicador("dolar",dolarField)
            showIndicador("euro",euroField)
            showIndicador("uf",ufField)
            showIndicador("utm",utmField)
        }
    }
    private suspend fun showIndicador(tipo:String, field:EditText){
        val indicadorResponse = GlobalScope.async {
            indicadoresApi.getIndicador(tipo,"03-11-2023")
        }
        val indicador = indicadorResponse.await()
        val unidadMedida = indicador?.unidadMedida
        val valor = indicador?.serie?.get(0)?.valor
        field.setText("${valor} ${unidadMedida}")
    }
}