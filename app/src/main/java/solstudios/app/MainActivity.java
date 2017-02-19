package solstudios.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import solstudios.app.moduls.mapviews.MapViewsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterCreationModul();
    }

    void enterCreationModul() {
        Intent intent = new Intent(this, MapViewsActivity.class);
        this.startActivity(intent);
    }
}
