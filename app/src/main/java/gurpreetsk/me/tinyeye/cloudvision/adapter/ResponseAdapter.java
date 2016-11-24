package gurpreetsk.me.tinyeye.cloudvision.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.activities.DataActivity;
import gurpreetsk.me.tinyeye.cloudvision.activities.DetailActivity;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;

/**
 * Created by daman on 24/10/16.
 */

public class ResponseAdapter extends BaseAdapter{
    private ArrayList<String> responselist;
    private ArrayList<String> location;
    private Context context;

    public ResponseAdapter(Context context, ArrayList<String> responselist, ArrayList<String> location) {
        this.context = context;
        this.responselist = responselist;
        this.location = location;
    }

    static class CardViewHolder {
        TextView title;
        CardView responsecard;
    }

    @Override
    public int getCount() {
        return responselist.size();
    }

    @Override
    public Object getItem(int location) {
        return responselist.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.response_cardview, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.responsecard = (CardView) row.findViewById(R.id.response_card);
            viewHolder.title = (TextView) row.findViewById(R.id.text_card);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        viewHolder.title.setText(responselist.get(position));
        viewHolder.responsecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constants.DATA_ACTIVITY_INTENT_KEY, responselist.get(position));
                intent.putStringArrayListExtra(Constants.MAP_FRAGMENT_INTENT_KEY, location);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return row;
    }

}
