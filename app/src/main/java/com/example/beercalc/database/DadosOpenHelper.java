package com.example.beercalc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.beercalc.tools.Globais;

public class DadosOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NM_BANCO = "banco";
    private Context context;


    public DadosOpenHelper(Context context) {
        super(context, NM_BANCO, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            StringBuilder sqlBebidas = new StringBuilder();
            sqlBebidas.append(" CREATE TABLE IF NOT EXISTS  ");
            sqlBebidas.append(Tabelas.TB_BEBIDAS);
            sqlBebidas.append("(");
            sqlBebidas.append(" id INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sqlBebidas.append(" nome VARCHAR (30) NOT NULL, ");
            sqlBebidas.append(" preco DOUBLE NOT NULL  ");
            sqlBebidas.append(")");
            db.execSQL(sqlBebidas.toString());


        } catch (Exception ex) {

            Globais.exibirMensagem(context, ex.getMessage(),"ERRO");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
