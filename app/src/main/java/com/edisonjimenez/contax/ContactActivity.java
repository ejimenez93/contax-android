package com.edisonjimenez.contax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ContactActivity extends BaseActivity {

    private final static String TAG = ContactActivity.class.getSimpleName();

    private Intent contactLoadedIntent;
    private Person contactLoaded;
    private Firebase mFirebaseRef;

    private TextView mContactLabel;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mDateOfBirthField;
    private EditText mZipcodeField;
    private Button mDeleteButton;
    private Context mContext;

    private boolean isNewContact = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mContext = this;


        // Show ActionBar with clickable arrow since this activity is a child of MainActivity
        activateToolbarWithHomeEnabled();

        mContactLabel = (TextView) findViewById(R.id.contactLabel);
        mFirstNameField = (EditText) findViewById(R.id.firstNameField);
        mLastNameField = (EditText) findViewById(R.id.lastNameField);
        mDateOfBirthField = (EditText) findViewById(R.id.dateOfBirthField);
        mZipcodeField = (EditText) findViewById(R.id.zipcodeField);
        mDeleteButton = (Button) findViewById(R.id.deleteButton);

        contactLoadedIntent = getIntent();

        contactLoaded = new Person();

        // Editing an EXISTING contact
        if (contactLoadedIntent.getBooleanExtra("contactLoaded", false)) {
            getSupportActionBar().setTitle(R.string.editContact);

            mFirebaseRef = MainActivity.mFirebaseRef.child(contactLoadedIntent.getStringExtra("id"));
            isNewContact = false;

            // Set our fields to the data from MainActivity
            mContactLabel.setText(contactLoadedIntent.getStringExtra("fullName"));
            mFirstNameField.setText(contactLoadedIntent.getStringExtra("firstName"));
            mLastNameField.setText(contactLoadedIntent.getStringExtra("lastName"));
            mDateOfBirthField.setText(contactLoadedIntent.getStringExtra("dateOfBirth"));
            mZipcodeField.setText(contactLoadedIntent.getStringExtra("zipcode"));

        }
        // Entering a NEW contact
        else {
            mDeleteButton.setEnabled(false);
            mDeleteButton.setTextColor(getResources().getColor(R.color.contaxAlertButtonColorDisabled));
            getSupportActionBar().setTitle(R.string.newContact);
            mFirebaseRef = MainActivity.mFirebaseRef.push();
        }

        // Set a click listener for the delete button to show an AlertDialog to the user
        // If the user confirms, delete the record from Firebase and close this activity to return back to MainActivity
        mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
                alertBuilder.setTitle(R.string.deleteContact)
                        .setMessage(R.string.deleteContactConfirmation)
                        .setCancelable(false)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFirebaseRef.removeValue(new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError == null) {
                                            finish();
                                        }
                                        else {
                                            Log.e(TAG, firebaseError.getMessage());
                                        }
                                    }
                                });
                            }
                        })
                        .create();

                alertBuilder.show();
            }
        });

        mDateOfBirthField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment(mDateOfBirthField, isNewContact);
                fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:


                // Perform some validation on the form
                boolean validForm = false;

                // Check if First Name + Last Name fields are filled in
                if (mFirstNameField.getText().length() == 0) {
                    mFirstNameField.setError("First name field is required!");
                }
                else if (mLastNameField.getText().length() == 0) {
                    mLastNameField.setError("Last name field is required!");
                }
                else {
                    validForm = true;
                }

                // If we have a valid form, update the loaded contact or generate a new one
                // based on the information provided. Use the ID as the key
                if (validForm) {

                    contactLoaded.setId(contactLoadedIntent.getStringExtra("id"));
                    contactLoaded.setFirstName(mFirstNameField.getText().toString());
                    contactLoaded.setLastName(mLastNameField.getText().toString());
                    contactLoaded.setFullName(mFirstNameField.getText().toString() + " " + mLastNameField.getText().toString());
                    contactLoaded.setDateOfBirth(mDateOfBirthField.getText().toString());
                    contactLoaded.setZipcode(mZipcodeField.getText().toString());

                    // Existing Contact -- UPDATE
                    if (!isNewContact) {

                        mFirebaseRef.setValue(contactLoaded, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    finish();
                                }
                                else {
                                    Log.e(TAG, firebaseError.getMessage());
                                }
                            }
                        });
                    }

                    // New Contact -- CREATE
                    else {
                        contactLoaded.setId(mFirebaseRef.getKey());
                        mFirebaseRef.setValue(contactLoaded, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    finish();
                                }
                                else {
                                    Log.e(TAG, firebaseError.getMessage());
                                }
                            }
                        });
                    }



                }



                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
