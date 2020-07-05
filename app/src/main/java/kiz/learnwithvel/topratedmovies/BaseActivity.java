package kiz.learnwithvel.topratedmovies;

import android.annotation.SuppressLint;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        @SuppressLint("InflateParams") ConstraintLayout root = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout container = root.findViewById(R.id.activity_container);
        getLayoutInflater().inflate(layoutResID, container, true);
        super.setContentView(root);
    }

    protected void activateToolBar(boolean homeEnabled, boolean showTitle) {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }

            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(showTitle);
                actionBar.setDisplayHomeAsUpEnabled(homeEnabled);
            }
        }
    }


}
