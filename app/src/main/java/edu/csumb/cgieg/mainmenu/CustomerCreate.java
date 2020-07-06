package edu.csumb.cgieg.mainmenu;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
import android.widget.Toast;

public class CustomerCreate extends Activity implements OnClickListener {
    private static final String TAG = "CustomerCreate";
    // declare widget variables
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createAccountBtn;
    private TextView validMessageTextView;
    private Button customerOkBtn;

    // declare attempts
    private static int attempts = 0;

    public boolean validFormat(String field) {
        int numOfDigits = 0;
        int numOfLetters = 0;
        int numOfSymbols = 0;
        for (int i = 0; i < field.length(); i++) { // loop through each character of field
            char token = field.charAt(i);
            int asciiToken = (int)token;
            // check for digit
            if (asciiToken >= 48 && asciiToken <= 57) { // character is a digit
                numOfDigits++;
            }
            // check for letter
            if ((asciiToken >= 65 && asciiToken <= 90) || (asciiToken >= 97 && asciiToken <= 122)) { // character is a letter
                numOfLetters++;
            }
            // check for symbol
            if ((asciiToken >= 33 && asciiToken <= 47) || (asciiToken >= 58 && asciiToken <= 64) ||
                    (asciiToken >= 91 && asciiToken <= 96) || (asciiToken >= 123 && asciiToken <= 126)) { // character is a special symbol
                numOfSymbols++;
            }
        }
        if (numOfDigits >= 1 && numOfLetters >= 3 && numOfSymbols == 0) { // field is valid
            Log.d(TAG,field + " has at least one digit");
            Log.d(TAG,field + " has at three letters");
            Log.d(TAG,field + " has no symbols");
            return true;
        }
        else { // field is not valid
            Log.d(TAG, field + " is invalid");
            return false;
        }
    }

    public boolean validAccount() {
        boolean validAccount = false;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        int usernameLen = username.length();
        int passwordLen = password.length();
        if (!username.equals("") && !password.equals("")) { // username and password are not empty
            Log.d(TAG,"username is not empty");
            Log.d(TAG,"password is not empty");
            if (usernameLen <= 8 && passwordLen <= 8) { // username and password length is less than or equal to 8
                Log.d(TAG,"username has at most 8 chars");
                Log.d(TAG,"password has at most 8 chars");
                if (validFormat(username) && validFormat(password)) { // username and password format is valid
                    validAccount = true;
                }
            }
        }
        return validAccount;
    }

    public String getDateHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = new Date();
        String currDateTime = sdf.format(date);
        return currDateTime;
    }

    public void displayAllCustomers() {
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Customer> customers = db.getCustomers("");
        String message = "=== Records in Customer ===\n";
        message += "username | dateTime\n";
        for (Customer c : customers) {
            String username = c.getUsername();
            String dateTime = c.getDateHour();
            message += username + " | " + dateTime + "\n";
        }
        Log.d(TAG, message);
    }

    public boolean canCustomerCreate() {
        boolean customerCreate = false;
        String username = usernameEditText.getText().toString();
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Customer> customers = db.getCustomers(username);
        ArrayList<Librarian> librarians = db.getLibrarians(username);
        if (customers.size() == 0 && librarians.size() == 0) { // customer can create an account
            customerCreate = true;
        }
        return customerCreate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_create);

        // initialize widget variables
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        createAccountBtn = (Button)findViewById(R.id.createAccountBtn);
        validMessageTextView = (TextView)findViewById(R.id.validMessageTextView);
        customerOkBtn = (Button)findViewById(R.id.customerOkBtn);
        createAccountBtn.setOnClickListener(this);
        customerOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createAccountBtn) {
            Log.d(TAG, "Create Account clicked");
            String message = "";
            String prompt = "Please try again.";
            if (validAccount()) {
                Log.d(TAG, "customer username and password is valid");
                if (canCustomerCreate()) {
                    Log.d(TAG, "customer username and password create");
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String dateHour = getDateHour();
                    Customer customer = new Customer("New account", username, password, dateHour);
                    // inform the customer that their account was created successfully
                    Toast.makeText(this, "Your account was created successfully", Toast.LENGTH_LONG).show(); // special toast
                    // log the customer
                    CsumbLibraryDB db = new CsumbLibraryDB(this);
                    db.insertCustomer(customer);
                    attempts = 0;
                    Log.d(TAG, "redirect to main menu");
                    // redirect to main menu activity
                    Intent main = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(main);
                }
                else {
                    Log.d(TAG, "customer username already exists");
                    // username already exists
                    message = "Username already exists";
                    validMessageTextView.setText(message + ". " + prompt);
                    attempts++;
                }
            }
            else {
                Log.d(TAG, "customer username and/or password is invalid");
                // username and/or password format is incorrect
                message = "Username and/or password format is incorrect";
                validMessageTextView.setText(message + ". " + prompt);
                attempts++;
            }
            if (attempts == 2) {
                attempts = 0;
                usernameEditText.setFocusable(false);
                passwordEditText.setFocusable(false);
                createAccountBtn.setVisibility(View.GONE);
                validMessageTextView.setText("");
                validMessageTextView.setText(message + " again. Click OK to go to the main menu.");
                customerOkBtn.setVisibility(View.VISIBLE);
            }

            displayAllCustomers(); // for testing
        }
        else if (v.getId() == R.id.customerOkBtn) { // OK btn clicked
            Log.d(TAG, "OK clicked");
            Log.d(TAG, "redirect to main menu");
            // redirect to main menu activity
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
