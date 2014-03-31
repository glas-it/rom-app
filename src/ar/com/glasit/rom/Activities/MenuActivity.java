package ar.com.glasit.rom.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ar.com.glasit.rom.Fragments.ItemFragment;
import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Model.IItem;
import ar.com.glasit.rom.Model.OnSelectItemListener;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.MenuItem;

public class MenuActivity extends StackFragmentActivity implements OnSelectItemListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new MenuFragment(this));
    }

    @Override
    public void selectItem(IItem item) {
        if (item.hasChildren()){
            pushFragment(new ItemFragment(item, this));
        } else {
            showItemDialog(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                popAll();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showItemDialog(IItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.toString());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_item, null);

        String description = item.getDescription();
        if (description != null && !description.isEmpty()) {
            view.findViewById(R.id.description_layout).setVisibility(View.VISIBLE);
            TextView textView = (TextView) view.findViewById(R.id.description);
            textView.setText(description);
        }

        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: add to order
                popAll();
            }
        });
        builder.create().show();
    }
}
