package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomerPlaceHold extends Activity
        implements OnClickListener, OnItemSelectedListener {
    private static final String TAG = "CustomerPlaceHold";
    // declare widget variables
    private EditText pickupEditText;
    private DatePickerDialog pickupDateDialog;
    private Spinner pickupTimeSpinner;
    private EditText returnEditText;
    private DatePickerDialog returnDateDialog;
    private Spinner returnTimeSpinner;
    private Button exitBtn;
    private TextView dateErrorTextView;
    private Button confirmRentalErrBtn;
    private TableLayout bookTable;

    public void displayAllBooks() {
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Book> books = db.getBooks("");
        String message = "=== Records in Book ===\n";
        message += "title | author | feePerHour\n";
        for (Book book : books) {
            String title = book.getTitle();
            String author = book.getAuthor();
            double feePerHour = book.getFeePerHour();
            message += title + " | " + author + " | " + feePerHour + "\n";
        }
        Log.d(TAG, message);
    }

    public void displayAllBookHolds() {
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<BookHold> bookHolds = db.getBookHolds("");
        String message = "=== Records in BookHold ===\n";
        message += "transType | title | pickupDateHour | returnDateHour\n";
        for (BookHold bookHold : bookHolds) {
            String transType = bookHold.getTransType();
            String title = bookHold.getBookTitle();
            String pickupDateHour = bookHold.getPickupDateHour();
            String returnDateHour = bookHold.getReturnDateHour();
            message += transType + " | " + title + " | " + pickupDateHour + " | " + returnDateHour + "\n";
        }
        Log.d(TAG, message);
    }

    public void displayAvailableBooks(ArrayList<Book> availableBooks) {
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<BookHold> bookHolds = db.getBookHolds("");
        String message = "=== availableBooks ===\n";
        message += "transType | title | pickupDateHour | returnDateHour\n";
        for (Book book : availableBooks) {
            String title = book.getTitle();
            String author = book.getAuthor();
            double feePerHour = book.getFeePerHour();
            message += title + " | " + author + " | " + feePerHour + "\n";
        }
        Log.d(TAG, message);
    }

    public void getAvailableBooks() { // display available books from Book table
        Log.d(TAG, "in getAvailableBooks");
        String pickupDateHour = pickupEditText.getText().toString() + " " + (String)pickupTimeSpinner.getSelectedItem();
        String returnDateHour = returnEditText.getText().toString() + " " + (String)returnTimeSpinner.getSelectedItem();
        Log.d(TAG, "original pickup date/hour: " + pickupDateHour);
        Log.d(TAG, "original return date/hour: " + returnDateHour);
        // convert from date/time input to sqlite date/time
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a"); // from regular
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // to sqlite
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = inputFormat.parse(pickupDateHour);
            d2 = inputFormat.parse(returnDateHour);
        }
        catch (ParseException pe) {
            Log.d(TAG, "parse exception date");
        }
        final String sqlPickupDateHour = outputFormat.format(d1);
        final String sqlReturnDateHour = outputFormat.format(d2);
        Log.d(TAG, "sqlite pickup date/hour: " + sqlPickupDateHour);
        Log.d(TAG, "sqlite return date/hour: " + sqlReturnDateHour);
        final CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Book> allBooks = db.getBooks("");
        displayAllBooks(); // for testing
        displayAllBookHolds(); // for testing
        ArrayList<BookHold> bookHolds = db.getBookHoldsFromDateHour(sqlPickupDateHour, sqlReturnDateHour);
        Log.d(TAG, "bookHolds found: " + String.valueOf(bookHolds.size()));
        final ArrayList<Book> availableBooks = new ArrayList<Book>();
        for (Book book : allBooks) {
            boolean available = false;
            for (BookHold bookHold : bookHolds) {
                if (book.getTitle().equals(bookHold.getBookTitle())) {
                    available = false;
                    break;
                } else {
                    available = true;
                }
            }
            if (available) { // if a book is not in bookHolds
                availableBooks.add(book);
            }
        }
        if (bookHolds.size() == 0) { // if there are no book holds
            for (Book book : allBooks) { // all books are available
                availableBooks.add(book);
            }
        }
        displayAvailableBooks(availableBooks); // for testing
        dateErrorTextView.setVisibility(View.GONE);
        exitBtn.setVisibility(View.GONE);
        dateErrorTextView.setText("");
        bookTable.removeAllViews(); // reset bookTable TableLayout
        // display available books availableBooks
        if (availableBooks.size() == 0) {
            dateErrorTextView.setVisibility(View.VISIBLE);
            String errMessage = "No books are available for the pickup and return dates and times.\n\n";
            errMessage += "Click Exit to go to the main menu.\n";
            dateErrorTextView.setText(errMessage);
            exitBtn.setVisibility(View.VISIBLE);
        } else {
            for (int bookIdx = 0; bookIdx < availableBooks.size(); bookIdx++) {
                TableRow row = new TableRow(this);
                TextView text1 = new TextView(this);
                TextView text2 = new TextView(this);
                TextView text3 = new TextView(this);
                Button rentBtn = new Button(this);
                Book book = availableBooks.get(bookIdx);
                String bookTitle = book.getTitle();
                String bookAuthor = book.getAuthor();
                String bookFeePerHour = String.valueOf(book.getFeePerHour());
                text1.setText(bookTitle);
                text2.setText(bookAuthor);
                text3.setText(bookFeePerHour);
                rentBtn.setText("Rent");
                rentBtn.setId(bookIdx);
                rentBtn.setOnClickListener(new OnClickListener() { // rent btn clicked
                    @Override
                    public void onClick(View v) {
                        int rentIdx = v.getId();
                        Book bookRental = availableBooks.get(rentIdx);
                        String bookRentalTitle = bookRental.getTitle();
                        String bookRentalAuthor = bookRental.getAuthor();
                        double bookRentalFeePerHour = bookRental.getFeePerHour();
                        Log.d(TAG, "requested pickup date/hour: " + sqlPickupDateHour);
                        Log.d(TAG, "requested return date/hour: " + sqlReturnDateHour);
                        Log.d(TAG, "requested title: " + bookRentalTitle);
                        Log.d(TAG, "requested author: " + bookRentalAuthor);
                        Log.d(TAG, "requested feeperhour: " + bookRentalFeePerHour);
                        // redirect to customer login
                        Log.d(TAG, "redirect to customer login");
                        Intent customerLogin = new Intent(getApplicationContext(), CustomerLogin.class);
                        Bundle extraInfo = new Bundle();
                        extraInfo.putString("sqlPickupDateHour", sqlPickupDateHour);
                        extraInfo.putString("sqlReturnDateHour", sqlReturnDateHour);
                        extraInfo.putString("bookTitle", bookRentalTitle);
                        extraInfo.putDouble("bookFeePerHour", bookRentalFeePerHour);
                        customerLogin.putExtras(extraInfo);
                        startActivity(customerLogin); // reservation number gets initialized in customerLogin
                    }
                });
                row.addView(text1);
                row.addView(text2);
                row.addView(text3);
                row.addView(rentBtn);
                bookTable.addView(row);
            }
        }
    }

    public boolean validDates() {
        Log.d(TAG, "in validDates");
        boolean validDates = false;
        confirmRentalErrBtn.setVisibility(View.GONE);
        String pickupDateHour = pickupEditText.getText().toString() + " " + (String)pickupTimeSpinner.getSelectedItem();
        String returnDateHour = returnEditText.getText().toString() + " " + (String)returnTimeSpinner.getSelectedItem();
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = inputFormat.parse(pickupDateHour);
            d2 = inputFormat.parse(returnDateHour);
        }
        catch (ParseException pe) {
            Log.d(TAG, "parse exception");
        }
        if (d1.compareTo(d2) > 0) {
            validDates = false;
            Log.d(TAG, "pickup after return");
            bookTable.setVisibility(View.GONE);
            dateErrorTextView.setVisibility(View.VISIBLE);
            dateErrorTextView.setText("Pickup date/hour is after return date/hour.");
            confirmRentalErrBtn.setVisibility(View.VISIBLE);
            exitBtn.setVisibility(View.GONE);
        }
        else if (d1.compareTo(d2) < 0) {
            validDates = true;
            Log.d(TAG, "pickup before return");
        }
        else if (d1.compareTo(d2) == 0) {
            validDates = true;
            Log.d(TAG, "pickup equal to return");
        }
        return validDates;
    }

    public double calculateDaysBetweenDates(){
        Log.d(TAG, "in calculateDaysBetweenDates");
        String pickupDateHour = pickupEditText.getText().toString() + " " + (String)pickupTimeSpinner.getSelectedItem();
        String returnDateHour = returnEditText.getText().toString() + " " + (String)returnTimeSpinner.getSelectedItem();
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        double daysBetween = 0.0;
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = inputFormat.parse(pickupDateHour);
            d2 = inputFormat.parse(returnDateHour);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();
        daysBetween = (int)(diff / (24 * 60 * 60 * 1000));
        Log.d(TAG, "days between dates: " + String.valueOf(daysBetween));
        return daysBetween;
    }

    public void doAllDateChecks() {
        Log.d(TAG, "in doAllDateChecks");
        String pickupDate = pickupEditText.getText().toString();
        String returnDate = returnEditText.getText().toString();
        if (!pickupDate.equals("") && !returnDate.equals("")) { // pickupDate and returnDate are not empty
            Log.d(TAG, "pickup date and return date are not empty");
            if (validDates()) { // dates are valid
                Log.d(TAG, "pickup date and return date are valid");
                if (calculateDaysBetweenDates() <= 7) { // dates are between 7 days
                    Log.d(TAG, "pickup date and return date are between 7 days");
                    dateErrorTextView.setVisibility(View.GONE);
                    dateErrorTextView.setText("");
                    confirmRentalErrBtn.setVisibility(View.GONE);
                    bookTable.setVisibility(View.VISIBLE);
                    getAvailableBooks();
                }
                else { // dates are more than 7 days between each other
                    Log.d(TAG, "pickup date and return date are more than 7 days between each other");
                    dateErrorTextView.setVisibility(View.VISIBLE);
                    confirmRentalErrBtn.setVisibility(View.VISIBLE);
                    bookTable.setVisibility(View.GONE);
                    String errMessage = "A book rental can not be reserved for more than 7 days.";
                    dateErrorTextView.setText(errMessage);
                    exitBtn.setVisibility(View.GONE);
                }
            }
            else {
                Log.d(TAG, "dates are invalid");
            }
        }
        else {
            Log.d(TAG, "pickup date and/or return date is empty");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_place_hold);

        pickupEditText = (EditText)findViewById(R.id.pickupDateEditText);
        pickupEditText.setOnClickListener(this);

        // get a reference to the spinner
        pickupTimeSpinner = (Spinner)findViewById(R.id.pickupTimeSpinner);

        // create an array adapter with specified array and layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.time_array,
                        android.R.layout.simple_spinner_item);

        // set the layout for the drop-down list
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // set the adapter for the spinner
        pickupTimeSpinner.setAdapter(adapter);
        pickupTimeSpinner.setOnItemSelectedListener(this); // adapter needs to be initialized first!!

        returnEditText = (EditText)findViewById(R.id.returnDateEditText);
        returnEditText.setOnClickListener(this);

        // get a reference to the spinner
        returnTimeSpinner = (Spinner)findViewById(R.id.returnTimeSpinner);

        // create an array adapter with specified array and layout
        adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.time_array,
                        android.R.layout.simple_spinner_item);

        // set the layout for the drop-down list
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // set the adapter for the spinner
        returnTimeSpinner.setAdapter(adapter);
        returnTimeSpinner.setOnItemSelectedListener(this); // adapter needs to be initialized first!!

        exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);

        dateErrorTextView = (TextView)findViewById(R.id.dateErrorTextView);
        confirmRentalErrBtn = (Button)findViewById(R.id.confirmRentalErrBtn);
        confirmRentalErrBtn.setOnClickListener(this);
        bookTable = (TableLayout)findViewById(R.id.bookTable);
    }

    @Override
    public void onClick (View v){
        Calendar calendar = Calendar.getInstance();
        int month = 0;
        int day = 0;
        int year = 0;

        if (v.getId() == R.id.pickupDateEditText) { // pickupDateEditText clicked
            Log.d(TAG, "Select pickup date clicked");
            String pickupText = pickupEditText.getText().toString();
            if (pickupText.equals("")) {
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
            } else {
                String[] str = pickupText.split("/");
                month = Integer.parseInt(str[0]) - 1;
                day = Integer.parseInt(str[1]);
                year = Integer.parseInt(str[2]);
            }
            pickupDateDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date = monthOfYear + 1 + "/" + dayOfMonth + "/" + year;
                            pickupEditText.setText(date);
                            doAllDateChecks();
                        }
                    }, year, month, day);
            pickupDateDialog.show();
        } else if (v.getId() == R.id.returnDateEditText) { // returnDateEditText clicked
            Log.d(TAG, "Select return date clicked");
            String returnText = returnEditText.getText().toString();
            if (returnText.equals("")) {
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
            } else {
                String[] str = returnText.split("/");
                month = Integer.parseInt(str[0]) - 1;
                day = Integer.parseInt(str[1]);
                year = Integer.parseInt(str[2]);
            }
            returnDateDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date = monthOfYear + 1 + "/" + dayOfMonth + "/" + year;
                            returnEditText.setText(date);
                            doAllDateChecks();
                        }
                    }, year, month, day);
            returnDateDialog.show();
        } else if (v.getId() == R.id.confirmRentalErrBtn) {
            Log.d(TAG, "OK clicked");
            Log.d(TAG, "redirect to customer place hold");
            // redirect to customer place hold activity
            Intent placeHold = new Intent(getApplicationContext(), CustomerPlaceHold.class);
            startActivity(placeHold);
        } else if (v.getId() == R.id.exitBtn) {
            // redirect to main menu activity
            Log.d(TAG, "Exit clicked");
            Log.d(TAG, "redirect to main menu");
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Log.d(TAG,"spinner item selected");
        doAllDateChecks();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // YOU NEED THIS FUNCTION DECLARED FOR setOnItemSelectedListener
    }
}
