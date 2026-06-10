package com.example.vitrinefashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity — Tela de boas-vindas (activity_splash.xml).
 *
 * Destino da MainActivity quando o usuário ainda não possui cadastro local.
 * Apresenta os botões "Explorar Coleção" e "Criar Meu Cadastro" (sem login — RN03).
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnEnter    = findViewById(R.id.btnEnter);
        Button btnRegister = findViewById(R.id.btnRegister);

        // "Explorar Coleção" — entra no catálogo sem cadastro
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, CatalogActivity.class);
                startActivity(intent);
            }
        });

        // "Criar Meu Cadastro" — vai para o formulário de cadastro local (RF03)
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
