package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CustomerCancelHold extends Activity implements OnClickListener {
    private static final String TAG = "CustomerCancelHold";
    // declare widget variables
    private TableLayout placeHoldTable;
    private TextView customerNoHoldsTextView;
    private Button noReserveOkBtn;

    // declare passed username variable
    private String passedUsername = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cancel_hold);

        placeHoldTable = (TableLayout)findViewById(R.id.placeHoldTable);
        customerNoHoldsTextView = (TextView)findViewById(R.id.customerNoHoldsTextView);
        noReserveOkBtn = (Button)findViewById(R.id.noReserveOkBtn);
        noReserveOkBtn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passedUsername = extras.getString("username");
        }
        // display all book holds with Place Hold and customer's username
        final CsumbLibraryDB db = new CsumbLibraryDB(this);
        TableRow firstRow = new TableRow(this);
        TextView headerText = new TextView(this);
        headerText.setText(passedUsername + "'s Book Holds\n");
        firstRow.addView(headerText);
        placeHoldTable.addView(firstRow);
        ArrayList<BookHold> bookHolds = db.getBookHolds(passedUsername);
        Log.d(TAG, "bookHolds size: " + bookHolds.size());
        if (bookHolds.size() == 0) {
            headerText.setText("");
            customerNoHoldsTextView.setText(passedUsername + " does not have any book holds.");
            noReserveOkBtn.setVisibility(View.VISIBLE);
        }
        else {
            customerNoHoldsTextView.setText("");
            for (final BookHold bookHold : bookHolds) {
                final int reservationNum = bookHold.getReserveNum();
                final String reservationNumStr = String.valueOf(reservationNum);
                final String pickupDateHour = bookHold.getPickupDateHour();
                final String returnDateHour = bookHold.getReturnDateHour();
                final String bookTitle = bookHold.getBookTitle();
                TableRow cancelRow = new TableRow(this);
                Button cancelBtn = new Button(this);
                cancelBtn.setText("Cancel");
                cancelRow.addView(cancelBtn);
                placeHoldTable.addView(cancelRow);
                TableRow bookInfoHeaderRow = new TableRow(this);
                TextView reserveHeader = new TextView(this);
                TextView titleHeader = new TextView(this);
                reserveHeader.setPaintFlags(reserveHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                reserveHeader.setText("Reservation #");
                titleHeader.setPaintFlags(titleHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                titleHeader.setText("Book Title");
                bookInfoHeaderRow.addView(reserveHeader);
                bookInfoHeaderRow.addView(titleHeader);
                cancelBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) { // cancel btn clicked
                        Log.d(TAG, "Cancel clicked");
                        AlertDialog alertDialog = new AlertDialog.Builder(CustomerCancelHold.this).create();
                        alertDialog.setTitle("Would you like to cancel this book hold?");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() { // No btn clicked
                            public void onClick(DialogInterface dialog, int which) {
                                // No alert dialog btn clicked
                                Log.d(TAG, "No clicked");
                                // do not remove any records
                            }
                        });
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() { // Yes btn clicked
                            public void onClick(DialogInterface dialog, int which) {
                                // Yes alert dialog btn clicked
                                Log.d(TAG, "Yes clicked");
                                // update book hold trans. type, total, and datetime
                                db.updateBookHold(bookHold);
                                // redirect to main menu activity
                                Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
                                startActivity(mainMenu);
                            }
                        });

                        alertDialog.show(); // show alert dialog
                    }
                });
                placeHoldTable.addView(bookInfoHeaderRow);
                TableRow bookInfoRow = new TableRow(this);
                TextView reserveText = new TextView(this);
                TextView titleText = new TextView(this);
                reserveText.setText(reservationNumStr);
                titleText.setText(bookTitle);
                bookInfoRow.addView(reserveText);
                bookInfoRow.addView(titleText);
                placeHoldTable.addView(bookInfoRow);
                TableRow pickupDateHourHeaderRow = new TableRow(this);
                TextView pickupDateHourHeader = new TextView(this);
                pickupDateHourHeader.setPaintFlags(pickupDateHourHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                pickupDateHourHeader.setText("Pickup Date/Hour");
                pickupDateHourHeaderRow.addView(pickupDateHourHeader);
                placeHoldTable.addView(pickupDateHourHeaderRow);
                TableRow pickupDateHourRow = new TableRow(this);
                TextView pickupDateHourText = new TextView(this);
                pickupDateHourText.setText(pickupDateHour);
                pickupDateHourRow.addView(pickupDateHourText);
                placeHoldTable.addView(pickupDateHourRow);
                TableRow returnDateHourHeaderRow = new TableRow(this);
                TextView returnDateHourHeader = new TextView(this);
                returnDateHourHeader.setPaintFlags(returnDateHourHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                returnDateHourHeader.setText("Return Date/Hour");
                returnDateHourHeaderRow.addView(returnDateHourHeader);
                placeHoldTable.addView(returnDateHourHeaderRow);
                TableRow returnDateHourRow = new TableRow(this);
                TextView returnDateHourText = new TextView(this);
                returnDateHourText.setText(returnDateHour);
                returnDateHourRow.addView(returnDateHourText);
                placeHoldTable.addView(returnDateHourRow);
                TableRow emptyRow = new TableRow(this);
                TextView emptyText = new TextView(this);
                emptyText.setText("");
                emptyRow.addView(emptyText);
                placeHoldTable.addView(emptyRow);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.noReserveOkBtn) { // Ok btn clicked
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
