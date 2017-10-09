package br.ufrn.eaj.tads.savefromcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orm.SugarContext;

import java.io.ByteArrayOutputStream;

public class DataBaseActivity extends AppCompatActivity {

    ImageView imagemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        imagemView = (ImageView) findViewById(R.id.imageViewBD);

        SugarContext.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    public void takePhotoAndSaveDatabase(View v){

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,1);
    }

    public void readPhotoDatabase(View v){

        ImageRecord imr = ImageRecord.last(ImageRecord.class);

        if(imr != null){
            Bitmap imagem = BitmapFactory.decodeByteArray(imr.getImage(),0, imr.getImage().length);
            imagemView.setImageBitmap(imagem);
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

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] photo = baos.toByteArray();

                    ImageRecord imageBanco = new ImageRecord(photo);

                    imageBanco.save();

                    Toast.makeText(this, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
