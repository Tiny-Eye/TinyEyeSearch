package gurpreetsk.me.tinyeye;

/**
 * Created by Gurpreet on 23/11/16.
 */

public class FirebaseModel {

    String userName, userContact, userFeedback;

    public FirebaseModel() {}

    public String getUserName() {
        return userName;
    }

    public FirebaseModel(String userName, String userContact, String userFeedback) {
        this.userName = userName;
        this.userContact = userContact;
        this.userFeedback = userFeedback;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

}
