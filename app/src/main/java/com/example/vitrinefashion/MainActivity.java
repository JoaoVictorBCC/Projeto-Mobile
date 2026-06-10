package com.example.vitrinefashion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vitrinefashion.util.LocalUserPrefs;

/**
 * MainActivity — Ponto de entrada (launcher) do aplicativo Vitrine Fashion.
 *
 * Responsabilidade única: verificar o estado local do usuário via
 * {@link LocalUserPrefs} (SharedPreferences) e redirecionar para a
 * tela correta, sem nenhuma chamada de rede (RN01 — 100 % offline).
 *
 * Fluxo de navegação:
 *
 *   App abre
 *       │
 *       ▼
 *   MainActivity ──► hasRegisteredUser()? ──► SIM ──► CatalogActivity
 *       │                                     NÃO
 *       └──────────────────────────────────────────► SplashActivity
 *
 * Não há tela de login (RN03 / Exclusões do Escopo).
 * Após a navegação, finish() é chamado para que o botão Voltar
 * não retorne a esta tela de splash.
 */
public class MainActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    /** Tempo de exibição da tela de splash em milissegundos. */
    private static final long SPLASH_DELAY_MS = 900L;

    // -------------------------------------------------------------------------
    // Views (sem ViewBinding — usando findViewById)
    // -------------------------------------------------------------------------

    private ImageView   imgSplashLogo;
    private TextView    tvSplashBrand;
    private TextView    tvSplashTagline;
    private ProgressBar progressMain;

    // -------------------------------------------------------------------------
    // Ciclo de vida
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Oculta a ActionBar padrão nesta tela de splash
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Liga as views ao layout via findViewById
        imgSplashLogo   = findViewById(R.id.imgSplashLogo);
        tvSplashBrand   = findViewById(R.id.tvSplashBrand);
        tvSplashTagline = findViewById(R.id.tvSplashTagline);
        progressMain    = findViewById(R.id.progressMain);

        // Executa a animação de entrada do logo e dos textos
        playEntranceAnimation();

        // Após o delay, verifica o estado local e navega para o destino correto
        new Handler(Looper.getMainLooper()).postDelayed(
                this::routeToDestination,
                SPLASH_DELAY_MS
        );
    }

    // -------------------------------------------------------------------------
    // Navegação
    // -------------------------------------------------------------------------

    /**
     * Lê {@link LocalUserPrefs} (sem rede, 100 % local — RN01) e decide:
     *   - Cadastro encontrado → CatalogActivity
     *   - Sem cadastro        → SplashActivity
     *
     * Em ambos os casos chama finish() para remover esta Activity da back stack.
     */
    private void routeToDestination() {
        // Esconde a barra de progresso antes de sair
        progressMain.setVisibility(View.GONE);

        LocalUserPrefs prefs = new LocalUserPrefs(this);

        Class<?> destination;

        if (prefs.hasRegisteredUser()) {
            // Usuário já criou cadastro local → vai direto ao catálogo
            destination = CatalogActivity.class;
        } else {
            // Primeira abertura ou sem cadastro → tela de boas-vindas
            destination = SplashActivity.class;
        }

        Intent intent = new Intent(this, destination);
        startActivity(intent);

        // Transição suave entre as telas
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Remove esta Activity da pilha — botão Voltar não deve retornar aqui
        finish();
    }

    // -------------------------------------------------------------------------
    // Animação de entrada
    // -------------------------------------------------------------------------

    /**
     * Aplica fade-in + leve scale-up escalonado no logo, título e tagline.
     * Usa apenas animações nativas da SDK Android (sem libs externas — RN01).
     */
    private void playEntranceAnimation() {
        View[] targets = {
                imgSplashLogo,
                tvSplashBrand,
                tvSplashTagline
        };

        for (int i = 0; i < targets.length; i++) {
            long delay = i * 120L; // 0 ms, 120 ms, 240 ms

            // Fade: transparente → opaco
            AlphaAnimation fade = new AlphaAnimation(0f, 1f);
            fade.setDuration(500);
            fade.setStartOffset(delay);
            fade.setFillAfter(true);

            // Scale: 85% → 100% a partir do centro
            ScaleAnimation scale = new ScaleAnimation(
                    0.85f, 1f,
                    0.85f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            );
            scale.setDuration(500);
            scale.setStartOffset(delay);
            scale.setFillAfter(true);

            AnimationSet set = new AnimationSet(true);
            set.addAnimation(fade);
            set.addAnimation(scale);

            targets[i].setVisibility(View.VISIBLE);
            targets[i].startAnimation(set);
        }
    }
}