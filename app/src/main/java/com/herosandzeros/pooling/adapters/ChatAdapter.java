package com.herosandzeros.pooling.adapters;

/**
 * Created by mathan on 6/9/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herosandzeros.pooling.R;
import com.herosandzeros.pooling.db.MessageDB;
import com.herosandzeros.pooling.utils.MySharedPreference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathan on 6/9/15.
 */

/**
 * Created by mathan on 11/8/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    private List<MessageDB> mMessageDBs;
    private MySharedPreference mPrefs;

    public ChatAdapter(Context context) {
        mContext = context;
        mPrefs = new MySharedPreference(mContext);
    }

    public void updateMessages(List<MessageDB> messageDBs) {
        mMessageDBs = messageDBs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_sender, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMessage.setText(mMessageDBs.get(position).getMessage());
        if (mPrefs.getUserId().equalsIgnoreCase(mMessageDBs.get(position).getSenderId())) {
            holder.mMessage.setGravity(Gravity.RIGHT);
            holder.mMessage.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
            holder.mMessage.setTextColor(mContext.getResources().getColor(R.color.grey));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mMessage.getLayoutParams();
            params.setMargins(dpToPx(30), 0, dpToPx(10), 0);
            holder.mMessage.setLayoutParams(params);
        } else {
            holder.mMessage.setGravity(Gravity.LEFT);
            holder.mMessage.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.mMessage.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mMessage.getLayoutParams();
            params.setMargins(dpToPx(10), 0, dpToPx(30), 0);
            holder.mMessage.setLayoutParams(params);
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public int getItemCount() {

        return mMessageDBs == null ? 0 : mMessageDBs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message)
        TextView mMessage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
