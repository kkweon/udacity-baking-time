package com.example.bakingtime.data;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Ignore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeStep implements Parcelable {
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public RecipeStep createFromParcel(Parcel in) {
                    return new RecipeStep(in);
                }

                public RecipeStep[] newArray(int size) {
                    return new RecipeStep[size];
                }
            };

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("videoURL")
    @Expose
    private String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    @Expose private boolean prev;
    @Expose private boolean next;

    @Ignore
    public RecipeStep(Parcel in) {
        this.id = in.readLong();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();

        this.prev = readBoolean(in);
        this.next = readBoolean(in);
    }

    @Ignore
    public RecipeStep(
            long id,
            String shortDescription,
            String description,
            String videoURL,
            String thumbnailURL,
            boolean prev,
            boolean next) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;

        this.prev = prev;
        this.next = next;
    }

    public boolean hasPrev() {
        return prev;
    }

    public boolean hasNext() {
        return next;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
     * representation. For example, if the object will include a file descriptor in the output of
     * {@link #writeToParcel(Parcel, int)}, the return value of this method must include the {@link
     * #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
     *     object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link
     *     #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        writeBoolean(dest, prev);
        writeBoolean(dest, next);
    }

    private boolean readBoolean(Parcel in) {
        return in.readByte() == 1;
    }

    private void writeBoolean(Parcel dest, boolean prev) {
        if (prev) dest.writeByte((byte) 1);
        else dest.writeByte((byte) 0);
    }
}
