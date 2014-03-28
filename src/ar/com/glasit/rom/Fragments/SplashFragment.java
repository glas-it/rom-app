package ar.com.glasit.rom.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;

public class SplashFragment extends SherlockFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container,
                false);
    }

}
