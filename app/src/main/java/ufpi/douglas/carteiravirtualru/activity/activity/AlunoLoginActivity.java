package ufpi.douglas.carteiravirtualru.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ufpi.douglas.carteiravirtualru.R;
import ufpi.douglas.carteiravirtualru.activity.config.ConfiguracaoFirebase;
import ufpi.douglas.carteiravirtualru.activity.helper.Base64Custom;
import ufpi.douglas.carteiravirtualru.activity.helper.Preferencias;
import ufpi.douglas.carteiravirtualru.activity.model.Aluno;

public class AlunoLoginActivity extends AppCompatActivity {

    private TextView botaoNovoAluno;
    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Aluno aluno;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_aluno_login);

        botaoNovoAluno = findViewById(R.id.botaoNovoCadastro);
        email = findViewById(R.id.campoEmailAlunoLogin);
        senha = findViewById(R.id.campoSenhaLoginAluno);
        botaoLogar = findViewById(R.id.botaoLogarAluno);

        botaoNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlunoLoginActivity.this, AlunoCadastrarActivity.class);
                startActivity(intent);
            }
        });

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aluno = new Aluno();
                aluno.setEmail(email.getText().toString());
                aluno.setSenha(senha.getText().toString());
                validarLogin();
            }
        });
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                aluno.getEmail(),
                aluno.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){


                    identificadorUsuarioLogado = Base64Custom.codificarBase64(aluno.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("alunos")
                            .child( identificadorUsuarioLogado );

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Aluno alunoRecuperado = dataSnapshot.getValue( Aluno.class );

                            Preferencias preferencias = new Preferencias(AlunoLoginActivity.this);
                            preferencias.salvarDados( identificadorUsuarioLogado, alunoRecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent( valueEventListenerUsuario );

                    abrirTelaPrincipal();

                    Toast.makeText(AlunoLoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText(AlunoLoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(AlunoLoginActivity.this, MainAlunoActivity.class);
        startActivity( intent );
        finish();
    }

}
