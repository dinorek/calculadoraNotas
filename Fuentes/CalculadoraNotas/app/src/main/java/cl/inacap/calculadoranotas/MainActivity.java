package cl.inacap.calculadoranotas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cl.inacap.calculadoranotas.DTO.Nota;

public class MainActivity extends AppCompatActivity {
    private EditText notaTxt;
    private EditText porcentajeTxt;
    private Button agregarBtn;
    private Button limpiarBtn;
    private ListView notasLv;
    private int porcentajeActual=0;
    private List<Nota> notas = new ArrayList<>();  //voy agregando notas a esta lista//
    private ArrayAdapter<Nota> adapter;
    private TextView promedioTxt;
    private LinearLayout promedioLl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.notaTxt = findViewById(R.id.notaTxt);
        this.promedioTxt = findViewById(R.id.promedioTxt);
        this.promedioLl = findViewById(R.id.promedioLl);
        this.porcentajeTxt = findViewById(R.id.notaTxt);
        this.agregarBtn = findViewById(R.id.agregarBtn);
        this.limpiarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LIMPIAR LOS EDITTEXT
                notaTxt.setText("");
                porcentajeTxt.setText("");
                //OCULTAR EL LINEARLAYO0UT DE RESULTADO
                promedioLl.setVisibility(View.INVISIBLE);
                //VOLVER EL PORCENTAJE ACUMULADO A 0
                porcentajeActual=0;
                //LIMPIAR LISTA
                notas.clear();
                adapter.notifyDataSetChanged();

            }
        });
        this.notasLv = findViewById(R.id.notasLv);
        //randeriza el tostring de la nota en la lista que seria el valor+ porcentaje
        this.adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, notas);
        this.notasLv.setAdapter(adapter);
        this.agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> errores = new ArrayList<>();
                String notaStr = notaTxt.getText().toString().trim();
                String porcStr = porcentajeTxt.getText().toString().trim();
                int porcentaje=0;
                double nota=0;
                try {
                    nota= Double.parseDouble(notaStr);
                    if(nota < 1.0 || nota> 7.0){
                        throw new NumberFormatException();
                    }
                }catch(NumberFormatException ex){
                    errores.add("-La nota debe ser entre 1.0 y 7.0");
                }
                try {
                    porcentaje =  Integer.parseInt(porcStr);
                    if(porcentaje > 1 || porcentaje < 100){
                        throw new NumberFormatException();
                    }
                }catch(NumberFormatException ex){
                    errores.add("-El porcentaje tiene que ser un numero entre 1 y 100");
                }
                if(errores.isEmpty()){
                    if(porcentaje + porcentajeActual > 100){
                        Toast.makeText(MainActivity.this, "El porcentaje no puede ser mayor a 100",Toast.LENGTH_SHORT).show();
                    }else{
                    //Ingresar notas
                    Nota n = new Nota();
                    n.setValor(nota);
                    n.setPorcentaje(porcentaje);
                    porcentajeActual+= porcentaje;
                    notas.add(n);
                    adapter.notifyDataSetChanged();
                    mostrarPromedio();
                    //Mostrar notas en el ListView
                    }

                }else {
                    mostrarErrores(errores);
                }


        }
        });
    }
    private void mostrarPromedio(){
        double promedio=0;
        for(Nota n: notas){
            promedio+= (n.getValor()* n.getPorcentaje()/100);
        }
        this.promedioTxt.setText(String.format("%.1f",promedio));
        if (promedio < 4.0){
            this.promedioTxt.setTextColor(ContextCompat.getColor(this,R.color.colorError));
    }else{
            this.promedioTxt.setTextColor(ContextCompat.getColor(this, R.color.colorExitoso));

        }
        this.promedioLl.setVisibility(View.VISIBLE);
        }


    private void mostrarErrores(List<String> errores){
        //1.Generar cadena de texto con los errores
        String mensaje = "";
        for(String e:errores){
            mensaje+= "-" + e + "\n";
        }
        //2.Mostrar un mensaje de alerta
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        //Chaining (encadenamiento)
        alertBuilder.setTitle("Error de la validación") //Define el titulo
                .setMessage(mensaje) //Define el mensaje del dialogo
                .setPositiveButton("Aceptar", null) //Agrega el boton aceptar y no hace nada
                .create() // crea el Alert
                .show(); // muestrar el alert


    }
}