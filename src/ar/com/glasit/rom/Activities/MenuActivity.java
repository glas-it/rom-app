package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;
import ar.com.glasit.rom.Dialogs.ItemDialog;
import ar.com.glasit.rom.Fragments.ItemFragment;
import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.*;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.view.MenuItem;

public class MenuActivity extends StackFragmentActivity implements OnSelectItemListener {

    private Table table;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new MenuFragment(this));

        Intent i = getIntent();
        table = TablesGestor.getInstance().getTable(i.getIntExtra("tableNumber", 0));
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
        ItemDialog dialog = new ItemDialog((ItemProduct) item);
        dialog.setOnSubmitListener(new ItemDialog.OnSubmitListener() {
            @Override
            public void onSubmitListener(Order o) {
                ((OpenTable)table).addOrder(o);
                if (getParent() == null) {
                    MenuActivity.this.setResult(RESULT_OK, getIntent());
                } else {
                    MenuActivity.this.getParent().setResult(RESULT_OK, getIntent());
                }
                finish();
            }
        });

        dialog.setOnCancelListener(new ItemDialog.OnCancelListener() {
            @Override
            public void onCancelListener() {
                if (getParent() == null) {
                    MenuActivity.this.setResult(RESULT_CANCELED, getIntent());
                } else {
                    MenuActivity.this.getParent().setResult(RESULT_CANCELED, getIntent());
                }
                MenuActivity.this.finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }
}
