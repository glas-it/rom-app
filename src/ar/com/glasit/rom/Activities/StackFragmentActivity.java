package ar.com.glasit.rom.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.Stack;

public class StackFragmentActivity extends SherlockFragmentActivity{

    private static final long SPLASH_SCREEN_DELAY = 3000;
    private Stack<Fragment> mFragmentStack = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Stack<Fragment>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!this.mFragmentStack.isEmpty()) {
            this.mFragmentStack.pop();
        }
    }

    protected void pushFragment(SherlockFragment fragment) {
        this.pushFragment(fragment, R.id.container);
    }

    protected void pushFragment(SherlockFragment fragment, int resourceContainer) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(ft, resourceContainer);
        ft.replace(resourceContainer, fragment);
        ft.addToBackStack(null);
        ft.commit();
        mFragmentStack.push(fragment);
    }

    protected void addFragment(SherlockFragment fragment) {
        this.addFragment(fragment, R.id.container);
    }

    protected void addFragment(SherlockFragment fragment, int resourceContainer) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(ft, resourceContainer);
        ft.add(resourceContainer, fragment);
        ft.commit();
        mFragmentStack.push(fragment);
    }

    protected void replaceFragment(SherlockFragment fragment) {
        this.replaceFragment(fragment, R.id.container);
    }

    protected void replaceFragment(SherlockFragment fragment, int resourceContainer) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(ft, resourceContainer);
        ft.replace(resourceContainer, fragment);
        ft.commit();
        mFragmentStack.push(fragment);
    }

    protected void setCustomAnimation(FragmentTransaction ft, int resourceContainer) {
    }

    protected Fragment getLastFragment(){
        return !this.mFragmentStack.isEmpty() ? this.mFragmentStack.lastElement() : null;
    }
}
