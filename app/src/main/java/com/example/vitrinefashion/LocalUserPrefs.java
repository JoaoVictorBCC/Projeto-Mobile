package com.example.vitrinefashion.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * LocalUserPrefs — Wrapper de SharedPreferences para o estado local do usuário.
 *
 * Toda leitura e escrita é estritamente local, sem acesso à rede (RN01).
 * Utilizado pela {@link com.example.vitrinefashion.MainActivity} para decidir
 * o destino de navegação ao abrir o app, e pela RegisterActivity para
 * persistir o cadastro após o preenchimento do formulário (RF03).
 *
 * Arquivo de preferências: "vf_user_prefs" (MODE_PRIVATE).
 */
public class LocalUserPrefs {

    // -------------------------------------------------------------------------
    // Constantes — chaves do arquivo de preferências
    // -------------------------------------------------------------------------

    private static final String PREFS_FILE     = "vf_user_prefs";
    private static final String KEY_HAS_USER   = "has_registered_user";
    private static final String KEY_USER_NAME  = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_CPF   = "user_cpf";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_ADDR  = "user_address";

    // -------------------------------------------------------------------------
    // Instância de SharedPreferences
    // -------------------------------------------------------------------------

    private final SharedPreferences prefs;

    /**
     * Construtor — obtém (ou cria) o arquivo de preferências local.
     *
     * @param context Context da Activity ou Application.
     */
    public LocalUserPrefs(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    // -------------------------------------------------------------------------
    // Verificação de cadastro  (lida pela MainActivity)
    // -------------------------------------------------------------------------

    /**
     * Verifica se o usuário já possui um cadastro salvo localmente.
     *
     * @return {@code true} se houver cadastro; {@code false} caso contrário.
     */
    public boolean hasRegisteredUser() {
        return prefs.getBoolean(KEY_HAS_USER, false);
    }

    // -------------------------------------------------------------------------
    // Salvar cadastro  (chamado pela RegisterActivity — RF03)
    // -------------------------------------------------------------------------

    /**
     * Persiste os dados do formulário de cadastro no armazenamento local.
     * Nenhum dado é enviado a servidores externos (RN01, RN03).
     *
     * @param name    Nome completo do usuário.
     * @param email   Endereço de e-mail.
     * @param cpf     CPF (apenas dígitos ou formatado).
     * @param phone   Telefone de contato.
     * @param address Endereço completo.
     */
    public void saveUser(String name,
                         String email,
                         String cpf,
                         String phone,
                         String address) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_HAS_USER,   true);
        editor.putString(KEY_USER_NAME,   name);
        editor.putString(KEY_USER_EMAIL,  email);
        editor.putString(KEY_USER_CPF,    cpf);
        editor.putString(KEY_USER_PHONE,  phone);
        editor.putString(KEY_USER_ADDR,   address);
        editor.apply(); // assíncrono — não bloqueia a UI thread
    }

    // -------------------------------------------------------------------------
    // Leitura dos dados  (para exibição de perfil ou pré-preenchimento)
    // -------------------------------------------------------------------------

    /** @return Nome completo salvo, ou string vazia se não existir. */
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    /** @return E-mail salvo, ou string vazia se não existir. */
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    /** @return CPF salvo, ou string vazia se não existir. */
    public String getUserCpf() {
        return prefs.getString(KEY_USER_CPF, "");
    }

    /** @return Telefone salvo, ou string vazia se não existir. */
    public String getUserPhone() {
        return prefs.getString(KEY_USER_PHONE, "");
    }

    /** @return Endereço salvo, ou string vazia se não existir. */
    public String getUserAddress() {
        return prefs.getString(KEY_USER_ADDR, "");
    }

    // -------------------------------------------------------------------------
    // Limpar cadastro  (troca de usuário demonstrativo — RF03)
    // -------------------------------------------------------------------------

    /**
     * Remove todos os dados locais, permitindo cadastrar outro perfil demo (RF03).
     */
    public void clearUser() {
        prefs.edit().clear().apply();
    }
}