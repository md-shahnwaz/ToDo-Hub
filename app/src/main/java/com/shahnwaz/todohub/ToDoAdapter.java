package com.shahnwaz.todohub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>{

    private List<ToDoItem> itemList;

    public ToDoAdapter(List<ToDoItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_single_item,parent,false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem item = itemList.get(position);
        holder.txtTask.setText(item.getTask());
        holder.checkBox.setChecked(item.isCompleted());
        holder.txtDate.setText(item.getTask());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setCompleted(compoundButton.isChecked());
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.remove(position);
                notifyItemRemoved(position);

            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView txtTask;
        Button btnDelete;

        TextView txtDate;


        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);


            checkBox = itemView.findViewById(R.id.checkbox);
            txtTask = itemView.findViewById(R.id.txtTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }


}
