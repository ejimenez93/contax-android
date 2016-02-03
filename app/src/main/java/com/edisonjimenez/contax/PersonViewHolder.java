package com.edisonjimenez.contax;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PersonViewHolder extends RecyclerView.ViewHolder {

    TextView fullNameText;

    public PersonViewHolder(View itemView) {
        super(itemView);
        fullNameText = (TextView) itemView.findViewById(R.id.personListFullName);
    }
}
