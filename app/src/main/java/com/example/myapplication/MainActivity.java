package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button btnAdd;
    EditText eItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        eItem = findViewById(R.id.eItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        // Creating method for interface used in adapter
        ItemsAdapter.OnLongClickListener onLongClickListener =  new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter which position was deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems(); // saving to text file
            }
        };

        // initializing values for adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter); // sets adapter for recycle view
        rvItems.setLayoutManager( new LinearLayoutManager(this)); // creates simple vertical layout

        // Listener for adding item
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Get string value of text field input
                String todoItem  = eItem.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Notify adapter that item is added
                itemsAdapter.notifyItemInserted(items.size() -1 );
                // Clear text input
                eItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems(); // saving to text file
            }
        });
    }


    // The following methods use the Apache commons io library

    // Gets the data file
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // Read lines of data file and loads these into the array list
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items  = new ArrayList<>();
        }
    }

    // Saves item by writing into the file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MakeActivity" , "Error writing item", e);
        }
    }
}