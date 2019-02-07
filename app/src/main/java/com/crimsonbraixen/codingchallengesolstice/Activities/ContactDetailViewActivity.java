package com.crimsonbraixen.codingchallengesolstice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.crimsonbraixen.codingchallengesolstice.Adapters.ContactDetailsAdapter;
import com.crimsonbraixen.codingchallengesolstice.Contacts;
import com.crimsonbraixen.codingchallengesolstice.MyListView;
import com.crimsonbraixen.codingchallengesolstice.POJO.Contact;
import com.crimsonbraixen.codingchallengesolstice.POJO.Phone;
import com.crimsonbraixen.codingchallengesolstice.R;
import com.crimsonbraixen.codingchallengesolstice.Utils;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to show a contact with its details.
 */
public class ContactDetailViewActivity extends AppCompatActivity {

    public ArrayList<Contact> contacts;
    public Contact contact;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.starButton)
    protected ImageButton starButton;
    @BindView(R.id.contactImage)
    protected ImageView contactImage;
    @BindView(R.id.contactName)
    protected TextView contactName;
    @BindView(R.id.contactCompany)
    protected TextView contactCompany;
    @BindView(R.id.view)
    protected MyListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        ButterKnife.bind(this);

        toolbar.setTitle(Utils.CONTACTS_TITLE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int contactId = Objects.requireNonNull(getIntent().getExtras()).getInt(Utils.CONTACT);
        contacts = ((Contacts) this.getApplication()).getContacts();
        contact = Utils.getById(contacts, contactId);

        new Utils.AsyncFetch(getApplicationContext(), Utils.IMAGES, contact.getLargeImageURL(), contactImage).execute();

        contacts.set(contacts.indexOf(contact), contact);

        ((Contacts) this.getApplication()).setContacts(contacts);

        setContactInformation();
    }

    @Override
    public void onStart() {
        super.onStart();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactListViewActivity.class));
                finish();
            }
        });

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContactFavoriteStatus(contact);
                Intent intent = new Intent(getApplicationContext(), ContactListViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Method that fills the view with the contact information.
     */
    private void setContactInformation() {
        if (contact.isFavorite()) {
            starButton.setImageResource(R.mipmap.star_true);
        }
        contactName.setText(contact.getName());
        if (contact.getCompanyName() != null) {
            contactCompany.setText(contact.getCompanyName());
        }
        setAdapterIntoListView(view);
    }

    /**
     * Method that sets the adapter into the recycler view.
     * @param view: listview into which the information will be rendered.
     */
    private void setAdapterIntoListView(MyListView view) {
            Phone phone = contact.getPhones();
            String[] data = new String[6];
            if(phone.getWork()!=null){
                data[0] = phone.getWork();
            }
            if(phone.getHome()!=null){
                data[1] = phone.getHome();
            }
            if(phone.getMobile()!=null){
                data[2] = phone.getMobile();
            }
            if(contact.getAddress()!=null){
                data[3] = contact.getAddress().toString();
            }
            if(contact.getBirthdate()!=null){
                data[4] = contact.getBirthdate();
            }
            if(contact.getEmailAddress()!=null){
                data[5] = contact.getEmailAddress();
            }
            view.setAdapter(new ContactDetailsAdapter(this, data));
    }

    /**
     * Updates the contact, changing the favorite status of it.
     * @param contact: which its status will change.
     */
    private void changeContactFavoriteStatus(Contact contact) {
        if(contact.isFavorite()){
            contact.setFavorite(false);
        } else {
            contact.setFavorite(true);
        }

        for (Contact contactFromList : contacts) {
            if(contact.compareTo(contactFromList)==0){
                int index = contacts.indexOf(contactFromList);
                contacts.set(index, contact);
            }
        }

    }

}
