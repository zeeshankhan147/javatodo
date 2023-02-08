package com.example.javatodo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.javatodo.data.DatabaseHandler;
import com.example.javatodo.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ImageButton addBtn , resetBtn;
    RecyclerView recyclerView;
    List<TodoModel> datalist = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    DataAdapter dataAdapter;
    DatabaseHandler mainBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.input);
        addBtn = findViewById(R.id.addTask);
        resetBtn = findViewById(R.id.resetAll);
        recyclerView = findViewById(R.id.recyclerView);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // init database
        mainBD = new DatabaseHandler(this);

        //store db value in datalist
        datalist = mainBD.getAllData();

        //init linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        //set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);

        //init adapter
        dataAdapter = new DataAdapter(datalist,MainActivity.this);

        //set adapter
        recyclerView.setAdapter(dataAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() { // :: THIS BUTTON FOR ADD TASK ::
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString();
                String flag = "000";
                if (!inputText.equals("")){
                    TodoModel todoModel = new TodoModel();
                    todoModel.setId((int) Math.round(Math.random() * 100));
                    todoModel.setText(inputText);
                    todoModel.setIsCheck(flag);
                    mainBD.addTodo(todoModel);
                    editText.setText("");
                    editText.clearFocus();
                    datalist.clear();
                    datalist.addAll(mainBD.getAllData());
                    dataAdapter.notifyDataSetChanged();

                }
                else {
                    builder.setTitle("Warning!");
                    builder.setMessage("please fill the field");
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() { // :: THIS BUTTON FOR RESET ALL TASK ::
            @Override
            public void onClick(View view) {
                if ((mainBD.getAllData().size()) > 0 ){
                    builder.setMessage("Are you sure delete all record from database!")
                            .setTitle("Warning!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editText.setText("");
                                    mainBD.resetDatabase();
                                    datalist.clear();
                                    datalist.addAll(mainBD.getAllData());
                                    dataAdapter.notifyDataSetChanged();
                                    editText.clearFocus();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    builder.setTitle("something went wrong!");
                    builder.setMessage("no records found");
                    builder.setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

    }
}