package com.crimsonbraixen.codingchallengesolstice.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crimsonbraixen.codingchallengesolstice.POJO.Contact;
import com.crimsonbraixen.codingchallengesolstice.R;

import java.util.List;

/**
 *  Adapter class to show the contacts list dividing between favorites and others.
 */
public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.MyViewHolder> {

    private List<Contact> contactsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView smallPhoto;
        ImageView star;
        TextView company;
        public TextView name;

        MyViewHolder(View view) {
            super(view);
            smallPhoto = view.findViewById(R.id.thumbnail);
            star = view.findViewById(R.id.star);
            name = view.findViewById(R.id.name);
            company = view.findViewById(R.id.company);
        }
    }

    public ContactsViewAdapter(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Contact contact = contactsList.get(position);
            if(contact.getSmallImage() != null) {
                holder.smallPhoto.setImageBitmap(contact.getSmallImage());
            }
            holder.name.setText(contact.getName());
            holder.company.setText((contact.getCompanyName()!=null && !contact.getCompanyName().equals("null")) ?
                    contact.getCompanyName() : "");
            if(contact.isFavorite()) {
                holder.star.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

}
