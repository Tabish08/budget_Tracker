package com.prakhar.budget_tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final List<Expense> expenseList;
    private final Context context;
    private final OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public ExpenseAdapter(Context context, List<Expense> expenses, OnDeleteClickListener listener) {
        this.context = context;
        this.expenseList = expenses;
        this.deleteClickListener = listener;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtAmount, txtDate, txtNote;
        ImageButton btnDelete;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNote = itemView.findViewById(R.id.txtNote);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.txtCategory.setText(expense.getCategory());
        holder.txtAmount.setText("₹ " + expense.getAmount());
        holder.txtDate.setText(expense.getDateTime());
        holder.txtNote.setText("Spent at: " + expense.getNote());
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDelete(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
