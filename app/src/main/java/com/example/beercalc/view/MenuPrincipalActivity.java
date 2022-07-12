package com.example.beercalc.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.beercalc.R;
import com.example.beercalc.controller.BebidasController;
import com.example.beercalc.model.Bebida;
import com.example.beercalc.tools.Globais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity implements LocationListener {

    CardView cardViewCalcularPorML;
    CardView cardViewCalcularQuantidade;
    CardView cardViewListaBebidas;
    CardView cardViewTempo;
    Context context;
    TextView lblCondicao;
    TextView lblTempoAtual;
    ImageView imgTempo;
    TextView lblTempMaxMin;
    TextView lblFrase_atual;


    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        cardViewCalcularPorML = findViewById(R.id.cardViewCalcularPorML);
        cardViewCalcularQuantidade = findViewById(R.id.cardViewCalcularQuantidade);
        cardViewListaBebidas = findViewById(R.id.cardViewListaBebidas);
        cardViewTempo = findViewById(R.id.cardViewTempo);
        cardViewTempo.setVisibility(View.GONE);
        lblCondicao = findViewById(R.id.lblCondicao_atual);
        lblTempoAtual = findViewById(R.id.lblTempo_atual);
        imgTempo = findViewById(R.id.imgTempo);
        lblTempMaxMin = findViewById(R.id.lblTempMaxMin);
        lblFrase_atual = findViewById(R.id.lblFrase_atual);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        context = MenuPrincipalActivity.this;

        //permissões
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        cardViewTempo.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {

                                                 chamadaTempo();
                                             }
                                         }
        );

        cardViewCalcularPorML.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {

                                                         Intent it = new Intent(context, CalculadoraMLActivity.class);
                                                         startActivity(it);

                                                     }

                                                 }
        );

        cardViewCalcularQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BebidasController controller = new BebidasController(context);
                ArrayList<Bebida> lista = controller.lista();
                if (lista != null && lista.size() > 0) {

                    Intent it = new Intent(context, CalculadoraQuantActivity.class);
                    startActivity(it);

                } else {
                    Globais.exibirMensagemPersonalizada(context, "ERRO", "Favor cadastrar uma cerveja");
                }


            }
        });

        cardViewListaBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(context, ListaBebidasActivity.class);
                startActivity(it);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {


            chamadaTempo();

        } catch (Exception ex) {
            Log.e("ERRO", ex.getMessage());
        }

    }


    private void chamadaTempo() {
        try {
            if (Globais.isConnected(context)) {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // getting GPS status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                Location location = null;

                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }else   if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //caso não tenha permissão, cai fora da função
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                     location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                }else {
                    Globais.exibirAlerta(context, "Para uma melhor experiência, ative sua localização");
                }

                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null && location != null) {

                    new ObterTempoTask().execute(location);

                    //Log.d("TEMPO", objeto_tempo.toString());
                }


            }else{
                Globais.exibirAlerta(context, "Você está desconectado");
                cardViewTempo.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Log.e("ERRO", ex.getMessage());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (location != null) {
            if (location.getLatitude() != 0 || location.getLongitude() != 0) {

                //JSONObject objeto_tempo = Globais.consultaTempo(location.getLatitude(), location.getLongitude());

               // Log.d("TEMPO", objeto_tempo.toString());

            }
        }
    }

    private class ObterTempoTask extends AsyncTask<Location, Void, JSONObject> {

        Bitmap mIcon11 = null;
        protected JSONObject doInBackground(Location... location) {
            try {

                JSONObject objeto_tempo = Globais.consultaTempo(location[0].getLatitude(), location[0].getLongitude());

                JSONArray weather = null;
                weather = objeto_tempo.getJSONArray("weather");

                JSONObject itemWeather = weather.getJSONObject(0);
                final String icone = itemWeather.getString("icon");
                String url_imagem = "https://openweathermap.org/img/wn/" + icone + "@4x.png";
                //Bitmap mIcon11 = null;

                InputStream in = new java.net.URL(url_imagem).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

                return objeto_tempo;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }

        }

        protected void onPostExecute(JSONObject result) {

            Log.d("TEMPO", result.toString());

            try {
                if(result != null) {

                    cardViewTempo.setVisibility(View.VISIBLE);

                    JSONArray weather = result.getJSONArray("weather");
                    JSONObject itemWeather = weather.getJSONObject(0);
                    JSONObject temperaturas = result.getJSONObject("main");

                    final String icone = itemWeather.getString("icon");
                    //   final String icone = "50d";

                    String tempo = itemWeather.get("description").toString();
                    lblCondicao.setText(tempo);
                    int temp = temperaturas.getInt("temp");
                    int max = (temperaturas.getInt("temp_max"));
                    int min = (temperaturas.getInt("temp_min"));
                    lblTempoAtual.setText(temp + "º");
                    lblTempMaxMin.setText(max + "º / " + min + "º");

                    if (!icone.equals("01d")) {
                        imgTempo.setImageBitmap(mIcon11);

                    /*runOnUiThread(new Runnable() {

                        @Override
                        public void run() {



                        }
                    });*/

                    } else {
                        imgTempo.setBackground(context.getResources().getDrawable(R.drawable.sun));

                    }

                    if (temp < 10) {
                        lblFrase_atual.setText("Meio frio para beber");
                    } else {
                        if (temp < 15) {
                            lblFrase_atual.setText("Ta esquentando, coloca pra gelar!!!");
                        } else {
                            if (temp < 25) {
                                lblFrase_atual.setText("Pode iniciar os trabalhos!!!!!");
                            } else {
                                if (temp > 25) {
                                    lblFrase_atual.setText("Clima agradável para tomar, um ou vinte fardos!");
                                }
                            }
                        }

                    }


                /*if(tempo.equals("algumas nuvens") ){

                    imgTempo.setBackground(context.getResources().getDrawable(R.drawable.cloudy));

                else if(tempo.equals("céu limpo"))

                    imgTempo.setBackground(context.getResources().getDrawable(R.drawable.sun));

                }else if(tempo.equals("rain")){

                    imgTempo.setBackground(context.getResources().getDrawable(R.drawable.rain));
                }*/
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }


        }

    }
}

