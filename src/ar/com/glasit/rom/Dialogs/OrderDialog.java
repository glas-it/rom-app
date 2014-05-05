package ar.com.glasit.rom.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import ar.com.glasit.rom.Model.Addition;
import ar.com.glasit.rom.Model.ItemProduct;
import ar.com.glasit.rom.Model.ItemSubRubro;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.WellKnownMethods;
import com.actionbarsherlock.app.SherlockDialogFragment;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Vector;

public class OrderDialog extends SherlockDialogFragment{

    private Order order;
    private OnSubmitListener submitListener;

    private Addition selectedAddition;
    private NameValuePair selectedPrice;

    public OrderDialog(Order order) {
        this.order = order;
    }

    public void setOnSubmitListener(OnSubmitListener listener) {
        this.submitListener = listener;
    }

    public interface OnSubmitListener {
        void onSubmitListener(Order order);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getSherlockActivity());
        dialog.getWindow().setTitle(order.toString());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_order);
        dialog.show();

        dialog.setTitle(order.toString());

        LinearLayout orders = (LinearLayout) dialog.findViewById(R.id.orders);
        for (int i = 0; i < order.getFullCount(); i++) {
            LayoutInflater inflater = getSherlockActivity().getLayoutInflater();
            TableRow newRow = (TableRow) inflater.inflate(R.layout.dialog_order_item, orders, false);
            ((TextView)newRow.findViewById(R.id.text1)).setText(order.getStatus(i));
            if (order.getStatusEnum(i).equals(Order.Status.PENDANT)) {
                newRow.setBackgroundColor(getResources().getColor(R.color.dark_red));
            } else if (order.getStatusEnum(i).equals(Order.Status.DOING)) {
                newRow.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
            } else if (order.getStatusEnum(i).equals(Order.Status.DONE)) {
                newRow.setBackgroundColor(getResources().getColor(R.color.dark_green));
            } else if (order.getStatusEnum(i).equals(Order.Status.CANCELLED)) {
                newRow.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            } else if (order.getStatusEnum(i).equals(Order.Status.DELIVERED)) {
                newRow.setBackgroundColor(getResources().getColor(R.color.dark_green));
                newRow.findViewById(R.id.image).setVisibility(View.VISIBLE);
            };
            if (order.getStatus(i).equals(Order.Status.PENDANT.toString())) {
                Button button = (Button) newRow.findViewById(R.id.button);
                button.setVisibility(View.VISIBLE);
                button.setText("Cancelar");
                button.setOnClickListener(new OnCancel(i, newRow));
            } else if (order.getStatus(i).equals(Order.Status.DELIVERED.toString())) {
                Button button = (Button) newRow.findViewById(R.id.button);
                button.setVisibility(View.VISIBLE);
                button.setText("Rechazar");
                button.setOnClickListener(new OnReject(i, newRow));
            }
            orders.addView(newRow);
        }
        return dialog;
    }

    private class OnCancel implements OnClickListener {
        View newRow;
        int i;
        OnCancel(int i, View txt) {
            this.i = i;
            this.newRow = txt;
        }
        @Override
        public void onClick(View v) {
            order.setStatus(i, Order.Status.CANCELLED);
            (newRow).setBackgroundColor(getResources().getColor(R.color.dark_gray));
            ((TextView)newRow.findViewById(R.id.text1)).setText("Cancelado");
            List<NameValuePair> param = new Vector<NameValuePair>();
            param.add(new BasicNameValuePair("uuidOrden", order.getId() + "-" + i));
            RestService.callPostService(null, WellKnownMethods.OrderCancel, param);
        }
    }

    private class OnReject implements OnClickListener {
        int i;
        View newRow;
        OnReject(int i, View txt) {
            this.i = i;
            this.newRow = txt;
        }
        @Override
        public void onClick(View v) {
            order.setStatus(i, Order.Status.REJECTED);
            (newRow).setBackgroundColor(getResources().getColor(R.color.dark_gray));
            ((TextView)newRow.findViewById(R.id.text1)).setText("Rechazado");
            List<NameValuePair> param = new Vector<NameValuePair>();
            param.add(new BasicNameValuePair("uuidOrden", order.getId() + "-" + i));
            RestService.callPostService(null, WellKnownMethods.OrderRejected, param);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        submitListener.onSubmitListener(order);
    }
}
