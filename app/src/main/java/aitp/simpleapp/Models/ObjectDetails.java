package aitp.simpleapp.Models;


import java.io.Serializable;

public class ObjectDetails implements Serializable {
    boolean favorite;
    float rating;
    String notes;

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
