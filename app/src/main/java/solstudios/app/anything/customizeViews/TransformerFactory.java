package solstudios.app.anything.customizeViews;

import android.view.View;

/**
 * Factory created to provide Transformer implementations like ResizeTransformer o
 * ScaleTransformer.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
class TransformerFactory {

    Transformer getTransformer(final boolean resize, final View view, final View parent) {
        Transformer transformer = null;
        transformer = new ResizeTransformer(view, parent);
        if (resize) {
            //transformer = new ResizeTransformer(view, parent, bottomView);
        } else {
            // transformer = new ScaleTransformer(view, parent, bottomView);
        }
        return transformer;
    }
}