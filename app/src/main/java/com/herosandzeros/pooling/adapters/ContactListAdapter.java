package com.herosandzeros.pooling.adapters;

/**
 * Created by mathan on 6/9/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herosandzeros.pooling.R;
import com.herosandzeros.pooling.db.UsersListDB;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathan on 11/8/15.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private Context mContext;
    private List<UsersListDB> mUsersListDBs;

    public ContactListAdapter(Context context) {
        mContext = context;
    }

    public void updateUsersList(List<UsersListDB> usersListDBs) {
        mUsersListDBs = usersListDBs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mUserName.setText(mUsersListDBs.get(position).getName());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        return mUsersListDBs == null ? 0 : mUsersListDBs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_name)
        TextView mUserName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

}
