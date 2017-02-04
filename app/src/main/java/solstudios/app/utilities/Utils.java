package solstudios.app.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by SolbadguyKY on 03-Feb-17.
 */

public class Utils {
    public static float convertPxToDp(Context context, float px) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
    }
}
