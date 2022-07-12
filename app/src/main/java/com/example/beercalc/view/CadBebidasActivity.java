package com.example.beercalc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beercalc.R;
import com.example.beercalc.controller.BebidasController;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;

public class CadBebidasActivity extends AppCompatActivity {

    private Context context;
    private EditText edtNome;
    private EditText edtPreco;
    private BebidasController bebidasController;
    private Button btnExcluir;
    private int codigo;
    boolean ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_bebidas);

        context = CadBebidasActivity.this;
        edtNome = findViewById(R.id.edtNomeCerveja);
        edtPreco = findViewById(R.id.edtValorCerveja);
        btnExcluir = findViewById(R.id.btnExcluir);

        codigo = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("id")) {

            codigo = bundle.getInt("id");
            bebidasController = new BebidasController(context);
            Bebida objeto = bebidasController.buscar(codigo);
            preencheCampos(objeto);

        }else {
            btnExcluir.setVisibility(View.INVISIBLE);
        }

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bebidasController = new BebidasController(context);
                boolean retorno = bebidasController.excluir(codigo);
                if (retorno) {
                    Globais.exibirMensagemPersonalizada(context, "SUCESSO","Bebida excluída com sucesso.");
                    finish();

                }





                }
        });


    }

  public boolean validaDados() {

        boolean retorno = true;
        String nome = edtNome.getText().toString();
        String preco = edtPreco.getText().toString();

        if (Globais.isCampoVazio(nome)) {
            edtNome.requestFocus();
            retorno = false;
        }

        if(Globais.isCampoVazio(preco) || preco.equals("0")){
            edtPreco.requestFocus();
            retorno = false;
        }

        if (!retorno) {
           Globais.exibirMensagem(context, "Aviso", "Há campos inválidos ou em branco");
           return retorno;
        }

       return retorno;

 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadbebidas, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_salvar:

                if (validaDados()) {

                    Bebida objeto = new Bebida();
                    objeto.setNome(edtNome.getText().toString());
                    objeto.setPreco(Double.parseDouble(edtPreco.getText().toString()));

                    BebidasController controller = new BebidasController(context);

                    boolean ret;

                    if (codigo == 0) {
                        ret = controller.inserir(objeto);

                    } else {
                        objeto.setId(codigo);
                        ret = controller.alterar(objeto);

                    }

                } else {

                    break;
                }

            case R.id.action_cancelar:
                finish();
        }



        return super.onOptionsItemSelected(item);
    }

    public void preencheCampos(Bebida objeto) {
        try {

            edtNome.setText(String.valueOf(objeto.getNome()));
            edtPreco.setText(String.valueOf(objeto.getPreco()));

        }catch (Exception ex) {

            Globais.exibirMensagem(context, "ERRO", "FALHOU");
        }

    }
}

