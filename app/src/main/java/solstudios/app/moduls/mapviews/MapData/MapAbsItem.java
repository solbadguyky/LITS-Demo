package solstudios.app.moduls.mapviews.MapData;

import java.io.Serializable;

public abstract class MapAbsItem implements Serializable {
    String title, description;

    public abstract void setTitle(String title);

    public abstract String getTitle();

    public abstract void setDescription(String description);

    public abstract String getDescription();
}
