package solstudios.app.moduls.creationtab;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
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
import solstudios.app.moduls.creationtab.behaviors.CreationCoordinateBehavior;

/**
 * Modul creation tab dùng để hiển thị creation button và các gesture liên quan đến Creation Tab
 * tại Maps Playground
 */
public class ModulCreationTabActivity extends AppCompatActivity implements OnMapReadyCallback {

    //FrameLayout collapsingToolbarContent;
    //DraggableView draggablePanel;
    CoordinatorLayout rootView;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    DraggableLayout draggableLayout;
    TextView contentView;
    FrameLayout creationBar;
    FrameLayout creationScreen;
    GoogleMap mMap;

    DisplayMetrics displayMetrics;
    CreationGroupView creationGroupView;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        draggableLayout = (DraggableLayout) findViewById(R.id.draggableLayout);
        creationBar = (FrameLayout) findViewById(R.id.creationTab);
        //contentView = (TextView) findViewById(R.id.contentView);
        creationScreen = (FrameLayout) findViewById(R.id.creationScreen);

        creationGroupView = (CreationGroupView) creationBar.findViewById(R.id.creationTab_actionFirstView);

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
                //Logger.d(bottom);
                DraggableLayout.parentHeight = bottom;

                ///Tính toản vị trí middle Position
                draggableLayout.setMiddlePosition(middlePosition(bottom));
                DraggableLayout.middlePosition = middlePosition(bottom);
                //creationGroupView.minEditorHeight = 54;
            }

            int middlePosition(int bottom) {
                AppCompatEditText appCompatEditText = (AppCompatEditText) draggableLayout.findViewById(R.id.creationQuickEditorField);
                if (appCompatEditText != null) {
                    appCompatEditText.setTextSize(20);
                    appCompatEditText.setText("Hello, world");
                    appCompatEditText.measure(0, 0);
                    int height = appCompatEditText.getMeasuredHeight();

                    int middlePos = bottom - height - draggableLayout.getmHeaderView().getHeight();

                    return middlePos;
                }

                return 0;
            }
        });
    }

    void setupView() {
        CreationCoordinateBehavior creationCoordinateBehavior = new CreationCoordinateBehavior(this);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(creationCoordinateBehavior);


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("LITS");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        draggableLayout.setOnStateChangeListener(new DraggableLayout.OnStateChange() {
            @Override
            public void onMinimize() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {
                    //creationButton.animateSelectedTab();
                    creationGroupView.currentPosition = CreationGroupView.PositionState.Bottom;
                    // creationButton.onReset(true);
                    creationGroupView.resetChildViews();

                    /*if (!creationButton.isInLayout()) {
                        creationButton.requestLayout();
                    }*/


                }
            }

            @Override
            public void onMaximize() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {
                    //creationButton.animateSelectedTab();
                    creationGroupView.currentPosition = CreationGroupView.PositionState.Top;
                    //creationButton.requestLayout();

                }
            }

            @Override
            public void onMiddlemize() {
                creationGroupView.currentPosition = CreationGroupView.PositionState.Midle;
            }

            @Override
            public void onStartDragging() {
                if (creationBar.findViewById(R.id.creationTab_actionFirstView) != null) {

                    ///Kiểm trả vị trí của creationbutton
                    float cy = creationGroupView.getBottom();
                    float dy = rootView.getHeight() - creationBar.getBottom();
                    //Logger.d(cy);
                    if (dy == 0 || dy <= DraggableLayout.middlePosition) {
                        creationGroupView.onReset(true);
                        creationGroupView.resetChildViews();
                    } else {
                        creationGroupView.onReset(false);
                    }


                }
            }
        });

        creationGroupView.setButtonStateChangeListener(new CreationGroupView.ButtonStateChange() {
            @Override
            public void onChanged(CreationGroupView.ButtonState buttonState) {
                ///Dịch chuyễn map camera đến user-location
                movingCameraToCeleb();

                switch (buttonState) {
                    case First:
                        draggableLayout.findViewById(R.id.creationTab_editorViewHolder)
                                .setBackgroundColor(getResources().getColor(R.color.md_amber_A700));
                        break;
                    case Second:
                        draggableLayout.findViewById(R.id.creationTab_editorViewHolder)
                                .setBackgroundColor(getResources().getColor(R.color.md_red_800));
                        break;
                    case Third:
                        draggableLayout.findViewById(R.id.creationTab_editorViewHolder)
                                .setBackgroundColor(getResources().getColor(R.color.md_brown_600));
                        break;
                }
            }

            @Override
            public void onClicked(CreationGroupView.ButtonState buttonState) {
                //Logger.d(buttonState);
                creationGroupView.setButtonState(buttonState);
            }
        });

        creationGroupView.setButtonState(CreationGroupView.ButtonState.First);

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
            float offsetY = creationGroupView.getBottom();

            //Logger.d(offsetY);

            if (mMap != null && hcmLatLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hcmLatLng));
            }
        }
    }

}
