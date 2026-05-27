package com.vitrinefashion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vitrinefashion.catalog.CatalogActivity;
import com.vitrinefashion.databinding.ActivityMainBinding;
import com.vitrinefashion.splash.SplashActivity;
import com.vitrinefashion.util.LocalUserPrefs;

/**
 * MainActivity — Ponto de entrada (launcher) do aplicativo Vitrine Fashion.
 *
 * Responsabilidade única: verificar o estado local do usuário via
 * {@link LocalUserPrefs} (SharedPreferences) e redirecionar para a
 * tela correta, sem nenhuma chamada de rede (RN01 — 100 % offline).
 *
 * Fluxo de navegação:
 * <pre>
 *   App abre
 *       │
 *       ▼
 *   MainActivity ──► hasRegisteredUser()? ──► SIM ──► CatalogActivity
 *       │                                     NÃO
 *       └──────────────────────────────────────────► SplashActivity
 * </pre>
 *
 * Não há tela de login (RN03 / Exclusões do Escopo):
 * <ul>
 *   <li>Cadastro local encontrado → entra direto no catálogo.</li>
 *   <li>Sem cadastro              → vai para a tela de boas-vindas (Splash),
 *       onde o usuário escolhe "Explorar" ou "Criar Cadastro".</li>
 * </ul>
 *
 * Após a navegação, {@code finish()} é chamado para que o botão Voltar
 * não retorne a esta tela de splash.
 */
public class MainActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    /** Tempo de exibição da tela de splash em milissegundos. */
    private static final long SPLASH_DELAY_MS = 900L;

    // -------------------------------------------------------------------------
    // ViewBinding
    // -------------------------------------------------------------------------

    /** Binding gerado a partir de activity_main.xml. */
    private ActivityMainBinding binding;

    // -------------------------------------------------------------------------
    // Ciclo de vida
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Infla o layout activity_main.xml via ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Oculta a ActionBar padrão nesta tela de splash
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
     * <ul>
     *   <li>Cadastro encontrado → {@link CatalogActivity}</li>
     *   <li>Sem cadastro        → {@link SplashActivity}</li>
     * </ul>
     * Em ambos os casos chama {@code finish()} para remover esta Activity
     * da back stack.
     */
    private void routeToDestination() {
        LocalUserPrefs prefs = new LocalUserPrefs(this);

        Class<?> destination;

        if (prefs.hasRegisteredUser()) {
            // Usuário já criou cadastro local anteriormente → catálogo direto
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
     * Aplica fade-in + leve scale-up escalonado no logo, no título e na tagline,
     * criando uma entrada elegante alinhada à identidade visual da Vitrine Fashion.
     *
     * Utiliza apenas {@link AlphaAnimation} e {@link ScaleAnimation} da SDK Android,
     * sem dependências externas, garantindo funcionamento offline (RN01).
     */
    private void playEntranceAnimation() {
        // Vistas a animar, na ordem em que devem aparecer
        View[] targets = {
                binding.imgSplashLogo,
                binding.tvSplashBrand,
                binding.tvSplashTagline
        };

        for (int i = 0; i < targets.length; i++) {
            long delay = i * 120L; // escalonamento suave: 0 ms, 120 ms, 240 ms

            // Animação de opacidade: 0 → 1
            AlphaAnimation fade = new AlphaAnimation(0f, 1f);
            fade.setDuration(500);
            fade.setStartOffset(delay);
            fade.setFillAfter(true);

            // Animação de escala: 85 % → 100 % (a partir do centro)
            ScaleAnimation scale = new ScaleAnimation(
                    0.85f, 1f,                               // scaleX: início → fim
                    0.85f, 1f,                               // scaleY: início → fim
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,  // pivô X central
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f   // pivô Y central
            );
            scale.setDuration(500);
            scale.setStartOffset(delay);
            scale.setFillAfter(true);

            // Combina as duas animações em um conjunto
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(fade);
            set.addAnimation(scale);

            targets[i].setVisibility(View.VISIBLE);
            targets[i].startAnimation(set);
        }
    }
}