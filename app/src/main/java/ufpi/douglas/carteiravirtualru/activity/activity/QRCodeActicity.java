package ufpi.douglas.carteiravirtualru.activity.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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

public class QRCodeActicity extends AppCompatActivity {

    private ImageView qrcode;
    private DatabaseReference reference;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_acticity);

        qrcode = findViewById(R.id.iv_QRCode);

        exibirQrCodeAluno();
    }

    public void exibirQrCodeAluno() {

        Preferencias preferencias = new Preferencias(QRCodeActicity.this);
        String usuarioLogado = preferencias.getIdentificador();
        reference = ConfiguracaoFirebase.getFirebase().child("alunos").child(usuarioLogado);

        aluno = new Aluno();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    aluno = dataSnapshot.getValue(Aluno.class);

                    String textoQRCode = Base64Custom.codificarBase64(aluno.getEmail().toString());
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(textoQRCode, BarcodeFormat.QR_CODE,2000,2000);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrcode.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
