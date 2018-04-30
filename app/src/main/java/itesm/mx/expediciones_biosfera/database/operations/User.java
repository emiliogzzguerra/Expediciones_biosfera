package itesm.mx.expediciones_biosfera.database.operations;

/**
 * Created by javier on 30/04/18.
 */

public class User {

    private long id;
    private String fbid;
    private String occupations;
    private String interests;
    private String phone;
    private byte[] picture;

    public User() {
        this.id = 0;
        this.fbid = null;
        this.occupations = null;
        this.interests = null;
        this.phone = null;
        this.picture = null;
    }

    public User(String fbid, String occupations, String interests, String phone, byte[] picture) {
        this.fbid = fbid;
        this.occupations = occupations;
        this.interests = interests;
        this.phone = phone;
        this.picture = picture;
    }

    public User(long id, String fbid, String occupations, String interests, String phone,
                byte[] picture) {
        this.id = id;
        this.fbid = fbid;
        this.occupations = occupations;
        this.interests = interests;
        this.phone = phone;
        this.picture = picture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getOccupations() {
        return occupations;
    }

    public void setOccupations(String occupations) {
        this.occupations = occupations;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
