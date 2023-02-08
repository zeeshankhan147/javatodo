package com.example.javatodo.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.javatodo.MainActivity;
import com.example.javatodo.model.TodoModel;
import com.example.javatodo.params.Params;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY," + Params.TODO_TASK + " TEXT, " + Params.IS_CHECK + "TEXT" + ")";
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Params.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addTodo (TodoModel todoList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_ID, todoList.getId());
        values.put(Params.TODO_TASK, todoList.getText());
//        values.put(Params.IS_CHECK, todoList.getIsCheck());
        db.insert(Params.TABLE_NAME,null,values);
        db.close();
    }
    public List<TodoModel> getAllData(){
        List<TodoModel> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);

        if (cursor.moveToFirst()){
            do {
                TodoModel data = new TodoModel();
                data.setId(cursor.getInt(0));
                data.setText(cursor.getString(1));
//                data.setIsCheck((cursor.getString(2)));
                todoList.add(data);
            } while (cursor.moveToNext());
        }
        return todoList;
    }

    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Params.TABLE_NAME,null,null);
        db.close();
    }
    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Params.TABLE_NAME,Params.KEY_ID + "=?",new String[] {String.valueOf(id)});
        db.close();
    }
    public void update(int id, String text, String isCheck){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_ID,id);
        values.put(Params.TODO_TASK,text);
//        values.put(Params.IS_CHECK,isCheck);
        db.update(Params.TABLE_NAME,values,Params.KEY_ID  + "=?", new String[]{String.valueOf(id)});

    }

}
