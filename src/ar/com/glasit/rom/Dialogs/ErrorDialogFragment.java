package ar.com.glasit.rom.Dialogs;

import ar.com.glasit.rom.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ErrorDialogFragment extends SherlockDialogFragment {

    public interface IErrorListener	{
        String getErrorMessage();
        String getErrorTitle();
    }
    
	private IErrorListener listener;
   
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        
        
        builder.setMessage(listener.getErrorMessage())
        		.setTitle(listener.getErrorTitle())
        		.setPositiveButton(R.string.errorDialog, null);
        return builder.create();
    }
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    try {
	        listener = (IErrorListener) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " debe implementar IErrorListener");
	    }
	}
    

	
}
