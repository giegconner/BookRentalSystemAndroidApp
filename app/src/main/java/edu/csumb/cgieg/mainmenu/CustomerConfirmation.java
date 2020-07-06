package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerConfirmation extends Activity implements OnClickListener {
    private static final String TAG = "CustomerConfirmation";
    // declare widget variables
    private TextView bookConfirmTextView;
    private Button confirmBtn;

    // declare book rental confirmation variables
    private String username = "";
    private String sqlPickupDateHour = "";
    private String sqlReturnDateHour = "";
    private String bookTitle = "";
    private int reserveNum = 0; // this gets generated here!!
    private int hours = 0;
    private double bookFeePerHour = 0.0;
    private double totalAmount = 0.0;
    private String totalAmountStr = "";

    public String getDateHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = new Date();
        String currDateTime = sdf.format(date);
        return currDateTime;
    }

    public void displayBookHoldTable() {
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<BookHold> bookHolds = db.getBookHolds("");
        String message = "=== Records in BookHold ===\n";
        message += "reserveNum | username\n";
        for (BookHold bookHold : bookHolds) {
            message += bookHold.getReserveNum() + " | " + bookHold.getUsername() + "\n";
        }
        Log.d(TAG, message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_confirmation);

        Bundle extras = getIntent().getExtras();

        bookConfirmTextView = (TextView)findViewById(R.id.bookConfirmTextView);
        if (extras != null) {
            username = extras.getString("username");
            sqlPickupDateHour = extras.getString("sqlPickupDateHour");
            sqlReturnDateHour = extras.getString("sqlReturnDateHour");
            bookTitle = extras.getString("bookTitle");
            CsumbLibraryDB db = new CsumbLibraryDB(this);
            ArrayList<BookHold> bookHolds = db.getBookHolds(""); // need this for generating reserveNum
            if (bookHolds.size() == 0) {
                reserveNum = 1;
            }
            else {
                reserveNum = bookHolds.size() + 1;
            }
            bookFeePerHour = extras.getDouble("bookFeePerHour");
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // from sqlite
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a"); // to regular
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = inputFormat.parse(sqlPickupDateHour);
                d2 = inputFormat.parse(sqlReturnDateHour);
                long diff = (d2.getTime() - d1.getTime());
                hours = (int)(diff / (60 * 60 * 1000)); // you need this for totalAmountOwed
                totalAmount = hours * bookFeePerHour;
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                totalAmountStr = formatter.format(totalAmount); // for display purposes
            } catch (ParseException e) {
                Log.d(TAG,"parse exception");
            }
            Log.d(TAG, "username: " + username);
            Log.d(TAG, "pickup date/time: " + sqlPickupDateHour);
            Log.d(TAG, "return date/time: " + sqlReturnDateHour);
            Log.d(TAG, "book title: " + bookTitle);
            Log.d(TAG, "reservation number: " + reserveNum);
            Log.d(TAG, "hours between: " + hours);
            Log.d(TAG, "bookFeePerHour: " + bookFeePerHour);
            Log.d(TAG, "total amount owed: " + totalAmountStr);
            String message = "";
            message = "Book Rental Confirmation\n\n";
            message += "Username: " + username + "\n";
            String pickupDateHourStr = "";
            String returnDateHourStr = "";
            pickupDateHourStr = outputFormat.format(d1);
            returnDateHourStr = outputFormat.format(d2);
            message += "Pickup Date/Hour: " + pickupDateHourStr + "\n";
            message += "Return Date/Hour: " + returnDateHourStr + "\n";
            message += "Book Title: " + bookTitle + "\n";
            message += "Reservation Number: " + reserveNum + "\n";
            message += "Total Amount: " + totalAmountStr + "\n";
            bookConfirmTextView.setTextColor(Color.parseColor("#808080"));
            bookConfirmTextView.setText(message);
        }

        confirmBtn = (Button)findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmBtn) {
            Log.d(TAG, "Confirm clicked");
            Log.d(TAG, "redirect to main menu");
            // record the book hold transaction
            CsumbLibraryDB db = new CsumbLibraryDB(this);
            BookHold bookHold = new BookHold("Place hold", username, sqlPickupDateHour,
                    sqlReturnDateHour, bookTitle, reserveNum, totalAmount, getDateHour());
            db.insertBookHold(bookHold);

            // redirect to main menu activity
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);

            displayBookHoldTable(); // for testing
        }
    }
}
