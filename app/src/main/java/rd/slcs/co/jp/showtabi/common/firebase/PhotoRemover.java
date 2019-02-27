package rd.slcs.co.jp.showtabi.common.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;

public class PhotoRemover {

    private final String photoKey;

    public PhotoRemover(String pPhotoKey) {
        photoKey = pPhotoKey;
    }

    public void removePhoto(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE + "/" + photoKey);
        mDatabase.removeValue();
    }
}
