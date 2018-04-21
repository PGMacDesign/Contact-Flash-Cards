package pgmacdesign.contactflashcards;

import com.pgmacdesign.pgmactips.utilities.ContactUtilities;
import com.pgmacdesign.pgmactips.utilities.MiscUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick-SSD2 on 4/21/2018.
 */

public class SimpleUtils {
	
	public static List<MyPojo> convertContactToPojoList(List<ContactUtilities.Contact> contacts,
	                                                    AdapterContacts.AdapterContactsTypes defaultType){
		List<MyPojo> pojos = new ArrayList<>();
		
		if(MiscUtilities.isListNullOrEmpty(contacts)){
			return pojos;
		}
		for(ContactUtilities.Contact c : contacts){
			if(c == null){
				continue;
			}
			MyPojo p = new MyPojo();
			p.setSelected(false);
			p.setContactId(c.getId());
			p.setContact(c);
			p.setType(defaultType);
			pojos.add(p);
		}
		return pojos;
	}
	
	public static List<MyPojo> changeMyPojosType(List<MyPojo> myPojos, AdapterContacts.AdapterContactsTypes defaultType){
		if(MiscUtilities.isListNullOrEmpty(myPojos)){
			return myPojos;
		}
		for(MyPojo c : myPojos){
			if(c == null){
				continue;
			}
			MyPojo p = new MyPojo();
			p.setType(defaultType);
		}
		return myPojos;
	}
	
}
