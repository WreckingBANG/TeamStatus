package com.wreckingbang.teamstate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> Workers;
    ArrayAdapter<String> arrayAdapter;
    GridView gridView;
    EditText input;
    EditText editText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, Workers);
        gridView = findViewById(R.id.gridview);

        gridView.setAdapter(arrayAdapter);


        //Opens the Pop-Up for deleting the element
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int which_item = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Sind sie sich sicher?")
                        .setMessage("Möchtest du diesen Arbeiter löschen?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Workers.remove(which_item);
                                arrayAdapter.notifyDataSetChanged();
                                saveData();
                            }
                        })
                        .setNegativeButton("no", null)
                        .show();
                return true;
            }
        });


        //Opens the Pop-Up for choosing the place
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int which_item = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setTitle("Ort auswählen")
                        .setMessage("Wo befindet sich dieser Arbeiter?")
                        .setPositiveButton("Werkstatt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = Workers.get(which_item);
                                String[] substring = text.split("-");
                                String text2 = Arrays.toString(substring);

                                String textfinal = substring[0] + "- Werkstatt";
                                Workers.set(which_item, textfinal);
                                arrayAdapter.notifyDataSetChanged();
                                saveData();
                            }
                        })
                        .setNegativeButton("nicht im Betrieb", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = Workers.get(which_item);
                                String[] substring = text.split("-");
                                String text2 = Arrays.toString(substring);

                                String textfinal = substring[0] + "- nicht im Betrieb";
                                Workers.set(which_item, textfinal);
                                arrayAdapter.notifyDataSetChanged();
                                saveData();
                            }
                        })
                        .setNeutralButton("Unterwegs", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = Workers.get(which_item);
                                String[] substring = text.split("-");
                                String text2 = Arrays.toString(substring);

                                String textfinal = substring[0] + "- Unterwegs";
                                Workers.set(which_item, textfinal);
                                arrayAdapter.notifyDataSetChanged();
                                saveData();
                            }
                        })
                        .show();
                return;
            }
        });
    }

    //Saving the Data
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Workers);
        editor.putString("task list", json);
        editor.apply();
    }


    //Loading the Data
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Workers = gson.fromJson(json, type);

        if (Workers == null) {
            Workers = new ArrayList<String>(); {
            }
        }
    }

    //Pop-Up for adding the Worker
    public void addWorkertoList(View view){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(MainActivity.this);
        mydialog.setTitle("Bitte Namen eingeben");

        final EditText nameinput = new EditText(MainActivity.this);
        nameinput.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        mydialog.setView(nameinput);
        mydialog.setIcon(android.R.drawable.ic_menu_add);
        mydialog.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = nameinput.getText().toString();
                text = text.trim();
                if(text.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Name ist nicht korrekt!", Toast.LENGTH_SHORT).show();
                } else {
                    Workers.add(text + " - nicht im Betrieb");
                    arrayAdapter.notifyDataSetChanged();
                    saveData();
                }
            }
        });
        mydialog.setNegativeButton("Cancel", null);
        mydialog.show();

    }

}
