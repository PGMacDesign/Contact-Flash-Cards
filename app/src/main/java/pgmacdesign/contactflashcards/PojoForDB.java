package pgmacdesign.contactflashcards;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by pmacdowell on 2018-04-20.
 */

@RealmClass
public class PojoForDB implements RealmModel {

    @SerializedName("contactId")
    @PrimaryKey
    private String contactId;
    @SerializedName("contact")
    private String contact;
    @SerializedName("type")
    private int type;
    @SerializedName("isSelected")
    private boolean isSelected;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
