package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;

import Models.Post;
import Providers.AuthProvider;
import Providers.PostProvider;
import Providers.imageProvider;
import Utils.FileUtil;

public class post extends AppCompatActivity {

    ImageView mImageViewPost1;
    File mImageFile;
    Button btn_post;
    imageProvider imageProvider;
    private final int GALLERY_REQUEST_CODE = 1;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        txt_title = findViewById(R.id.txt_name_game);
        txt_description = findViewById(R.id.txt_description_game);
        mImageViewPost1 = findViewById(R.id.image_view_post_1);
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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

    }

    private void clickPost() {
         title = txt_title.getText().toString();
         description = txt_description.getText().toString();
        if (!title.isEmpty() && !description.isEmpty() && !category.isEmpty()){
            if (mImageFile != null){
                saveImage();
            }
        }else{
            Toast.makeText(post.this,"Complete los campos para publicar",Toast.LENGTH_SHORT).show();
        }


    }

    private void saveImage() {
        imageProvider.save(post.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    imageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage1(url);
                            post.setDescription(description);
                            post.setTitle(title);
                            post.setCategory(category);
                            post.setUser_id(auth.getUid());
                            postProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    if (taskSave.isSuccessful()){
                                        Toast.makeText(post.this,"La información se almaceno correctamente",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(post.this,"No se pudo almacenar la información",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });

                }else{
                    Toast.makeText(post.this,"Hubo un error al almacenar la imagen",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //metodo para abrir la galeria del telefono
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    //Cuando se usa startActivityForResult se sobre escribe el metodo onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Si el request al seleccionar una imagen de la galeria es igual al resultado de seleccionarlo correctamente y cargarlo en la app
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}