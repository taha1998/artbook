package com.omertahaozturk.artbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    ImageView ımageView;
    EditText editText;
    Button button;
    static SQLiteDatabase database;
    Bitmap selectedimge;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ımageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);


        Intent intent = getIntent();
        String info = intent.getStringExtra("info");



        if (info.equalsIgnoreCase("new")){
            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.background);
            ımageView.setImageBitmap(background);
            button.setVisibility(View.VISIBLE);
            editText.setText("");

        }else {

          String name = intent.getStringExtra("name");
          editText.setText(name);

          int position = intent.getIntExtra("position",0);
          ımageView.setImageBitmap(MainActivity.artimages.get(position));
          button.setVisibility(View.INVISIBLE);
        }

    }

public void save(View view){
    String artname = editText.getText().toString();

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    selectedimge.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
    byte[] byteArray = byteArrayOutputStream.toByteArray();

    try {

        database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, image BLOB)");

        String sqlString = "INSERT INTO arts (name, image) VALUES (?, ?)";
        SQLiteStatement statement = database.compileStatement(sqlString);
        statement.bindString(1,artname);
        statement.bindBlob(2,byteArray);
        statement.execute();

    } catch (Exception e){
        e.printStackTrace();
    }

    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
    startActivity(intent);




}

public void select(View view){
    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
    }
    else {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if (requestCode == 2){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);

        }
    }


    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1 && resultCode == RESULT_OK && data != null){

        Uri images = data.getData();

        try {
            selectedimge = MediaStore.Images.Media.getBitmap(this.getContentResolver(), images);
            ımageView.setImageBitmap(selectedimge);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    super.onActivityResult(requestCode, resultCode, data);
    }
}
