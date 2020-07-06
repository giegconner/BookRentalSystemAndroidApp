package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LibrarianLogs extends Activity implements OnClickListener {
    private static final String TAG = "LibrarianLogs";
    // declare widget variables
    private Button librarianOkBtn;
    private TextView librarianLogsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_logs);

        librarianOkBtn = (Button)findViewById(R.id.librarianOkBtn);
        librarianOkBtn.setOnClickListener(this);
        librarianLogsTextView = (TextView)findViewById(R.id.librarianLogsTextView);

        String transactionsStr = "";
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        // display whole Customer table
        transactionsStr += "*** Customer ***\n";
        transactionsStr += "Trans. Type | Username | Trans. Date/Hour\n";
        transactionsStr += "--------------------------------------------------\n";
        ArrayList<Customer> customers = db.getCustomers("");
        for (Customer customer : customers) {
            String transType = customer.getTransType();
            String username = customer.getUsername();
            String dateHour = customer.getDateHour();
            if (transType.equals("New account")) {
                transactionsStr += transType + " | " + username + " | " + dateHour + "\n" +
                        "--------------------------------------------------\n";
            }
        }

        // display whole PlaceBookHold table
        transactionsStr += "\n*** BookHold ***\n";
        transactionsStr += "Trans. Type | Username | Book Title |\n" +
            "Pickup Date/Hour | Return Date/Hour |\n" +
            "Reserve # | Total | Trans. Date/Hour\n";
        transactionsStr += "--------------------------------------------------\n";
        ArrayList<BookHold> bookHolds = db.getBookHolds("");
        for (BookHold bookHold : bookHolds) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String transType = bookHold.getTransType();
            String username = bookHold.getUsername();
            String bookTitle = bookHold.getBookTitle();
            String pickupDateHour = bookHold.getPickupDateHour();
            String returnDateHour = bookHold.getReturnDateHour();
            int reserveNum = bookHold.getReserveNum();
            double totalAmount = bookHold.getTotalAmount();
            String totalAmountStr = formatter.format(totalAmount);
            String dateHour = bookHold.getDateHour();

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // from sqlite
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a"); // to regular
            String pickupDateHourStr = "";
            String returnDateHourStr = "";
            try {
                Date d1 = inputFormat.parse(pickupDateHour);
                Date d2 = inputFormat.parse(returnDateHour);
                pickupDateHourStr = outputFormat.format(d1);
                returnDateHourStr = outputFormat.format(d2);
                Log.d(TAG, "pickup date/hour: " + pickupDateHourStr);
                Log.d(TAG, "return date/hour: " + returnDateHourStr);
            }
            catch (ParseException pe) {
                Log.d(TAG,"parse exception");
            }
            transactionsStr += transType + " | " + username + " | " + bookTitle + " |\n" +
                    pickupDateHourStr + " | " + returnDateHourStr + " |\n" +
                    reserveNum + " | ";
            if (transType.equals("Place hold")) {
                transactionsStr += totalAmountStr;
            }
            else {
                transactionsStr += "$0.00";
            }
            transactionsStr += " | " + dateHour + "\n";
            transactionsStr += "--------------------------------------------------\n";
        }

        librarianLogsTextView.setText(transactionsStr);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.librarianOkBtn) { // Ok btn clicked
            Log.d(TAG, "OK clicked");
            AlertDialog alertDialog = new AlertDialog.Builder(LibrarianLogs.this).create(); // alert dialog
            alertDialog.setTitle("Would you like to add a new book to the library?");
            alertDialog.setCancelable(false); // user must select an option

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener() { // No btn clicked
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "No clicked");
                    Log.d(TAG, "redirect to main menu");
                    // redirect to main menu activity
                    Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(mainMenu);
                }
            });
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() { // Yes btn clicked
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Yes clicked");
                    Log.d(TAG, "redirect to librarian add book");
                    // redirect to librarian add book activity
                    Intent librarianAddBook = new Intent(getApplicationContext(), LibrarianAddBook.class);
                    startActivity(librarianAddBook);
                }
            });

            alertDialog.show();  // show alert dialog
        }
    }
}
