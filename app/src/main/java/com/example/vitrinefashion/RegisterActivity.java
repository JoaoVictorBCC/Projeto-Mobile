package com.example.vitrinefashion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vitrinefashion.util.LocalUserPrefs;
import com.google.android.material.textfield.TextInputEditText;

/**
 * RegisterActivity — Tela de cadastro local (activity_register.xml).
 *
 * Persiste os dados do formulário via {@link LocalUserPrefs} (SharedPreferences),
 * sem envio a servidores externos (RF03, RN01, RN03).
 * Após salvar, redireciona para o CatalogActivity.
 */
public class RegisterActivity extends AppCompatActivity {

    // Views do formulário (mapeiam os IDs de activity_register.xml)
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etCpf;
    private TextInputEditText etPhone;
    private TextInputEditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Liga as views
        etName    = findViewById(R.id.etName);
        etEmail   = findViewById(R.id.etEmail);
        etCpf     = findViewById(R.id.etCpf);
        etPhone   = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndProceed();
            }
        });
    }

    /**
     * Valida os campos e salva o cadastro localmente (RF03).
     * Redireciona para o CatalogActivity após salvar com sucesso.
     */
    private void saveAndProceed() {
        String name    = etName.getText()    != null ? etName.getText().toString().trim()    : "";
        String email   = etEmail.getText()   != null ? etEmail.getText().toString().trim()   : "";
        String cpf     = etCpf.getText()     != null ? etCpf.getText().toString().trim()     : "";
        String phone   = etPhone.getText()   != null ? etPhone.getText().toString().trim()   : "";
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";

        // Validação mínima
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Preencha ao menos nome e e-mail.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Persiste localmente — sem rede (RN01)
        LocalUserPrefs prefs = new LocalUserPrefs(this);
        prefs.saveUser(name, email, cpf, phone, address);

        Toast.makeText(this, "Cadastro salvo localmente!", Toast.LENGTH_SHORT).show();

        // Vai para o catálogo e limpa a back stack de registro/splash
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
