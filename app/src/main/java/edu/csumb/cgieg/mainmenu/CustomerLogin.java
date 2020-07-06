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

public class CustomerLogin extends Activity implements OnClickListener {
    private static final String TAG = "CustomerLogin";
    // declare widget variables
    private EditText customerUsernameEditText;
    private EditText customerPasswordEditText;
    private Button customerLoginBtn;
    private TextView customerLoginMessageTextView;
    private Button customerLoginOkBtn;

    // declare and initialize book rental confirmation variables
    private String username = "";
    private String pickupDateHour = "";
    private String returnDateHour = "";
    private String bookTitle = "";
    private double bookFeePerHour = 0.0;

    // declare and initialize attempts variable
    private static int attempts = 0;

    public boolean validLogin() {
        boolean validLogin = false;
        String username = customerUsernameEditText.getText().toString();
        String password = customerPasswordEditText.getText().toString();
        CsumbLibraryDB db = new CsumbLibraryDB(this);
        ArrayList<Customer> customers = db.checkCustomers(username, password);
        if (customers.size() == 1) { // valid customer
            validLogin = true;
            attempts = 0;
        }
        return validLogin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        // initialize widget variables
        customerUsernameEditText = (EditText)findViewById(R.id.customerUsernameEditText);
        customerPasswordEditText = (EditText)findViewById(R.id.customerPasswordEditText);
        customerLoginBtn = (Button)findViewById(R.id.customerLoginBtn);
        customerLoginBtn.setOnClickListener(this);
        customerLoginMessageTextView = (TextView)findViewById(R.id.customerLoginMessageTextView);
        customerLoginOkBtn = (Button)findViewById(R.id.customerLoginOkBtn);
        customerLoginOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.customerLoginBtn) {
            Log.d(TAG, "Login clicked");
            String message = "";
            String prompt = "Please try again.";
            if (validLogin()) { // customer login is valid
                Log.d(TAG, "customer is valid");
                Toast.makeText(this, "You are now logged in", Toast.LENGTH_LONG).show(); // special toast
                customerLoginMessageTextView.setText("");
                Bundle extras = getIntent().getExtras();
                username = customerUsernameEditText.getText().toString(); // do this before extras != null
                if (extras != null) { // get confirmation variables from CustomerPlaceHold
                    pickupDateHour = extras.getString("sqlPickupDateHour");
                    returnDateHour = extras.getString("sqlReturnDateHour");
                    bookTitle = extras.getString("bookTitle");
                    bookFeePerHour = extras.getDouble("bookFeePerHour");
                }
                Bundle extraInfo = new Bundle();
                extraInfo.putString("username", username);
                extraInfo.putString("sqlPickupDateHour", pickupDateHour);
                extraInfo.putString("sqlReturnDateHour", returnDateHour);
                extraInfo.putString("bookTitle", bookTitle);
                extraInfo.putDouble("bookFeePerHour", bookFeePerHour);
                if (pickupDateHour.equals("")) { // customer is trying to cancel holds
                    Log.d(TAG, "redirect to customer cancel hold");
                    // redirect to customer cancel hold activity
                    Intent customerCancelHold = new Intent(getApplicationContext(), CustomerCancelHold.class);
                    customerCancelHold.putExtras(extraInfo);
                    startActivity(customerCancelHold);
                }
                else {
                    Log.d(TAG, "redirect to customer confirmation");
                    // redirect to customer confirmation activity
                    Intent customerConfirm = new Intent(getApplicationContext(), CustomerConfirmation.class);
                    customerConfirm.putExtras(extraInfo); // pass confirmation variables to customerConfirm
                    startActivity(customerConfirm);
                }
            }
            else {
                Log.d(TAG, "customer username and/or password is invalid");
                message = "Username and/or password is invalid";
                customerLoginMessageTextView.setText(message + ". " + prompt);
                attempts++;
            }
            if (attempts == 2) {
                attempts = 0;
                customerUsernameEditText.setFocusable(false);
                customerPasswordEditText.setFocusable(false);
                customerLoginBtn.setVisibility(View.GONE);
                customerLoginMessageTextView.setText("");
                customerLoginMessageTextView.setText(message + " again. Click OK to go to the main menu.");
                customerLoginOkBtn.setVisibility(View.VISIBLE);
            }
        }
        else if (v.getId() == R.id.customerLoginOkBtn) { // OK clicked
            Log.d(TAG, "OK clicked");
            Log.d(TAG, "redirect to main menu");
            Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(mainMenu);
        }
    }
}
