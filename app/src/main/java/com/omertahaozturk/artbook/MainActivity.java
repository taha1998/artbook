package com.omertahaozturk.artbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Bitmap> artimages;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == (R.id.adder)){
            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            intent.putExtra("info", "new");
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> artname = new ArrayList<String>();
        artimages = new ArrayList<Bitmap>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artname);
        listView.setAdapter(arrayAdapter);
        artname.add("taha");
        try {

            Main2Activity.database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, image BLOB)");

            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM arts",null);

            int nameIx = cursor.getColumnIndex("name");
            int imageIx =  cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null){
            artname.add(cursor.getString(nameIx));

            byte[] byteArray = cursor.getBlob(imageIx);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            artimages.add(bitmap);

            cursor.moveToNext();
            arrayAdapter.notifyDataSetChanged();

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("info","old");
                intent.putExtra("name",artname.get(i));
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
    }
}
