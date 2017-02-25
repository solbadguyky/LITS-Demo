package solstudios.app.moduls.mapviews.MapData;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * Created by SolbadguyKY on 26-Feb-17.
 */

abstract class AbsMapItemAdapter {

    HashMap<MarkerOptions, MapItem> itemList;

    public abstract void add(MapAbsItem absItem);

}
