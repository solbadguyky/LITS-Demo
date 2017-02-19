package solstudios.app.moduls.mapviews.MapViewgroup;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dựa vào mã nguồn {}@link:Adapter} của Android
 * Created by SolbadguyKY on 16-Feb-17.
 */

interface MyAdapter {
    int IGNORE_ITEM_VIEW_TYPE = -1;
    int NO_SELECTION = -2147483648;

    void registerDataSetObserver(DataSetObserver dataSetObserver);

    void unregisterDataSetObserver(DataSetObserver dataSetObserver);

    int getCount();

    Object getItem(int index);

    long getItemId(int id);

    boolean hasStableIds();

    View getView(int var1, View var2, ViewGroup var3);

    int getItemViewType(int var1);

    int getViewTypeCount();

    boolean isEmpty();
}
