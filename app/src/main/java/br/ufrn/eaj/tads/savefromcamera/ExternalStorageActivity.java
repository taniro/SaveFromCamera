package br.ufrn.eaj.tads.savefromcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ExternalStorageActivity extends AppCompatActivity {

    String FILENAME = "photo_external.jpg";
    private String pictureImagePath = "";
    ImageView imageView;
    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        imageView = (ImageView) findViewById(R.id.imageViewInterno);

    }

    private File createImageFile() throws IOException {

        // Create an image file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        pictureImagePath = storageDir.getAbsolutePath()+"/"+FILENAME;
        File image = new File(pictureImagePath);

        /*This was not necessary as i needed a fixed name to the image and t
        he createTempFile method adds a random interger at the end of the file name*/

        //File image  = File.createTempFile(
        //            FILENAME,  /* prefix */
        //            ".jpg",         /* suffix */
        //            storageDir      /* directory */
        //);

        return image;
    }



    public void takePhotoAndSaveExternally(View v) throws IOException {

        if (checkStorage() == false){
            return;
        }else {

            File file = createImageFile();
            Uri outputFileUri = FileProvider.getUriForFile(ExternalStorageActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            Log.i("Salvando", file.getAbsolutePath());
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent,1);

            /*
            * This code do not work for Android Nougart as file uris can not be exposed anymore. Use a file provider for android N.
            * */

            /*
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            pictureImagePath = storageDir.getAbsolutePath() + "/Camera/" + FILENAME;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent,1);
            */
        }
    }

    public void readPhotoExternally(View v) throws IOException {
        
        if (checkStorage() == false){
            Toast.makeText(this, "Storage is busy", Toast.LENGTH_SHORT).show();
            return;
        }else {

            File imgFile = createImageFile();

            Log.i("Imagem", imgFile.getAbsolutePath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.imageViewExterno);
                Log.i("Testes","Arquivo:"+ imgFile);
                myImage.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean checkStorage(){
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }
        return true;
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
