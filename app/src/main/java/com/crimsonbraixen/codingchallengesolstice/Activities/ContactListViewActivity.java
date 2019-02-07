package com.crimsonbraixen.codingchallengesolstice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.crimsonbraixen.codingchallengesolstice.Adapters.ContactsViewAdapter;
import com.crimsonbraixen.codingchallengesolstice.Contacts;
import com.crimsonbraixen.codingchallengesolstice.POJO.Contact;
import com.crimsonbraixen.codingchallengesolstice.R;
import com.crimsonbraixen.codingchallengesolstice.RecyclerTouchListener;
import com.crimsonbraixen.codingchallengesolstice.Utils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity that shows the list of contacts and allows accessing one for more information.
 */
public class ContactListViewActivity extends AppCompatActivity {

    public ArrayList<Contact> favoriteContacts;
    public ArrayList<Contact> otherContacts;

    @BindView(R.id.viewFavorites)
    protected RecyclerView favoritesRecyclerView;
    @BindView(R.id.viewOthers)
    protected RecyclerView othersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_view);

        ButterKnife.bind(this);

        favoriteContacts = new ArrayList<>();
        otherContacts = new ArrayList<>();

        // Obtain contacts from application
        ArrayList<Contact> contacts = ((Contacts) this.getApplication()).getContacts();

        if(contacts!=null){
            // Sort contacts alphabetically
            Collections.sort(contacts);
            // Separate them into two arrays
            for (Contact contact : contacts) {
                if(contact.isFavorite()){
                    favoriteContacts.add(contact);
                } else {
                    otherContacts.add(contact);
                }
            }

            // Set favorites' view
            setAdapterIntoRecyclerView(favoritesRecyclerView, favoriteContacts);
            // Set other contacts' view
            setAdapterIntoRecyclerView(othersRecyclerView, otherContacts);
        } else {
            Log.d(Utils.TAG, "No contacts.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        favoritesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), favoritesRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openDetailViewOfSelectedContact(true, position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        othersRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), othersRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openDetailViewOfSelectedContact(false, position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * Method that starts the new activity to view details of a certain contact.
     * @param isFavorite: 'true' if its a favorite contact or 'false' if not.
     * @param position: position of the contact selected in the corresponding array.
     */
    private void openDetailViewOfSelectedContact(boolean isFavorite, int position) {
        Contact contact;
        if(isFavorite){
            contact = favoriteContacts.get(position);
        } else {
            contact = otherContacts.get(position);
        }
        Intent intent = new Intent(getApplicationContext(), ContactDetailViewActivity.class);
        intent.putExtra(Utils.CONTACT, contact.getId());
        startActivity(intent);
    }

    /**
     * Method that set the adapter into the recycler view.
     * @param view: which recycler view to render.
     * @param contactList: to render into the view.
     */
    private void setAdapterIntoRecyclerView(RecyclerView view, ArrayList<Contact> contactList) {
        ContactsViewAdapter adapter= new ContactsViewAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        view.setLayoutManager(mLayoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }
}
