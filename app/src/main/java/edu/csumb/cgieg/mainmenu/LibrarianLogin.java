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
import android.widget.Toast;
import java.util.ArrayList;

public class LibrarianLogin extends Activity implements OnClickListener {
    private static final String TAG = "LibrarianLogin";
    // declare widget variables
    private EditText librarianUsernameEditText;
    private EditText librarianPasswordEditText;
    private Button librarianLoginBtn;
    private TextView librarianLoginMessageTextView;
    private Button librarianLoginOkBtn;

    // declare attempts variable
    private static int attempts = 0;

    public boolean validLogin() {
        boolean validLogin = false;
        String username = librarianUsernameEditText.getText().toString();
        String password = librarianPasswordEditText.getText().toString();
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Librarian> librarians = db.checkLibrarians(username, password);
        if (librarians.size() == 1) { // valid librarian
            validLogin = true;
            attempts = 0;
        }
        return validLogin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_login);
        
        librarianUsernameEditText = (EditText)findViewById(R.id.librarianUsernameEditText);
        librarianPasswordEditText = (EditText)findViewById(R.id.librarianPasswordEditText);
        librarianLoginBtn = (Button)findViewById(R.id.librarianLoginBtn);
        librarianLoginMessageTextView = (TextView)findViewById(R.id.librarianLoginMessageTextView);
        librarianLoginBtn.setOnClickListener(this);
        librarianLoginOkBtn = (Button)findViewById(R.id.librarianLoginOkBtn);
        librarianLoginOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.librarianLoginBtn) { // Login btn clicked
            Log.d(TAG,"Login clicked");
            String message = "";
            String prompt = "Please try again.";
            if (validLogin()) {
                Toast.makeText(this, "You are now logged in", Toast.LENGTH_LONG).show(); // special toast
                Log.d(TAG, "redirect to librarian logs");
                // redirect to librarian logs activity
                librarianLoginMessageTextView.setText("");
                Intent librarianShowLogs = new Intent(getApplicationContext(), LibrarianLogs.class);
                startActivity(librarianShowLogs);
            }
            else {
                Log.d(TAG, "librarian username and/or password is invalid");
                message = "Username and/or password is invalid";
                librarianLoginMessageTextView.setText(message + " " + prompt);
                attempts++;
            }
            if (attempts == 2) {
                attempts = 0;
                librarianUsernameEditText.setFocusable(false);
                librarianPasswordEditText.setFocusable(false);
                librarianLoginBtn.setVisibility(View.GONE);
                librarianLoginMessageTextView.setText("");
                librarianLoginMessageTextView.setText(message + " again. Click OK to go to the main menu.");
                librarianLoginOkBtn.setVisibility(View.VISIBLE);
            }
        }
        else if (v.getId() == R.id.librarianLoginOkBtn) { // OK btn clicked
            Log.d(TAG, "OK clicked");
            Log.d(TAG, "redirect to main menu");
            // redirect to main menu activity
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
