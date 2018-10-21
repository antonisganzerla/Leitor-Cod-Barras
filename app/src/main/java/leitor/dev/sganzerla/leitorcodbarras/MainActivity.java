package leitor.dev.sganzerla.leitorcodbarras;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
                Intent intent = new Intent(MainActivity.this,SimpleScannerActivity.class);
                startActivityForResult(intent,COD);
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClipboardManager Copiar = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
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
                }else{
                    showError();
                }
            }else{
                showError();
            }
        }
    }

    private void showError(){
        Toast.makeText(MainActivity.this,"Não foi possível ler o código.", Toast.LENGTH_SHORT).show();
        editTextBarCode.getText().clear();
    }

}
