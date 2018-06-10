package com.example.hauntarl.smartfarming;

/**
 * Created by hauntarl on 25/4/18.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class EulaClass extends SignupActivity {
    private String EULA_PREFIX = "appeula";
    private Activity mContext;

    public EulaClass(Activity context) {
        mContext = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        boolean bAlreadyAccepted = prefs.getBoolean(eulaKey, false);
        if (!bAlreadyAccepted) {

            // EULA title
            String title = mContext.getString(R.string.eulaString);

            // EULA text
            String message = "1. We (genX Farming) can give only Practical technical guidance regarding agriculture cultivation and marketing\n\n"+
                    "2. We (genX Farming) are not responsible for agricultural final produce quality and yield\n\n"+
                    "3. We (genX Farming) are not responsible for crop damage\n\n" +
                    "4. We (genX Farming) will not give anything in writing\n\n" +
                    "5. We (genX Farming) will provide guidance only after your request\n\n" +
                    "6. We (genX Farming) are not entitled to visit your crop\n\n" +
                    "7. We (genX Farming) will provide agricultural inputs only after mentioning the demand on web and availability of the product in the market\n\n" +
                    "8. We (genX Farming) will give guidance, services after your registration\n\n" +
                    "9. We (genX Farming) will give guidance for the specific crop and crop period only for which you have taken registration\n\n" +
                    "10. We (genX Farming) are not responsible for crop market price.";

            // Disable orientation changes, to prevent parent activity
            // reinitialization
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.iAgree,
                            new Dialog.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface, int i) {
                                    // Mark this version as read.
                                    SharedPreferences.Editor editor = prefs
                                            .edit();
                                    editor.putBoolean(eulaKey, true);
                                    editor.commit();

                                    // Close dialog
                                    dialogInterface.dismiss();

                                    // Enable orientation changes based on
                                    // device's sensor
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new Dialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // Close the activity as they have declined
                                    // the EULA
                                    SharedPreferences.Editor editor = prefs
                                            .edit();
                                    editor.putBoolean(eulaKey, false);
                                    editor.commit();
                                    mContext.finish();
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                }

                            });
            builder.create().show();
        }
    }
}
