package solstudios.app.moduls.mapviews.MapPool;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * MapMarkerBound dùng để xác định những marker nào 'có khả năng' tiến vào vùng hiển thị để
 * thực hiện tính toán
 * <p>
 * Mỗi marker option sẽ được gắn vào một ID kèm tọa độ Lat/Long
 * Lat/Long sẽ được lưu vào một trường 0->x
 */
public class MapMarkerBoundHelper {

    private ArrayList<MarkerOptions> markerOptionsArrayList;
    public LatLngBounds currentMapBound;

    public MapMarkerBoundHelper() {
        markerOptionsArrayList = new ArrayList<MarkerOptions>() {
            @Override
            public boolean add(MarkerOptions o) {
                return super.add(o);
            }
        };
    }

    public void addMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptionsArrayList.add(markerOptions);
    }

    /**
     * Sử dụng thuật toán sắp xếp dựa vào vị trí của marker so với gốc màn hình
     *
     * @return
     */
    public ArrayList<MarkerOptions> sortArrayBaseOnLatLn(LatLng latLng, LatLng topLatLn) {
        //Logger.d("Lat: " + latLng.latitude + "|Long: " + latLng.longitude);

        /*double maxDistance =
                Math.sqrt(
                        ((latLng.latitude - topLatLn.latitude)
                                * (latLng.latitude - topLatLn.latitude))
                                + (latLng.longitude - topLatLn.longitude)
                                * (latLng.longitude - topLatLn.longitude));*/

        ArrayList<MarkerOptions> arrayListResult = new ArrayList<>();
        LatLngBounds mLatLngBounds = new LatLngBounds(latLng, topLatLn);
        for (MarkerOptions markerOptions : this.markerOptionsArrayList) {
            if (mLatLngBounds.contains(markerOptions.getPosition())) {
                arrayListResult.add(markerOptions);
            }
            /*if (
               ///Nếu marker nằm bên dưới Lat của màn hình,
               // bên trái Long của màn hình thì loại ngay lập tức
            if (markerOptions.getPosition().latitude < latLng.latitude
                    || markerOptions.getPosition().longitude < latLng.longitude) {
                continue;
            }*/

          /*  double distance =
                    Math.sqrt(
                            ((latLng.latitude - markerOptions.getPosition().latitude)
                                    * (latLng.latitude - markerOptions.getPosition().latitude))
                                    + ((latLng.longitude - markerOptions.getPosition().longitude)
                                    * (latLng.longitude - markerOptions.getPosition().longitude)));


            if (distance <= maxDistance) {
                arrayListResult.add(markerOptions);
                double tanA = (latLng.longitude - markerOptions.getPosition().longitude) /
                        (latLng.latitude - markerOptions.getPosition().latitude);
                if (tanA > 0 && tanA < 1) {
                    //Logger.d("Distance = " + distance + ", Degree = " + tanA);
                }

                //Logger.d(distance);
            } else {
                continue;
            }*/
        }

        return arrayListResult;
    }

    public ArrayList<MarkerOptions> sortArrayBaseOnLong() {
        Collections.sort(this.markerOptionsArrayList, new Comparator<MarkerOptions>() {
            @Override
            public int compare(MarkerOptions markerOptions, MarkerOptions t1) {
                return (int) (markerOptions.getPosition().longitude - t1.getPosition().longitude);
            }
        });

        return this.markerOptionsArrayList;
    }

    public ArrayList<MarkerOptions> getMarkerOptionsBaseOnBound(Projection projection,
                                                                boolean active) {
        ArrayList<MarkerOptions> activeMarkerOptionses = new ArrayList<>();
        ArrayList<MarkerOptions> inActivemarkerOptionses = new ArrayList<>();


        if (active) {
            return activeMarkerOptionses;
        } else {
            return inActivemarkerOptionses;
        }
    }

}
