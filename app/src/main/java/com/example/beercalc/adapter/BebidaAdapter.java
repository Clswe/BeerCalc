package com.example.beercalc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beercalc.R;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;
import com.example.beercalc.view.CadBebidasActivity;

import java.util.ArrayList;

public class BebidaAdapter extends ArrayAdapter<Bebida> {

        private final Context context;
        private final ArrayList<Bebida> elementos;

    public BebidaAdapter(Context context, ArrayList<Bebida> elementos){
            super(context, R.layout.item_lista_bebidas, elementos);
            this.context = context;
            this.elementos = elementos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            try{
                final Bebida objeto = elementos.get(position);

                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //toda vez que passa por um item da lista, os elementos são associados
                View rowView = inflater.inflate(R.layout.item_lista_bebidas, parent, false);

                TextView lblnome = rowView.findViewById(R.id.lblNome_lista_bebida);
                TextView lblPreco = rowView.findViewById(R.id.lblPreco_lista_bebida);

                lblnome.setText(String.valueOf(objeto.getNome()));
                lblPreco.setText(Globais.formataDecimal(objeto.getPreco(), 2, true));



                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent tela = new Intent(context, CadBebidasActivity.class);
                        tela.putExtra("id", objeto.getId());
                        context.startActivity(tela);
                    }
                });

                return rowView;

            }catch (Exception ex){
                Globais.exibirAlerta(context, ex.getMessage());
                return null;
            }

        }
}
