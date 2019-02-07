package com.crimsonbraixen.codingchallengesolstice;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.crimsonbraixen.codingchallengesolstice.Activities.ContactDetailViewActivity;
import com.crimsonbraixen.codingchallengesolstice.Activities.ContactListViewActivity;
import com.crimsonbraixen.codingchallengesolstice.POJO.Address;
import com.crimsonbraixen.codingchallengesolstice.POJO.Contact;
import com.crimsonbraixen.codingchallengesolstice.POJO.Phone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class used for handling some system constants and static methods
 */
public class Utils {

    // --------------------------------  CONSTANTS --------------------------------------------

    /**
     * Contact Constants
     */
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COMPANY_NAME = "companyName";
    public static final String COLUMN_IS_FAVORITE = "isFavorite";
    public static final String COLUMN_SMALL_IMAGE_URL = "smallImageURL";
    public static final String COLUMN_LARGE_IMAGE_URL = "largeImageURL";
    public static final String COLUMN_EMAIL_ADDRESS = "emailAddress";
    public static final String COLUMN_BIRTHDATE = "birthdate";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";

    /**
     * Phone Constants
     */
    public static final String COLUMN_WORK = "work";
    public static final String COLUMN_HOME = "home";
    public static final String COLUMN_MOBILE = "mobile";

    /**
     * Address Constants
     */
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_ZIPCODE = "zipCode";

    /**
     General constants
     */
    public static final String URL = "https://s3.amazonaws.com/technical-challenge/v3/contacts.json";
    public static final String TAG = "SOLSTICE";
    public static final String CONTACTS_TITLE = "Contacts";
    public static final String CONTACTS = "contacts";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "ADDRESS";
    public static final String BIRTHDATE = "BIRTHDATE";
    public static final String EMAIL = "EMAIL";
    public static final String WORK = "WORK";
    public static final String HOME = "HOME";
    public static final String MOBILE = "MOBILE";
    public static final String IMAGES = "IMAGES";
    public static final String MAIN_PAGE = "Main Page";
    public static final String WHATEVER = "Whatever";


    // --------------------------------  METHODS --------------------------------------------


    /**
     * AsyncTask class to fetch web data.
     */
    public static class AsyncFetch extends AsyncTask<String, String, String> {

        Context context;
        String whatToFetch;
        Bitmap image;
        ArrayList<Contact> contacts;
        String url;
        ImageView largeImage;

        public AsyncFetch(Context context, String whatToFetch, String url, ImageView largeImage){
            this.context = context;
            this.whatToFetch = whatToFetch;
            this.url = url;
            this.contacts = new ArrayList<>();
            this.largeImage = largeImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String goTo = Utils.WHATEVER;
            Object response = getData(context, whatToFetch, url);
            if(whatToFetch.equals(Utils.CONTACTS)){
                contacts = (ArrayList<Contact>) response;
                goTo = Utils.MAIN_PAGE;
            } else {
                image = (Bitmap) response;
            }
            return goTo;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals(Utils.MAIN_PAGE)){
                Intent intent = new Intent(context, ContactListViewActivity.class);
                context.startActivity(intent);
            } else {
                largeImage.setImageBitmap(image);
            }
        }
    }

    /**
     * Method that fetches the data from the web server.
     * @param whatToFetch, can be 'contacts' if the fetch is of all the contacts or
     *                   it can be 'image' if the fetch is of an image (small or large) of a contact.
     * @param url, the url from where the data will be fetched.
     */
    private static Object getData(Context context, String whatToFetch, String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response responses;

        try {
            responses = client.newCall(request).execute();
            if(responses!=null) {
                if (responses.isSuccessful()) {
                    if (responses.body() != null) {
                        if(whatToFetch.equals(Utils.IMAGES)){
                            InputStream inputStream = responses.body().byteStream();
                            return BitmapFactory.decodeStream(inputStream);
                        } else {
                            String jsonData = responses.body().string();
                            JSONArray jArray = new JSONArray(jsonData);
                            return manageContactsResponse(context, jArray);
                        }
                    }
                }
            } else {
                Log.d(Utils.TAG, "NOT Successful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that does all the parsing into contact objects from the json response.
     * @param context, context from activity caller
     * @param jsonContacts: data fetched from the web page.
     */
    private static Object manageContactsResponse(Context context, JSONArray jsonContacts) {
        try {
            ArrayList<Contact> contacts = ((Contacts) context.getApplicationContext()).getContacts();
            for (int i = 0; i < jsonContacts.length(); i++) {
                JSONObject objectContact = jsonContacts.getJSONObject(i);
                Contact contact = new Contact();
                contact.setId(objectContact.getInt(Utils.COLUMN_ID));
                contact.setName(objectContact.getString(Utils.COLUMN_NAME));
                if(objectContact.has(Utils.COLUMN_COMPANY_NAME)
                        && !objectContact.getString(Utils.COLUMN_COMPANY_NAME).equals("null")
                        && !objectContact.getString(Utils.COLUMN_COMPANY_NAME).equals("")){
                    contact.setCompanyName(objectContact.getString(Utils.COLUMN_COMPANY_NAME));
                }
                contact.setFavorite(objectContact.getBoolean(Utils.COLUMN_IS_FAVORITE));
                contact.setSmallImageURL(objectContact.getString(Utils.COLUMN_SMALL_IMAGE_URL));
                contact.setLargeImageURL(objectContact.getString(Utils.COLUMN_LARGE_IMAGE_URL));

                contact.setSmallImage((Bitmap) getData(context, Utils.IMAGES, contact.getSmallImageURL()));

                contact.setEmailAddress(objectContact.getString(Utils.COLUMN_EMAIL_ADDRESS));
                contact.setBirthdate(objectContact.getString(Utils.COLUMN_BIRTHDATE));

                // Phone numbers
                JSONObject jsonPhones = objectContact.getJSONObject(Utils.COLUMN_PHONE);
                Phone phones = new Phone();
                phones.setWork(jsonPhones.has(Utils.COLUMN_WORK) ? jsonPhones.getString(Utils.COLUMN_WORK) : "");
                phones.setHome(jsonPhones.has(Utils.COLUMN_HOME) ? jsonPhones.getString(Utils.COLUMN_HOME) : "");
                phones.setMobile(jsonPhones.has(Utils.COLUMN_MOBILE) ? jsonPhones.getString(Utils.COLUMN_MOBILE) : "");
                contact.setPhones(phones);

                //Address
                Address address = new Address();
                JSONObject jsonAddress = objectContact.getJSONObject(Utils.COLUMN_ADDRESS);
                address.setCity(jsonAddress.getString(Utils.COLUMN_CITY));
                address.setCountry(jsonAddress.getString(Utils.COLUMN_COUNTRY));
                address.setState(jsonAddress.getString(Utils.COLUMN_STATE));
                address.setStreet(jsonAddress.getString(Utils.COLUMN_STREET));
                address.setZipCode(jsonAddress.getString(Utils.COLUMN_ZIPCODE));
                contact.setAddress(address);

                contacts.add(contact);
            }
            ((Contacts) context.getApplicationContext()).setContacts(contacts);
            return contacts;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Obtains the contact by the id.
     * @param contacts: the contact list in which the search will be done.
     * @param id: to be searched in the contact list.
     */
    public static Contact getById(ArrayList<Contact> contacts, int id){
        for (Contact contact :
                contacts) {
            if(contact.getId()==id){
                return contact;
            }
        }
        return null;
    }
}
