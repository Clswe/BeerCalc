package com.example.beercalc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.beercalc.R;
import com.example.beercalc.controller.BebidasController;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;

import java.util.ArrayList;

public class CalculadoraMLActivity extends AppCompatActivity {

    Context context;
    Spinner spMarcas;
    EditText edtMl, edtPreco;
    Button btnCalcular;
    ListView ltvLista;
    ArrayList<String> listagemCalculos;
    Bebida objBebida;
    Button btnLimpar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_ml);

        context = CalculadoraMLActivity.this;
        spMarcas = findViewById(R.id.spMarcas_ml);
        edtMl = findViewById(R.id.edtMl_ml);
        edtPreco = findViewById(R.id.edtPreco_ml);
        btnCalcular = findViewById(R.id.btnCalcular_ml);
        btnLimpar = findViewById(R.id.btnLimpar_ml);
        ltvLista = findViewById(R.id.lstDados_ml);


        listagemCalculos = new ArrayList<>();

        BebidasController controller = new BebidasController(context);

        final ArrayList<Bebida> lista = controller.lista();

        if (lista != null && lista.size() > 0) {
            ArrayAdapter<Bebida> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, lista);
            spMarcas.setAdapter(adapter);
        } else {
        }

        spMarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                objBebida = (Bebida) spMarcas.getSelectedItem();
                edtPreco.setText(String.valueOf(objBebida.getPreco()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {


                                               try {
                                                   double num1 = Double.parseDouble(edtPreco.getText().toString());
                                                   double num2 = Double.parseDouble(edtMl.getText().toString());
                                                   double divide = num1 / num2 * 100 ;

                                                   String mensagem = "100 ml  " + " custarão: " + Globais.formataDecimal(divide, 2, true) ;

                                                   Globais.exibirMensagemPersonalizada(context, "Aproveita", mensagem);

                                                   listagemCalculos.add(mensagem);
                                                   atualizaLista();

                                               } catch (Exception ex) {
                                                   Globais.exibirAlerta(context, "Esqueceu de preencher algo!");
                                               }
                                           }
                                       });

                btnLimpar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        edtMl.setText("");
                        edtPreco.setText("");



                        ltvLista.setAdapter(null);

                    }
                });

            }

            private void atualizaLista() {
                try {

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listagemCalculos);
                    ltvLista.setAdapter(adapter);

                } catch (Exception ex) {

                    Log.e("ERRO", ex.getMessage());

                }
            }
}







