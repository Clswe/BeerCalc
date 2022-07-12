package com.example.beercalc.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beercalc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Globais {

    public static boolean isConnected(Context cont){
        ConnectivityManager conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conmag != null ) {
            conmag.getActiveNetworkInfo();

            //Verifica internet pela WIFI
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                return true;
            }

            //Verifica se tem internet móvel
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                return true;
            }
        }

        return false;
    }


    public static JSONObject consultaTempo(double lat, double lon) {

        StringBuilder stringBuilder = new StringBuilder();

        try {

            String utl_tempo = "https://weather.contrateumdev.com.br/api/weather?lat=" + lat + "&lon=" + lon;
            URL mUrl = new URL(utl_tempo);

            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "close")
                    .url(mUrl)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }


        } catch (IllegalArgumentException ex) {
            Log.d("ERRO", ex.getMessage());
        } catch (IOException ex) {
            Log.d("ERRO", ex.getMessage());
        } catch (Exception ex){
            Log.d("ERRO", ex.getMessage());
        }

        return null;

    }

    public static void exibirAlerta(Context context, String texto){
        try{

            Toast.makeText(context, texto, Toast.LENGTH_LONG).show();

        }catch (Exception ex){
            Log.e("ERRO", ex.getMessage());
        }
    }

    public static void exibirMensagem(Context context, String titulo, String mensagem){
        try{

            AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
            dialogo.setTitle(titulo);
            dialogo.setMessage(mensagem);
            dialogo.setNeutralButton("OK", null);

            dialogo.show();

        }catch (Exception ex){

        }
    }

    public static void exibirMensagemPersonalizada(Context context, String titulo, String mensagem) {
        try {

            final Dialog dialog = new Dialog(context, R.style.themeDialog);
            dialog.setContentView(R.layout.dialog_mensagem);
            dialog.setCancelable(false);

            final TextView lblTitulo   = dialog.findViewById(R.id.lblTitulo_dialog);
            TextView lblMensagem = dialog.findViewById(R.id.lblMensagem_dialog);
            Button btnFechar     = dialog.findViewById(R.id.btnFechar_dialog);

            lblTitulo.setText(titulo);
            lblMensagem.setText(mensagem);

            btnFechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });

            dialog.show();


        }catch (Exception ex) {
            Log.e("DIALOG", ex.getMessage());
        }

    }

    public static String formataDecimal(double valor, int casasDecimal, boolean sifrao) {
        try {
            DecimalFormat df;
            String strDecimal = "###,###,###,###,##0";
            if (casasDecimal > 0)
                strDecimal += ".";
            for (int i = 0; i < casasDecimal; i++) {
                strDecimal += "0";
            }

            df = new DecimalFormat(strDecimal);
            String formatado = (sifrao ? "R$ " : "") + df.format(valor);
            return formatado;

        } catch (Exception ex) {
            return "";
        }
    }

    public static boolean isCampoVazio (String valor) {

        //return true se estiver vazio
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return  resultado;
    }

}
