package solstudios.app.moduls.mapviews.MapData;

public class MapItem extends MapAbsItem {

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title != null ? this.title : "null_title";
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description != null ? this.description : "null_description";
    }

}
