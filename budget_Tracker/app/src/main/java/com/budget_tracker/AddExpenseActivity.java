package com.prakhar.budget_tracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    EditText edtAmount, edtNote;
    AutoCompleteTextView spinnerCategory;
    MaterialButton btnSave;

    String[] categories = {"Food", "Transport", "Shopping", "Health", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        edtAmount = findViewById(R.id.edtAmount);
        edtNote = findViewById(R.id.edtNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) spinnerCategory.showDropDown();
        });

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amount = edtAmount.getText().toString().trim();
        String category = spinnerCategory.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        if (amount.isEmpty()) {
            edtAmount.setError("Amount required");
            return;
        }

        if (category.isEmpty()) {
            spinnerCategory.setError("Please select a category");
            return;
        }

        String record = category + " | " + amount + " | " + datetime + " | " + note + "\n";

        try {
            File file = new File(getFilesDir(), "expenses.txt");
            FileWriter writer = new FileWriter(file, true);
            writer.append(record);
            writer.close();
            Toast.makeText(this, "Expense saved!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Notify MainActivity
            finish();

        } catch (IOException e) {
            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
        }
    }
}
