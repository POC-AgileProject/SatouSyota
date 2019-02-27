package rd.slcs.co.jp.showtabi.common.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;

public class EventRemover {

    public void removePhotosRelatedFromEvent(String eventKey){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);
        mDatabase.removeValue();
    }
}
