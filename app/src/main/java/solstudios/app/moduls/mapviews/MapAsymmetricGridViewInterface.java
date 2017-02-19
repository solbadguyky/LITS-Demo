package solstudios.app.moduls.mapviews;

import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by SolbadguyKY on 14-Feb-17.
 */

public interface MapAsymmetricGridViewInterface extends ListAdapter {

    void appendItems(List<MapImageAsymmetricGridViewItem> newItems);

    void setItems(List<MapImageAsymmetricGridViewItem> moreItems);
}