package rd.slcs.co.jp.showtabi.common;

import android.app.Activity;
import android.content.Context;

public class UseImagePicker {


    // ImagePicker起動　選択上限30枚
    public static void start(Context context){

        com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker.with((Activity)context)                         //  Initialize UseImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("アップロード画像選択")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("決定")               //  Done button title
                .setLimitMessage("これ以上画像を選択できません!!")    // Selection limit message
                .setMaxSize(30)                     //  Max images can be selected
                .setSavePath("UseImagePicker")         //  Image capture folder name
                //.setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                //.setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start UseImagePicker

    }

}
