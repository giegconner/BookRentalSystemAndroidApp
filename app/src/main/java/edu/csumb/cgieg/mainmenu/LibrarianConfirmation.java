package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

public class LibrarianConfirmation extends Activity implements OnClickListener {
    private static final String TAG = "LibrarianConfirmation";
    // declare widget variables
    private TextView librarianConfirmTextView;
    private Button librarianConfirmBtn;

    // declare librarian confirmation variables
    private String bookTitle;
    private String bookAuthor;
    private String bookFeePerHour;
    private double bookFeePerHourNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_confirmation);

        librarianConfirmTextView = (TextView)findViewById(R.id.librarianConfirmTextView);
        librarianConfirmBtn = (Button)findViewById(R.id.librarianConfirmBtn);
        librarianConfirmBtn.setOnClickListener(this);

        bookTitle = "";
        bookAuthor = "";
        bookFeePerHour = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookTitle = extras.getString("bookTitle");
            bookAuthor = extras.getString("bookAuthor");
            bookFeePerHour = extras.getString("bookFeePerHour");
            String message = "";
            message += "New Book Confirmation\n\n";
            message += "Book Title: " + bookTitle + "\n";
            message += "Book Author: " + bookAuthor + "\n";
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            bookFeePerHourNum = Double.parseDouble(bookFeePerHour);
            String bookFeePerHourStr = formatter.format(bookFeePerHourNum);
            message += "Book Fee Per Hour: " + bookFeePerHourStr + "\n";
            librarianConfirmTextView.setText(message);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.librarianConfirmBtn) { // confirm btn clicked
            Log.d(TAG, "Confirm clicked");
            Log.d(TAG, "redirect to main menu");
            // redirect to main menu activity
            CsumbLibraryDB db = new CsumbLibraryDB(this);
            Book book = new Book(bookTitle, bookAuthor, bookFeePerHourNum);
            // insert into Book
            db.insertBook(book);
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
