package com.shahnwaz.todohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    CollectionReference collectionReference;
    FirebaseFirestore db;
    Button openDatePickerButton;
    RecyclerView taskRecyclerView;
    FloatingActionButton fab;
    ToDoAdapter adapter;
    List<ToDoItem> itemList = new ArrayList<>();
    EditText etNewTaskText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDatePickerButton = findViewById(R.id.openDatePickerButton);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        fab = findViewById(R.id.fab);
        etNewTaskText = findViewById(R.id.etNewTaskText);

        adapter = new ToDoAdapter(itemList);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String taskText= etNewTaskText.getText().toString().trim();

                if (!taskText.isEmpty()) {
                    itemList.add(new ToDoItem(taskText, false));
                    adapter.notifyItemInserted(itemList.size() - 1);
                    etNewTaskText.getText().clear();
                }
            }
        });

        openDatePickerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    }
                },year,month,day);

                datePickerDialog.show();
            }

        });

    }
    private void addTaskToFireStore(String taskText, boolean completed, Date dueDate){
        Map<String,Object> taskData = new HashMap<>();
        taskData.put("task", taskText);
        taskData.put("completed", completed);

        collectionReference.add(taskData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,"Task Added to FireStore",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error loading task from fireStore",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadTasksFromFireStore (){
        collectionReference.orderBy("task", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        itemList.clear();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            ToDoItem toDoItem = documentSnapshot.toObject(ToDoItem.class);
                            itemList.add(toDoItem);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error Loading Task from fireStore",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}