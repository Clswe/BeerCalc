package com.example.beercalc.view;

import android.content.Context;
import android.os.Bundle;

import com.example.beercalc.R;
import com.example.beercalc.controller.BebidasController;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class CalculadoraQuantActivity extends AppCompatActivity {

    Context context;
    Spinner spMarcas;
    EditText edtValor, edtPreco;
    Button btnCalcular;
    Button btnLimpar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_quant);

        context = CalculadoraQuantActivity.this;
        spMarcas = findViewById(R.id.spMarcas);
        edtValor = findViewById(R.id.edtValor);
        edtPreco = findViewById(R.id.edtPreco);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnLimpar = findViewById(R.id.btnLimpar_);

        BebidasController controller = new BebidasController(context);
        ArrayList<Bebida> lista = controller.lista();
        if(lista != null && lista.size() > 0){
            ArrayAdapter<Bebida> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, lista);
            spMarcas.setAdapter(adapter);

        }else {
            Globais.exibirMensagemPersonalizada(context, "ERRO","Favor cadastrar uma cerveja");
        }

        spMarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bebida objeto = (Bebida) spMarcas.getSelectedItem();
                edtPreco.setText(String.valueOf(objeto.getPreco()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    double num1 = Double.parseDouble(edtValor.getText().toString());
                    double num2 = Double.parseDouble(edtPreco.getText().toString());
                    double divide = num1 / num2;

                    String mensagem = "Você e seus parceiros(as), vão beber: " + Globais.formataDecimal(divide, 0, false) + " Brejas";

                    Globais.exibirMensagemPersonalizada(context, "Aproveita", mensagem);

                }catch (Exception ex){
                    Globais.exibirAlerta(context, "Esqueceu de preencher algo!");
                    finish();
                }


            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtValor.setText(null);

            }
        });

    }

}
