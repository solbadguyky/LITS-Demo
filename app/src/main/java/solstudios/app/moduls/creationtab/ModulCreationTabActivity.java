package solstudios.app.moduls.creationtab;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import solstudios.app.R;

public class ModulCreationTabActivity extends AppCompatActivity implements OnMapReadyCallback {

    //FrameLayout collapsingToolbarContent;
    //DraggableView draggablePanel;
    CoordinatorLayout rootView;
    AppBarLayout appBarLayout;
    DraggableLayout draggableLayout;
    TextView contentView;
    FrameLayout creationBar;
    NestedScrollView nestedScrollView;
    GoogleMap mMap;

    DisplayMetrics displayMetrics;
    CreationButton creationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modul_creation_tab);
        initValue();
        initView();
        setupView();
    }

    void initValue() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

    }

    void setupValue() {

    }

    void initView() {
        rootView = (CoordinatorLayout) findViewById(R.id.activity_modul_creation_tab);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        draggableLayout = (DraggableLayout) findViewById(R.id.draggableLayout);
        creationBar = (FrameLayout) findViewById(R.id.creationTab);
        contentView = (TextView) findViewById(R.id.contentView);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        creationButton = (CreationButton) creationBar.findViewById(R.id.creationTab_actionFirstView);

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left,
                                       int top,
                                       int right,
                                       int bottom,
                                       int oldLeft,
                                       int oldTop,
                                       int oldRight,
                                       int oldBottom) {
                creationButton.parentHeight = bottom;
                draggableLayout.setMiddlePosition(oldBottom / 2);
                creationButton.parentMiddle = oldBottom / 2;
            }
        });
    }

    void setupView() {

        //collapsingToolbarContent.setMinimumHeight(displayMetrics.heightPixels - toolbarView.getHeight());
        /*CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) nestedScrollView.getLayoutParams();
        params.topMargin = displayMetrics.heightPixels - creationBar.getHeight();
        nestedScrollView.setLayoutParams(params);*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        draggableLayout.setOnStateChangeListener(new DraggableLayout.OnStateChange() {
            @Override
            public void onMinimize() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {
                    //creationButton.animateSelectedTab();
                    creationButton.currentPosition = CreationButton.PositionState.Bottom;
                    // creationButton.onReset(true);
                    creationButton.resetChildViews();

                    /*if (!creationButton.isInLayout()) {
                        creationButton.requestLayout();
                    }*/


                }
            }

            @Override
            public void onMaximize() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {
                    //creationButton.animateSelectedTab();
                    creationButton.currentPosition = CreationButton.PositionState.Top;
                    //creationButton.requestLayout();

                }
            }

            @Override
            public void onMiddlemize() {

            }

            @Override
            public void onStartDragging() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {

                    ///Kiểm trả vị trí của creationbutton
                    float cy = creationButton.getBottom();
                    float dy = rootView.getHeight() - creationBar.getBottom();
                    //Logger.d(cy);
                    if (dy == 0 || dy <= draggableLayout.getMiddlePosition()) {
                        creationButton.onReset(true);
                        creationButton.resetChildViews();
                    } else {
                        creationButton.onReset(false);
                    }


                }
            }
        });

        creationButton.setButtonStateChangeListener(new CreationButton.ButtonStateChange() {
            @Override
            public void onChanged(CreationButton.ButtonState buttonState) {
                ///Dịch chuyễn map camera đến user-location
                movingCameraToCeleb();

                switch (buttonState) {
                    case First:
                        draggableLayout.findViewById(R.id.creationTab_Editor)
                                .setBackgroundColor(getResources().getColor(R.color.md_amber_A700));
                        break;
                    case Second:
                        draggableLayout.findViewById(R.id.creationTab_Editor)
                                .setBackgroundColor(getResources().getColor(R.color.md_red_800));
                        break;
                    case Third:
                        draggableLayout.findViewById(R.id.creationTab_Editor)
                                .setBackgroundColor(getResources().getColor(R.color.md_brown_600));
                        break;
                }
            }

            @Override
            public void onClicked(CreationButton.ButtonState buttonState) {
                //Logger.d(buttonState);
                creationButton.setButtonState(buttonState);
            }
        });

        creationButton.setButtonState(CreationButton.ButtonState.First);

    }

    LatLng hcmLatLng;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (hcmLatLng == null) {
            hcmLatLng = new LatLng(10.8231, 106.6297);
        }

        mMap.addMarker(new MarkerOptions().position(hcmLatLng).title("Marker in HCM"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(hcmLatLng));

        mMap.setMaxZoomPreference(15f);
        //draggableLayout.minimize();
    }

    void movingCameraToCeleb() {
        if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {

            ///Kiểm trả vị trí của creationbutton
            float offsetY = creationButton.getBottom();

            //Logger.d(offsetY);

            if (mMap != null && hcmLatLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hcmLatLng));
            }
        }
    }

}
