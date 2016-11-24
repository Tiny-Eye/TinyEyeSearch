package gurpreetsk.me.tinyeye.cloudvision.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gurpreetsk.me.tinyeye.R;

/**
 * Created by daman on 14/11/16.
 */

public class LinksAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> key;
    Context context;

    public LinksAdapter(Context context, ArrayList<String> key) {
        this.context = context;
        this.key = key;
    }

    static class CardViewHolder {
        TextView title;
    }

    @Override
    public int getCount() {
        return key.size();
    }

    @Override
    public Object getItem(int location) {
        return key.get(location);
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
            row = inflater.inflate(R.layout.links_layout, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.title = (TextView) row.findViewById(R.id.links_text);

            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }


        final String url = key.get(position);

        viewHolder.title.setText(url);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        return row;
    }

}
