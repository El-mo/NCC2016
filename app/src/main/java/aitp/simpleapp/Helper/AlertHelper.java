package aitp.simpleapp.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import aitp.simpleapp.Callbacks.AlertCallBack;


public class AlertHelper {
    private Context mContext;
    private AlertCallBack alertCallBack;

    public AlertHelper(Context context, AlertCallBack callBack){
        mContext = context;
        alertCallBack = callBack;
    }

    public void makeAlertWithNumberInput(final String Title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(Title);

        // Set up the input
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperTextResponse(Title, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperTextResponse(Title, "Cancel");
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void makeAlertWithTextInput(final String Title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(Title);

        // Set up the input
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperTextResponse(Title, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperTextResponse(Title, "Cancel");
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void makeAlertBoolean(final String Title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(Title);


        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperBooleanResponse(Title, true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertCallBack.alertHelperBooleanResponse(Title, false);
                dialog.cancel();
            }
        });
        builder.show();
    }
}
