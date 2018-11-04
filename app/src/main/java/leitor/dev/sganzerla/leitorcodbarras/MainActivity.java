package leitor.dev.sganzerla.leitorcodbarras;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    private Button buttonRead;
    private Button buttonCopy;
    private EditText editTextBarCode;
    public static final int COD = 2;
    private static final int ZBAR_CAMERA_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRead = findViewById(R.id.button_ler);
        buttonCopy = findViewById(R.id.button_copiar);
        editTextBarCode = findViewById(R.id.edittext_codigo_barras);

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
                } else {
                    buttonCopy.setEnabled(false);
                    Intent intent = new Intent(MainActivity.this, SimpleScannerActivity.class);
                    startActivityForResult(intent,COD);
                }
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("bar_code", editTextBarCode.getText().toString().trim());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this,"Copiado para Área de Transferência", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == COD){
            if(data != null){
                Bundle extras = data.getExtras();
                if(extras != null){
                    String codigo = extras.getString("codigo");
                    editTextBarCode.setText(codigo);
                    buttonCopy.setEnabled(true);
                }else{
                    showError();
                }
            }else{
                showError();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, SimpleScannerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Por favor, permita acesso a câmera para usar o scanner.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void showError(){
        Toast.makeText(MainActivity.this,"Não foi possível ler o código.", Toast.LENGTH_SHORT).show();
        editTextBarCode.getText().clear();
    }

}
