package pgmacdesign.contactflashcards;

import com.google.gson.annotations.SerializedName;
import com.pgmacdesign.pgmactips.utilities.ContactUtilities;

/**
 * Created by pmacdowell on 2018-04-20.
 */
public class MyPojo {

    @SerializedName("contactId")
    private String contactId;
    @SerializedName("contact")
    private ContactUtilities.Contact contact;
    @SerializedName("type")
    private AdapterContacts.AdapterContactsTypes type;
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

    public ContactUtilities.Contact getContact() {
        return contact;
    }

    public void setContact(ContactUtilities.Contact contact) {
        this.contact = contact;
    }

    public AdapterContacts.AdapterContactsTypes getType() {
        return type;
    }

    public void setType(AdapterContacts.AdapterContactsTypes type) {
        this.type = type;
    }
}
