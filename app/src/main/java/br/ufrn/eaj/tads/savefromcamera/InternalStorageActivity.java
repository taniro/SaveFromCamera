package br.ufrn.eaj.tads.savefromcamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorageActivity extends AppCompatActivity {

    String FILENAME = "photo_internal.jpg";
    ImageView imagemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        imagemView = (ImageView) findViewById(R.id.imageViewInterno);
    }

    public void takePhotoAndSaveInternally(View v){

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,1);
    }

    public void readPhotoInternally(View v){

        FileInputStream fin ;
        try {
            //abre o arquivo chamado FILENAME para LEITURA
            fin = openFileInput(FILENAME);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap imagem = BitmapFactory.decodeStream(fin,null, options);

            imagemView.setImageBitmap(imagem);

            fin.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                if(data!=null){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput(FILENAME, Context.MODE_PRIVATE);;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
