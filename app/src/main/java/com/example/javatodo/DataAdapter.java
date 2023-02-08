package com.example.javatodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.javatodo.data.DatabaseHandler;
import com.example.javatodo.model.TodoModel;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<TodoModel> data;
    private Context context;
    DatabaseHandler mainBD;
    AlertDialog.Builder builder;

    public DataAdapter(List<TodoModel> data, Context context) {
        this.data = data;
        this.context = context;
        notifyDataSetChanged();
    }
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View row = layoutInflater.inflate(R.layout.activity_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoModel getList = data.get(position);
        holder.textView.setText(getList.getText());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    Toast.makeText(context, "Task done!" , Toast.LENGTH_SHORT).show();
                } else {
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() & Paint.ANTI_ALIAS_FLAG);
                }
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() { // FOR DELETE ONE ITEM
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(context);
                mainBD = new DatabaseHandler(context);
                builder.setMessage("Are you sure you want to delete this task?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TodoModel taskList = data.get(holder.getAdapterPosition());
                                mainBD.delete(taskList.getId());
                                data.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete Item");
                alert.show();

            }
        });
        holder.editeBtn.setOnClickListener(new View.OnClickListener() { // FOR UPDATE ONE ITEM
            @Override
            public void onClick(View view) {
                mainBD = new DatabaseHandler(context);
                TodoModel taskList = data.get(holder.getAdapterPosition());
                int _id = taskList.getId();
                String _text = taskList.getText();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_dialog_box);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();

                EditText updateInput = dialog.findViewById(R.id.updateInput);
                Button updateBtn = dialog.findViewById(R.id.updateBtn);

                updateInput.setText(_text);
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String  updatedText = updateInput.getText().toString().trim();
                        mainBD.update(_id,updatedText,"0");
                        data.clear();
                        data.addAll(mainBD.getAllData());
                        notifyDataSetChanged();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageButton deleteBtn , editeBtn;
        public CheckBox checkBox;

        public ViewHolder(View itemView){
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
            this.editeBtn = (ImageButton) itemView.findViewById(R.id.editeBtn);
            this.checkBox = (CheckBox) itemView.findViewById((R.id.checkBox));

        }

    }
}
