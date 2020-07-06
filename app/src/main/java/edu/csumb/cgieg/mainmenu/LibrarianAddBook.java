package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class LibrarianAddBook extends Activity implements OnClickListener {
    private static final String TAG = "LibrarianAddBook";
    // declare widget variables
    private EditText librarianBookTitleEditText;
    private EditText librarianAuthorEditText;
    private EditText librarianFeePerHourEditText;
    private Button librarianAddBookBtn;
    private TextView librarianValidBookMessageTextView;
    private Button librarianOkBtn;

    public boolean validBookInfo() {
        boolean validBookInfo = false;
        String bookTitle = librarianBookTitleEditText.getText().toString();
        String bookAuthor = librarianAuthorEditText.getText().toString();
        String bookFeePerHour = librarianFeePerHourEditText.getText().toString();
        if (!bookTitle.equals("") && !bookAuthor.equals("") && !bookFeePerHour.equals("")) { // all fields are not empty
            CsumbLibraryDB db = new CsumbLibraryDB(this);
            ArrayList<Book> books = db.getBooks(bookTitle);
            if (books.size() == 0) { // book does not exist in Book
                validBookInfo = true;
            }
            else { // book exists
                validBookInfo = false;
                librarianValidBookMessageTextView.setText("Book Title: " + bookTitle + " already exists in the library.");
            }
        }
        else {
            // librarian entered at least 1 empty field
             librarianValidBookMessageTextView.setText("At least one field was empty while adding a new book.");
        }
        return validBookInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_add_book);

        // initialize widget variables
        librarianBookTitleEditText = (EditText)findViewById(R.id.librarianBookTitleEditText);
        librarianAuthorEditText = (EditText)findViewById(R.id.librarianAuthorEditText);
        librarianFeePerHourEditText = (EditText)findViewById(R.id.librarianFeePerHourEditText);
        librarianAddBookBtn = (Button)findViewById(R.id.librarianAddBookBtn);
        librarianValidBookMessageTextView = (TextView)findViewById(R.id.librarianValidBookMessageTextView);
        librarianOkBtn = (Button)findViewById(R.id.librarianOkBtn);
        librarianOkBtn.setOnClickListener(this);
        librarianAddBookBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.librarianAddBookBtn) { // add book btn clicked
            Log.d(TAG, "Add Book clicked");
            if (validBookInfo()) {
                Log.d(TAG, "book info is valid");
                Log.d(TAG, "redirect to librarian confirmation");
                // redirect to librarian confirmation activity
                Bundle extraInfo = new Bundle();
                String bookTitle = librarianBookTitleEditText.getText().toString();
                String bookAuthor = librarianAuthorEditText.getText().toString();
                String bookFeePerHour = librarianFeePerHourEditText.getText().toString();
                extraInfo.putString("bookTitle", bookTitle);
                extraInfo.putString("bookAuthor", bookAuthor);
                extraInfo.putString("bookFeePerHour", bookFeePerHour);
                Intent librarianConfirmation = new Intent(getApplicationContext(), LibrarianConfirmation.class);
                librarianConfirmation.putExtras(extraInfo);
                startActivity(librarianConfirmation);
            }
            else {
                Log.d(TAG, "book info is invalid");
                // inform the librarian the book info is invalid
                librarianBookTitleEditText.setVisibility(View.GONE);
                librarianAuthorEditText.setVisibility(View.GONE);
                librarianFeePerHourEditText.setVisibility(View.GONE);
                librarianAddBookBtn.setVisibility(View.GONE);
                librarianOkBtn.setVisibility(View.VISIBLE);
            }
        }
        else if (v.getId() == R.id.librarianOkBtn) { // exit btn clicked
            Log.d(TAG, "OK clicked");
            Log.d(TAG, "redirect to main menu");
            // redirect to main menu activity
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
