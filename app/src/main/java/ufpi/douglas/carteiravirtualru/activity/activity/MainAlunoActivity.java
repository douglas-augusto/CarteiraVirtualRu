package ufpi.douglas.carteiravirtualru.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ufpi.douglas.carteiravirtualru.R;
import ufpi.douglas.carteiravirtualru.activity.config.ConfiguracaoFirebase;
import ufpi.douglas.carteiravirtualru.activity.model.Aluno;

public class MainAlunoActivity extends AppCompatActivity {

    private Aluno aluno;
    private TextView nomeAluno;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aluno);

        nomeAluno = findViewById(R.id.textNomeAluno);
        recuperarAlunoLogado();
    }

    public void recuperarAlunoLogado(){
        aluno = new Aluno();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    aluno = dataSnapshot.getValue(Aluno.class);
                    String nomeAlunoExibir = aluno.getNome().toString();
                    nomeAluno.setText(nomeAlunoExibir);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
