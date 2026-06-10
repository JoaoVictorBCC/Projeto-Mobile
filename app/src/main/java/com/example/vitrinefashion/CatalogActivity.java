package com.example.vitrinefashion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CatalogActivity — Tela principal do catálogo de produtos (activity_catalog.xml).
 *
 * Destino da MainActivity quando o usuário já possui cadastro local.
 * Exibe o grid de produtos com filtros por categoria, tamanho e cor (RF01).
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // TODO: inicializar RecyclerView, ChipGroup de filtros e SearchBar
        //       conforme activity_catalog.xml e RF01.
    }
}
