package br.ufrn.eaj.tads.savefromcamera;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b;

    // Storage Permissions
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.btnExterno);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.validate(MainActivity.this,0, PERMISSIONS_STORAGE);
    }

    public void goToExternalStorage(View v){
        startActivity(new Intent(MainActivity.this, ExternalStorageActivity.class));
    }

    public void goToInternalStorage(View v){
        startActivity(new Intent(MainActivity.this, InternalStorageActivity.class ));
    }

    public void goToDataBase(View v){
        startActivity(new Intent(MainActivity.this, DataBaseActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                b.setEnabled(false);
                alertAndFinish();
                return;
            }
        }
        b.setEnabled(true);
        // Se chegou aqui está OK :-)
    }

    private void alertAndFinish() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name).setMessage("Para utilizar todas as funções desse aplicativo, você precisa aceitar o acesso ao armanamento externo.");
            // Add the buttons
            builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //faz nada...
                }
            });
            builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
    }
}
