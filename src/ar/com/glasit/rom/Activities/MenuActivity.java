package ar.com.glasit.rom.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import ar.com.glasit.rom.Fragments.ItemFragment;
import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.MenuItem;

import java.util.List;

public class MenuActivity extends StackFragmentActivity implements OnSelectItemListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new MenuFragment(this));
    }

    @Override
    public void selectItem(IItem item) {
        if (item.hasChildren() && item.isAvailable()){
            pushFragment(new ItemFragment(item, this));
        } else if (item.getClass() == ItemProduct.class && item.isAvailable()){
            showItemDialog(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                popAll();
                return true;
            case R.id.close_Session:
                BackendHelper.setLoggedUser("");
                Intent intent = new Intent(this, StartSessionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
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

        List<Addition> additions = ((ItemSubRubro)item.getParent()).getAdditions();

        if (!additions.isEmpty()) {
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.addition_radios);
            boolean showRadioGroup = false;
            for (Addition addition: additions) {
                RadioButton rad = new RadioButton(this);
                if (!addition.toString().isEmpty() &&
                        !addition.toString().equals("null") &&
                            !addition.toString().trim().equals("null")) {
                    showRadioGroup = true;
                    rad.setText(addition.toString());
                    radioGroup.addView(rad);
                }
            }
            if (showRadioGroup) {
                view.findViewById(R.id.addition_layout).setVisibility(View.VISIBLE);
                ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            }
        }

        List<String> prices = ((ItemProduct)item).getPrices();

        if (!prices.isEmpty()) {
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.price_radios);
            boolean showRadioGroup = false;
            for (String s: prices) {
                RadioButton rad = new RadioButton(this);
                if (!s.toString().isEmpty() &&
                        !s.toString().equals("null") &&
                        !s.toString().trim().equals("null")) {
                    showRadioGroup = true;
                    rad.setText(s);
                    radioGroup.addView(rad);
                }
            }
            if (showRadioGroup) {
                view.findViewById(R.id.price_layout).setVisibility(View.VISIBLE);
                ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            }
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
