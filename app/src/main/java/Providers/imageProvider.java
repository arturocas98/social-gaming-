package Providers;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

import Utils.CompressorBitmapImage;

public class imageProvider {
    StorageReference storage;
    public   imageProvider(){
        storage = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),500,500);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(new Date()+".jpg");
        storage = storageReference;
        UploadTask task = storageReference.putBytes(imageByte);
        return task;
    }

    public  StorageReference getStorage(){
        return storage;
    }
}
