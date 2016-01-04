package dialogFolder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;


/***
 * Created by Johnson_moon on 2016/01/04
 * */
public class dialog {
    private String message;
    private AlertDialog.Builder builder;

    public dialog(Context context, String showMessage){
        this.message = showMessage;
        this.builder = new AlertDialog.Builder(context);
        builder.setMessage(this.message);
        builder.setTitle("Warning");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        this.builder.show();
    }
}
