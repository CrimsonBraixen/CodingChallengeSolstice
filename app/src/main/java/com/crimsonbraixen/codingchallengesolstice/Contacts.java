package com.crimsonbraixen.codingchallengesolstice;

import android.app.Application;

import com.crimsonbraixen.codingchallengesolstice.POJO.Contact;

import java.util.ArrayList;

/**
 * Class used to be able to access the data in the whole application
 * @author MartinArtime
 */
public class Contacts extends Application {

    public ArrayList<Contact> contacts;

    public Contacts(){
        contacts = new ArrayList<>();
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

}
