package ufpi.douglas.carteiravirtualru.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ufpi.douglas.carteiravirtualru.R;
import ufpi.douglas.carteiravirtualru.activity.config.ConfiguracaoFirebase;
import ufpi.douglas.carteiravirtualru.activity.helper.Base64Custom;
import ufpi.douglas.carteiravirtualru.activity.helper.Preferencias;
import ufpi.douglas.carteiravirtualru.activity.model.Aluno;

public class AlunoCadastrarActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText matricula;
    private EditText curso;
    private EditText repetirSenha;
    private Button botaoCadastrar;
    private Aluno aluno;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_cadastrar);

        nome = findViewById(R.id.campoNomeAluno);
        email = findViewById(R.id.campoEmailCadastro);
        senha = findViewById(R.id.campoSenhaCadastro);
        matricula = findViewById(R.id.campoMatricula);
        curso = findViewById(R.id.campoCurso);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        repetirSenha = findViewById(R.id.campoRepetirSenha);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nome.getText().toString().isEmpty() || email.getText().toString().isEmpty() || senha.getText().toString().isEmpty() || matricula.getText().toString().isEmpty() || curso.getText().toString().isEmpty() || repetirSenha.getText().toString().isEmpty()){
                    Toast.makeText(AlunoCadastrarActivity.this, "Todos os campos sao obrigatorios", Toast.LENGTH_LONG ).show();
                }else if(senha.getText().toString().equals(repetirSenha.getText().toString())){
                    aluno = new Aluno();
                    aluno.setNome(nome.getText().toString());
                    aluno.setEmail(email.getText().toString());
                    aluno.setSenha(senha.getText().toString());
                    aluno.setCurso(curso.getText().toString());
                    aluno.setMatricula(matricula.getText().toString());
                    final ProgressDialog dialog =
                            new ProgressDialog(AlunoCadastrarActivity.this);
                    dialog.setMessage("Cadastrando... aguarde");
                    dialog.setIndeterminate(false);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();
                    cadastrarAluno();
                }else{
                    Toast.makeText(AlunoCadastrarActivity.this, "As senhas digitadas não são iguais", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }

    private void cadastrarAluno(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                aluno.getEmail(),
                aluno.getSenha()
        ).addOnCompleteListener(AlunoCadastrarActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    Toast.makeText(AlunoCadastrarActivity.this, "Sucesso ao cadastrar aluno!", Toast.LENGTH_LONG ).show();

                    String identificadorUsuario = Base64Custom.codificarBase64( aluno.getEmail() );
                    aluno.setId( identificadorUsuario );
                    aluno.salvar();

                    Preferencias preferencias = new Preferencias(AlunoCadastrarActivity.this);
                    preferencias.salvarDados( identificadorUsuario, aluno.getNome() );

                    abrirLoginUsuario();

                }else{

                    String erro = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Escolha uma senha que contenha, letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email indicado não é válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Já existe uma conta com esse e-mail.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(AlunoCadastrarActivity.this, "Erro ao cadastrar usuário " + erro, Toast.LENGTH_LONG ).show();
                }

            }
        });

    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(AlunoCadastrarActivity.this, AlunoLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
