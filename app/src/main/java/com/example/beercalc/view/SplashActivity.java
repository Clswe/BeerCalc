package com.example.beercalc.view;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.example.beercalc.R;

public class SplashActivity {
    public static class SplashDevMedia extends Activity {
        // Timer da splash screen
        MediaPlayer mp = new MediaPlayer();
        private static int SPLASH_TIME_OUT = 3000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {
                /*
                 * Exibindo splash com um timer.
                 */
                @Override
                public void run() {
                    // Esse método será executado sempre que o timer acabar
                    // E inicia a activity principal
                    Intent i = new Intent(SplashDevMedia.this,
                            MenuPrincipalActivity.class);
                    startActivity(i);

                    // Fecha esta activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
