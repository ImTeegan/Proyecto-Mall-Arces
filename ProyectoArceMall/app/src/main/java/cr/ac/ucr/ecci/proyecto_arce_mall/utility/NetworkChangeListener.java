package cr.ac.ucr.ecci.proyecto_arce_mall.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import cr.ac.ucr.ecci.proyecto_arce_mall.MainActivity;
import cr.ac.ucr.ecci.proyecto_arce_mall.R;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(!Common.isConnectedToInternet(context)) {
            DbHelper dataBase = new DbHelper(context);
            if(dataBase.getLoginUser().getEmail() != null ){
                dataBase.deleteUserLogged();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.no_internet_connection, null);
            builder.setView(view);

            AppCompatButton retryButton = view.findViewById(R.id.retry_button);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            retryButton.setOnClickListener(view1 -> {
                dialog.dismiss();
                onReceive(context, intent);
                if (Common.isConnectedToInternet(context)) {
                    this.goToMainScreen(context);
                }
            });
        }
    }

    private void goToMainScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
