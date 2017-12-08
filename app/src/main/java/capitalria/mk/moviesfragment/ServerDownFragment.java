package capitalria.mk.moviesfragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerDownFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_internet_missing)
                .setMessage(R.string.server_down)
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    getActivity().finish();
                    System.exit(0);
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
