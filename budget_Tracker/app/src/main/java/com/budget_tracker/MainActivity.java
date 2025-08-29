package com.prakhar.budget_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerExpenses;
    private ExpenseAdapter adapter;
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final String FILE_NAME = "expenses.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerExpenses = findViewById(R.id.recyclerExpenses);
        recyclerExpenses.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExpenseAdapter(this, expenses, position -> {
            if (position >= 0 && position < expenses.size()) {
                Expense toDelete = expenses.get(position);
                deleteExpenseFromFile(toDelete);
                expenses.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        recyclerExpenses.setAdapter(adapter);

        FloatingActionButton addExpense = findViewById(R.id.btnAddExpense);
        addExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, 1);
        });


        loadExpensesFromFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadExpensesFromFile(); // reload after saving
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        expenses.clear();
//        loadExpensesFromFile();
        adapter.notifyDataSetChanged();  // Ensure UI refresh
    }
    private void loadExpensesFromFile() {
        expenses.clear(); // clear old list

        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    String amount = parts[1].trim();
                    String dateTime = parts[2].trim();
                    String note = parts[3].trim();
                    expenses.add(new Expense(category, amount, dateTime, note));
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
    private void deleteExpenseFromFile(Expense toDelete) {
        File file = new File(getFilesDir(), FILE_NAME);
        File tempFile = new File(getFilesDir(), "temp_expenses.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    String amount = parts[1].trim();
                    String dateTime = parts[2].trim();
                    String note = parts[3].trim();

                    // Only skip if ALL fields match
                    if (category.equals(toDelete.getCategory().trim()) &&
                            amount.equals(toDelete.getAmount().trim()) &&
                            dateTime.equals(toDelete.getDateTime().trim()) &&
                            note.equals(toDelete.getNote().trim())) {
                        continue; // skip writing this line (i.e., delete it)
                    }
                }

                // keep all other lines
                writer.write(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Deletion error", Toast.LENGTH_SHORT).show();
            return;
        }

        // Replace the old file
        if (file.delete()) {
            tempFile.renameTo(file);
        }
    }



}
