package com.crimsonbraixen.codingchallengesolstice.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crimsonbraixen.codingchallengesolstice.R;
import com.crimsonbraixen.codingchallengesolstice.Utils;

import static com.crimsonbraixen.codingchallengesolstice.Utils.TAG;

/**
 *  Adapter class to render into the view the phone details.
 */
public class ContactDetailsAdapter extends ArrayAdapter<String> {

    public ContactDetailsAdapter(Context context, String[] data) {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        String data = getItem(position);

        if(data!=null && !data.equals("")){

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_data_row, parent, false);
            }

            TextView phoneType = convertView.findViewById(R.id.phoneType);
            TextView key = convertView.findViewById(R.id.key);
            TextView value = convertView.findViewById(R.id.value);

            value.setText(data);
            // Populate the data into the template view using the data object
            switch (position){
                case 0 :
                    phoneType.setText(Utils.WORK);
                    break;
                case 1:
                    phoneType.setText(Utils.HOME);
                    break;
                case 2:
                    phoneType.setText(Utils.MOBILE);
                    break;
                case 3:
                    key.setText(Utils.ADDRESS);
                    break;
                case 4:
                    key.setText(Utils.BIRTHDATE);
                    break;
                case 5:
                    key.setText(Utils.EMAIL);
                    break;
                default:
                    Log.d(TAG, "Non existent position");
            }
        } else {
            //if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dummy_view, parent, false);
            //}
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

