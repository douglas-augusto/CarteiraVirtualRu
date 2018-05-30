package ufpi.douglas.carteiravirtualru.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import ufpi.douglas.carteiravirtualru.R;
import ufpi.douglas.carteiravirtualru.activity.config.ConfiguracaoFirebase;

public class WelcomeActivity extends AppCompatActivity {

    private Button botaoAluno;
    private Button botaoFuncionario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        verificarUsuarioLogado();

        botaoAluno = findViewById(R.id.botaoSouAluno);
        botaoFuncionario = findViewById(R.id.botaoSouFuncionario);

        botaoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, AlunoLoginActivity.class);
                startActivity(intent);
            }
        });

        botaoFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SeguracaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(WelcomeActivity.this, MainAlunoActivity.class);
        startActivity( intent );
        finish();
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }

    }
}
