package ufpi.douglas.carteiravirtualru.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ufpi.douglas.carteiravirtualru.R;

public class SeguracaActivity extends AppCompatActivity {

    private EditText senha;
    private Button botaoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguraca);

        senha = findViewById(R.id.senhaSeguranca);
        botaoSenha = findViewById(R.id.botaoSeguranca);

        botaoSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(senha.getText().toString().equals("admin123")){
                    Intent intent = new Intent(SeguracaActivity.this, FuncLoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SeguracaActivity.this, "Senha incorreta, em caso de dúvidas procure o administrador", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }
}
