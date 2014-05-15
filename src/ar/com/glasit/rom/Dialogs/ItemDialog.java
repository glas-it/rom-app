package ar.com.glasit.rom.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import ar.com.glasit.rom.Model.Addition;
import ar.com.glasit.rom.Model.ItemProduct;
import ar.com.glasit.rom.Model.ItemSubRubro;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockDialogFragment;
import org.apache.http.NameValuePair;

import java.util.List;

public class ItemDialog extends SherlockDialogFragment{

    private ItemProduct item;
    private OnSubmitListener submitListener;
    private OnCancelListener cancelListener;

    private Addition selectedAddition;
    private NameValuePair selectedPrice;

    private EditText input;
    public ItemDialog(ItemProduct item) {
        this.item = item;
    }

    public void setOnSubmitListener(OnSubmitListener listener) {
        this.submitListener = listener;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
    }

    public interface OnSubmitListener {
        void onSubmitListener(Order o);
    }

    public interface OnCancelListener {
        void onCancelListener();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getSherlockActivity());
        dialog.getWindow().setTitle(item.toString());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_item);
        dialog.show();


        String description = item.getDescription();
        if (description != null && !description.isEmpty()) {
            dialog.findViewById(R.id.description_layout).setVisibility(View.VISIBLE);
            TextView textView = (TextView) dialog.findViewById(R.id.description);
            textView.setText(description);
        }

        List<Addition> additions = ((ItemSubRubro)item.getParent()).getAdditions();

        if (!additions.isEmpty()) {
            RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.addition_radios);
            boolean showRadioGroup = false;
            for (Addition addition: additions) {
                RadioButton rad = new RadioButton(getSherlockActivity());
                if (!addition.toString().isEmpty() &&
                        !addition.toString().equals("null") &&
                        !addition.toString().trim().equals("null")) {
                    showRadioGroup = true;
                    rad.setText(addition.toString());
                    rad.setOnCheckedChangeListener(new OnAdditionSelected(addition));
                    radioGroup.addView(rad);
                }
            }
            if (showRadioGroup) {
                dialog.findViewById(R.id.addition_layout).setVisibility(View.VISIBLE);
                ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            }
        }

        List<NameValuePair> prices = ((ItemProduct)item).getPrices();

        if (!prices.isEmpty()) {
            RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.price_radios);
            boolean showRadioGroup = false;
            for (NameValuePair s: prices) {
                RadioButton rad = new RadioButton(getSherlockActivity());
                if (!s.getName().isEmpty() &&
                        !s.getName().equals("null") &&
                        !s.getName().trim().equals("null")) {
                    showRadioGroup = true;
                    rad.setText((prices.size() == 1) ? "$ " + s.getValue() : s.getName());
                    rad.setOnCheckedChangeListener(new OnPriceSelected(s));
                    radioGroup.addView(rad);
                }
            }
            if (showRadioGroup) {
                dialog.findViewById(R.id.price_layout).setVisibility(View.VISIBLE);
                ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            }
        }

        input = (EditText) dialog.findViewById(R.id.input);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Order o = new Order(item, selectedAddition, selectedPrice);
                o.setCount(1);
                o.setNotes(input.getText().toString());
                submitListener.onSubmitListener(o);
                dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListener.onCancelListener();
                dismiss();
            }
        });
        return dialog;
    }

    private class OnAdditionSelected implements CompoundButton.OnCheckedChangeListener {

        private Addition addition;

        private OnAdditionSelected(Addition addition) {
            this.addition = addition;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                selectedAddition = addition;
            }
        }
    }

    private class OnPriceSelected implements CompoundButton.OnCheckedChangeListener {

        private NameValuePair price;

        private OnPriceSelected(NameValuePair price) {
            this.price = price;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                selectedPrice = price;
            }
        }
    }
}
