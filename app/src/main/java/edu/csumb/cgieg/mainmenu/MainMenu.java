package edu.csumb.cgieg.mainmenu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class MainMenu extends Activity implements OnClickListener {
    private static final String TAG = "MainMenu";
    // declare widget button variables
    private Button createAccountBtn;
    private Button placeHoldBtn;
    private Button cancelHoldBtn;
    private Button manageSystemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // initialize widget button variables
        createAccountBtn = (Button)findViewById(R.id.createAccountBtn);
        placeHoldBtn = (Button)findViewById(R.id.placeHoldBtn);
        cancelHoldBtn = (Button)findViewById(R.id.cancelHoldBtn);
        manageSystemBtn = (Button)findViewById(R.id.manageSystemBtn);

        // set on click listeners for widget button variables
        createAccountBtn.setOnClickListener(this);
        placeHoldBtn.setOnClickListener(this);
        cancelHoldBtn.setOnClickListener(this);
        manageSystemBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountBtn:
                // redirect to customer create activity
                Log.d(TAG, "Create Account clicked");
                Intent customerCreate = new Intent(getApplicationContext(), CustomerCreate.class);
                startActivity(customerCreate);
                break;
            case R.id.placeHoldBtn:
                // redirect to customer place hold activity
                Log.d(TAG, "Place Hold clicked");
                Intent customerPlaceHold = new Intent(getApplicationContext(), CustomerPlaceHold.class);
                startActivity(customerPlaceHold);
                break;
            case R.id.cancelHoldBtn:
                // redirect to customer login activity
                Log.d(TAG, "Cancel Hold clicked");
                Intent customerLogin = new Intent(getApplicationContext(), CustomerLogin.class);
                startActivity(customerLogin);
                break;
            case R.id.manageSystemBtn:
                // redirect to librarian login activity
                Log.d(TAG, "Manage System clicked");
                Intent librarianLogin = new Intent(getApplicationContext(), LibrarianLogin.class);
                startActivity(librarianLogin);
                break;
        }
    }
}
