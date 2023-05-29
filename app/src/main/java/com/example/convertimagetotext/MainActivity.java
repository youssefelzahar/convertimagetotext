package com.example.convertimagetotext;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView clear,copy,camera;
    EditText recotexts;
    Uri imageuri;
    TextRecognizer textRecognizer;
   // private com.google.mlkit.vision.text.TextRecognizerOptionsInterface TextRecognizerOptionsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clear=findViewById(R.id.imageView2);
        copy=findViewById(R.id.imageView4);
        camera=findViewById(R.id.imageView);
        recotexts=findViewById(R.id.editTextText);
        openccamera();
        //textRecognizer = TextRecognition.getClient(TextRecognizerOptionsInterface);
    }

    private void openccamera() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.github.dhaval2404.imagepicker.ImagePicker.with(MainActivity.this)  .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Activity.RESULT_OK){
            if(data!=null){
                imageuri=data.getData();
                Toast.makeText(this,"image is selected",Toast.LENGTH_LONG);
                recotext();

            }

        }
        else {
            Toast.makeText(this,"image not select",Toast.LENGTH_LONG);
        }
    }

    private void recotext() {
        if(imageuri!=null){
            try {
                InputImage inputImage=InputImage.fromFilePath(MainActivity.this,imageuri);
                Task<Text>result=textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String recognizer=text.getText();
                                recotexts.setText(recognizer);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                            }
                        });
            }
            catch (IOException e){
                e.printStackTrace();
            }


        }

    }
}