package ufpi.douglas.carteiravirtualru.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ufpi.douglas.carteiravirtualru.R;

public class AlunoLoginActivity extends AppCompatActivity {

    private TextView botaoNovoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_aluno_login);

        botaoNovoAluno = findViewById(R.id.botaoNovoCadastro);

        botaoNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlunoLoginActivity.this, AlunoCadastrarActivity.class);
                startActivity(intent);
            }
        });
    }
}
