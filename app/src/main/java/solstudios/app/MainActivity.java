package solstudios.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import solstudios.app.moduls.anchorpoint.LayoutAnchorActivity;
import solstudios.app.moduls.creationtab.ModulCreationTabActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterCreationModul();
    }

    void enterCreationModul() {
        Intent intent = new Intent(this, ModulCreationTabActivity.class);
        this.startActivity(intent);
    }
}
