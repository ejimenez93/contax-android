package com.edisonjimenez.contax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class MainActivity extends BaseActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    TextView mEmptyText;
    FirebaseRecyclerAdapter<Person, PersonViewHolder> mAdapter;
    public static Firebase mFirebaseRef;
    Intent contactIntent;
    ValueEventListener mValueEventListener;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        mContext = this;

        // Set the initial context for Firebase
        Firebase.setAndroidContext(mContext);

        // Set up initial Firebase reference
        mFirebaseRef = new Firebase("https://contax.firebaseio.com/Persons");

        // Setup the recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyText = (TextView) findViewById(R.id.noDataText);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        // Generate list items from Firebase and alphabetically order by Contact (Person) last name
        mAdapter = new FirebaseRecyclerAdapter<Person, PersonViewHolder>(Person.class, R.layout.person_list_item, PersonViewHolder.class, mFirebaseRef.orderByChild("lastName")) {
            @Override
            protected void populateViewHolder(PersonViewHolder personViewHolder, Person person, int i) {
                personViewHolder.fullNameText.setText(person.getFullName());
            }
        };

        // Set the adapter for the RecyclerView
        mRecyclerView.setAdapter(mAdapter);


        // Observe for changes in list. Show 'No Data' text if appropriate
        mFirebaseRef.addValueEventListener(mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showHideNoDataText(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
            }
        });

        // Set onClickListener
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                contactIntent = new Intent(mContext, ContactActivity.class);
                Person contactLoaded = mAdapter.getItem(position);

                // Get the Contact (Person) info and place into intent for ContactActivity to load
                contactIntent.putExtra("contactLoaded", true);
                contactIntent.putExtra("id", contactLoaded.getId());
                contactIntent.putExtra("firstName", contactLoaded.getFirstName());
                contactIntent.putExtra("lastName", contactLoaded.getLastName());
                contactIntent.putExtra("fullName", contactLoaded.getFullName());
                contactIntent.putExtra("dateOfBirth", contactLoaded.getDateOfBirth());
                contactIntent.putExtra("zipcode", contactLoaded.getZipcode());

                startActivity(contactIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


    }

    @Override
    protected void onDestroy() {
        // Remove event listeners from the Firebase reference
        mFirebaseRef.removeEventListener(mValueEventListener);

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // Load up the ContactActivity with no additional info because we are ADDING an entry
                contactIntent = new Intent(mContext, ContactActivity.class);
                startActivity(contactIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function used to show/hide the "No Data" text on RecyclerView
     * @param snapshot DataSnapshot of current Firebase instance
     */
    private void showHideNoDataText(DataSnapshot snapshot) {
        if (!snapshot.exists()) {
            mEmptyText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
        }
    }
}
