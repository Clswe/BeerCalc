package com.example.beercalc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.beercalc.database.DadosOpenHelper;
import com.example.beercalc.database.Tabelas;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;

import java.util.ArrayList;

public class BebidasController {

    private SQLiteDatabase conexao;
    private Context context;

    public BebidasController(Context context) {
        DadosOpenHelper banco = new DadosOpenHelper(context);
        this.conexao = banco.getWritableDatabase();
        this.context = context;
    }

    public boolean inserir(Bebida bebida) {
        try{

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", bebida.nome);
            contentValues.put("preco", bebida.preco);

            long result = conexao.insertOrThrow(Tabelas.TB_BEBIDAS, null, contentValues);
            if(result > 0){
                return true;
            }else{
                return false;
            }

        }catch (SQLException ex){
            Globais.exibirAlerta(context, ex.getMessage());
            return false;
        }catch (Exception ex){
            Globais.exibirAlerta(context, ex.getMessage());
            return false;
        }
    }



    public boolean alterar (Bebida cerveja) {

        try {


            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", cerveja.nome);
            contentValues.put("preco", cerveja.preco);

            String[] parametros = new String[1];
            parametros[0] = String.valueOf(cerveja.id);

            int ret = conexao.update("bebidas", contentValues, "id = ?", parametros);
            if(ret > 0){
                return true;
            }else{
                return false;
            }

        } catch (Exception ex) {
            Log.e("ERRO", "Loucura mano");
            return false;
        }
    }
    public Bebida buscar(int id){
        try{

            Bebida objeto = null;

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(Tabelas.TB_BEBIDAS);
            sql.append(" WHERE id = '"+ id +"'");

            Cursor resultado = conexao.rawQuery(sql.toString(), null);
            if(resultado.moveToNext()){
                objeto = new Bebida();
                objeto.setId(resultado.getInt(resultado.getColumnIndexOrThrow("id")));
                objeto.setNome(resultado.getString(resultado.getColumnIndexOrThrow("nome")));
                objeto.setPreco(resultado.getDouble(resultado.getColumnIndexOrThrow("preco")));
            }

            return objeto;


        }catch (Exception ex){
            Globais.exibirAlerta(context, ex.getMessage());
            return null;
        }
    }

    public boolean excluir(int id){
        try{

            String[] parametros = new String[1];
            parametros[0] = String.valueOf(id);

            conexao.delete(Tabelas.TB_BEBIDAS, "id = ?", parametros);

            return true;

        }catch (Exception ex){
            Globais.exibirAlerta(context, ex.getMessage());
            return false;
        }
    }


    public ArrayList<Bebida> lista(){

        ArrayList<Bebida> listagem = new ArrayList<>();
        try{

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(Tabelas.TB_BEBIDAS);

            Cursor resultado = conexao.rawQuery(sql.toString(), null);
            if(resultado.moveToFirst()){

                Bebida objeto;
                do{
                    objeto = new Bebida();
                    objeto.setId(resultado.getInt(resultado.getColumnIndexOrThrow("id")));
                    objeto.setNome(resultado.getString(resultado.getColumnIndexOrThrow("nome")));
                    objeto.setPreco(resultado.getDouble(resultado.getColumnIndexOrThrow("preco")));

                    listagem.add(objeto);

                }while (resultado.moveToNext());

            }

            return listagem;

        }catch (Exception ex){
            Globais.exibirAlerta(context, ex.getMessage());
            return listagem;
        }
    }


}

