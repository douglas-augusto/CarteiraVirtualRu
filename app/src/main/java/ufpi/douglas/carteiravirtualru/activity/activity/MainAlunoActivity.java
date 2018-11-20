package ufpi.douglas.carteiravirtualru.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ufpi.douglas.carteiravirtualru.R;
import ufpi.douglas.carteiravirtualru.activity.config.ConfiguracaoFirebase;
import ufpi.douglas.carteiravirtualru.activity.helper.Preferencias;
import ufpi.douglas.carteiravirtualru.activity.model.Aluno;

public class MainAlunoActivity extends AppCompatActivity {

    private Aluno aluno;
    private TextView nomeAluno;
    private TextView curso;
    private TextView fichas;
    private TextView matricula;
    private Button botaoDeslogar;
    private Button botaoQRCode;
    private DatabaseReference reference;
    private FirebaseAuth alunoAutenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aluno);

        alunoAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


        recuperarAlunoLogado();


        nomeAluno = findViewById(R.id.textNomeAluno);
        curso = findViewById(R.id.textCursoAluno);
        fichas = findViewById(R.id.textFichas);
        matricula = findViewById(R.id.textMatricula);
        botaoDeslogar = findViewById(R.id.botaoDeslogar);
        botaoQRCode = findViewById(R.id.botaoQRCode);

        botaoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarUsuario();
            }
        });

        botaoQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAlunoActivity.this, QRCodeActicity.class);
                startActivity(intent);
            }
        });

    }

    public void recuperarAlunoLogado(){

        Preferencias preferencias = new Preferencias(MainAlunoActivity.this);
        String usuarioLogado = preferencias.getIdentificador();
        reference = ConfiguracaoFirebase.getFirebase().child("alunos").child(usuarioLogado);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    aluno = new Aluno();
                    aluno = dataSnapshot.getValue(Aluno.class);

                    String nomeAlunoExibir = aluno.getNome().toString();
                    String cursoAluno = aluno.getCurso().toString();
                    String ficha = Integer.toString(aluno.getFicha());
                    String matriculaAluno = aluno.getMatricula();

                    nomeAluno.setText("ALUNO: "+nomeAlunoExibir.toUpperCase());
                    matricula.setText("MATRICULA: "+matriculaAluno.toUpperCase());
                    curso.setText("CURSO: "+cursoAluno.toUpperCase());
                    fichas.setText("FICHAS: "+ficha.toUpperCase());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deslogarUsuario(){

        alunoAutenticacao.signOut();
        Intent intent = new Intent(MainAlunoActivity.this, AlunoLoginActivity.class);
        startActivity(intent);
        finish();

    }

}
