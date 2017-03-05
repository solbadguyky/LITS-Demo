package solstudios.app.moduls.mapviews;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by SolbadguyKY on 14-Feb-17.
 */

public class MapImageAsymmetricGridViewItem implements AsymmetricItem {
    /* Parcelable interface implementation */
    public static final Parcelable.Creator<MapImageAsymmetricGridViewItem> CREATOR = new Parcelable.Creator<MapImageAsymmetricGridViewItem>() {
        @Override
        public MapImageAsymmetricGridViewItem createFromParcel(@NonNull Parcel in) {
            return new MapImageAsymmetricGridViewItem(in);
        }

        @Override
        @NonNull
        public MapImageAsymmetricGridViewItem[] newArray(int size) {
            return new MapImageAsymmetricGridViewItem[size];
        }
    };
    private int columnSpan;
    private int rowSpan;
    private int position;
    private String imageUrl;

    public MapImageAsymmetricGridViewItem() {
        this(1, 1, 0);
    }

    public MapImageAsymmetricGridViewItem(int columnSpan, int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public MapImageAsymmetricGridViewItem(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }

    String getImageUrl() {
        return (this.imageUrl != null) ? this.imageUrl : "http://cdn.wallpapersafari.com/40/25/zMDnPd.jpg";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}