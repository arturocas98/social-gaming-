package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.social_gaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import Models.Post;
import Providers.AuthProvider;
import Providers.PostProvider;
import Providers.imageProvider;
import Utils.FileUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class post extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button btn_post;
    imageProvider imageProvider;

    TextInputEditText txt_title;
    TextInputEditText txt_description;
    ImageView image_view_pc;
    ImageView image_view_ps4;
    ImageView image_view_xbox;
    ImageView image_view_nintendo;
    TextView txt_category;
    String category="";
    String title = "";
    String description = "";
    PostProvider postProvider;
    AuthProvider auth;
    AlertDialog mDialog;
    CircleImageView mCircleImageBack;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];

    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;

    // FOTO 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    // FOTO 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        txt_title = findViewById(R.id.txt_name_game);
        txt_description = findViewById(R.id.txt_description_game);

        //foto 1
        mImageViewPost1 = findViewById(R.id.image_view_post_1);
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);
            }
        });
        //foto 2
        mImageViewPost2 = findViewById(R.id.image_view_post_2);
        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
            }
        });

        btn_post = findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveImage();
                clickPost();
            }
        });

        imageProvider = new imageProvider();

        txt_description = findViewById(R.id.txt_description_game);
        image_view_pc = findViewById(R.id.image_view_pc);
        image_view_ps4 = findViewById(R.id.image_view_ps4);
        image_view_xbox = findViewById(R.id.image_view_xbox);
        image_view_nintendo = findViewById(R.id.image_view_nintendo);
        txt_category = findViewById(R.id.txt_categoria);

        image_view_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "PC";
                txt_category.setText(category);
            }
        });

        image_view_ps4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "PS4";
                txt_category.setText(category);
            }
        });
        image_view_xbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "XBOX";
                txt_category.setText(category);
            }
        });
        image_view_nintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "NINTENDO";
                txt_category.setText(category);
            }
        });

        postProvider = new PostProvider();
        auth = new AuthProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mCircleImageBack = findViewById(R.id.circleImageBack);
        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};


    }

    private void clickPost() {
         title = txt_title.getText().toString();
         description = txt_description.getText().toString();
        if (!title.isEmpty() && !description.isEmpty() && !category.isEmpty()){
            //SELECCIONO AMBAS IMAGENES DE LA GALERIA
            if (mImageFile != null && mImageFile2 !=null){
                saveImage(mImageFile,mImageFile2);
            }
            // TOMO LAS DOS FOTOS DE LA CAMARA
            else if (mPhotoFile != null && mPhotoFile2 != null) {
                saveImage(mPhotoFile, mPhotoFile2);
            }
            else if (mImageFile != null && mPhotoFile2 != null) {
                saveImage(mImageFile, mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null) {
                saveImage(mPhotoFile, mImageFile2);
            }
            else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(post.this,"Complete los campos para publicar",Toast.LENGTH_SHORT).show();
        }


    }

    private void saveImage(File imageFile1, final File imageFile2) {
        mDialog.show();
        imageProvider.save(post.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            imageProvider.save(post.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()) {
                                        imageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(title.toLowerCase());
                                                post.setDescription(description);
                                                post.setCategory(category);
                                                post.setUser_id(auth.getUid());
                                                post.setTimestamp(new Date().getTime());
                                                postProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if (taskSave.isSuccessful()) {
                                                            clearForm();
                                                            Toast.makeText(post.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            Toast.makeText(post.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText(post.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(post.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //metodo para abrir la galeria del telefono
    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }
    //Cuando se usa startActivityForResult se sobre escribe el metodo onActivityResult
    //METODO CUANDO EL USUARIO YA SELECCIONO LA FOTO DE LA GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Si el request al seleccionar una imagen de la galeria es igual al resultado de seleccionarlo correctamente y cargarlo en la app
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            try {
                mPhotoFile2 = null;
                mImageFile2 = FileUtil.from(this, data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        /**
         * SELECCION DE FOTOGRAFIA
         * */

        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(post.this).load(mPhotoPath).into(mImageViewPost1);
        }

        /**
         * SELECCION DE FOTOGRAFIA 2
         */
        if (requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(post.this).load(mPhotoPath2).into(mImageViewPost2);
        }




    }

    private void clearForm() {
        txt_title.setText("");
        txt_description.setText("");
        txt_category.setText("CATEGORIAS");
        mImageViewPost1.setImageResource(R.drawable.ic_upload_image);
        mImageViewPost2.setImageResource(R.drawable.ic_upload_image);
        title = "";
        description = "";
        category = "";
        mImageFile = null;
        mImageFile2 = null;
    }

    //metodo para abrir dialog y mostrar opciob de abrir galeria o tomar foto
    private void selectOptionImage(final int numberImage) {
        //el dialog interface nos va a abrir un modal
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (numberImage == 1) {
                        openGallery(GALLERY_REQUEST_CODE);
                    }
                    else if (numberImage == 2) {
                        openGallery(GALLERY_REQUEST_CODE_2);
                    }
                }
                else if (i == 1){
                    if (numberImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE);
                    }
                    else if (numberImage == 2) {
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });

        mBuilderSelector.show();

    }

    private void takePhoto(int requestCode) {
        //mediastore es para abrir la camara
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch(Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(post.this, "com.example.social_gaming", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        //getExternalFilesDir devuelve la ruta del direcytorio de imagenes
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE) {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_2) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }
}
