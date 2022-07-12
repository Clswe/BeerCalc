package com.example.beercalc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.beercalc.R;
import com.example.beercalc.adapter.BebidaAdapter;
import com.example.beercalc.controller.BebidasController;
import com.example.beercalc.model.Bebida;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListaBebidasActivity extends AppCompatActivity {

    ListView rcvLista;
    FloatingActionButton btnAdd;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bebidas);

        context = ListaBebidasActivity.this;
        rcvLista = findViewById(R.id.ltvBebidas_listaBebidas);
        btnAdd = findViewById(R.id.btnAddBebida_listaBebidas);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(context, CadBebidasActivity.class);
                startActivity(it);

            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista(){

        try{

            //buscar todos os usuarios e preencher em um List
           BebidasController controller = new BebidasController(context);
            ArrayList<Bebida> lista = controller.lista();
            if(lista != null){


                ArrayAdapter adapter = new BebidaAdapter(context, lista);

                rcvLista.setAdapter(adapter);

            }

        }catch (Exception ex){


        }
    }
}


