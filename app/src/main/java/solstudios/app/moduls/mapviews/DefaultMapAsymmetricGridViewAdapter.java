package solstudios.app.moduls.mapviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import solstudios.app.R;

public class DefaultMapAsymmetricGridViewAdapter extends ArrayAdapter<MapImageAsymmetricGridViewItem> implements MapAsymmetricGridViewInterface {

    private final LayoutInflater layoutInflater;

    DefaultMapAsymmetricGridViewAdapter(Context context, List<MapImageAsymmetricGridViewItem> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
    }

    public DefaultMapAsymmetricGridViewAdapter(Context context) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FrameLayout v;
        MapImageAsymmetricGridViewItem item = getItem(position);
        boolean isRegular = getItemViewType(position) == 0;

        if (convertView == null) {
            v = new FrameLayout(getContext());
            v.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            v.setLeft(25);
            v.setRight(25);
            v.setBottom(25);
            v.setTag(25);
            v.setBackgroundColor(getContext().getResources().getColor(R.color.material_light_blue_500));

            TextView textView = new TextView(getContext());
            textView.setTag("textView");

            v.addView(textView);

        } else {
            v = (FrameLayout) convertView;
        }

        ((TextView) v.findViewWithTag("textView")).setText(item.toString());
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }

    public void appendItems(List<MapImageAsymmetricGridViewItem> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<MapImageAsymmetricGridViewItem> moreItems) {
        clear();
        appendItems(moreItems);
    }
}