package bw.bushwhack.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by prodromalex on 5/28/2017.
 */
@IgnoreExtraProperties
public class Dates {
    //Not sure if date format works so be saved in firebase, string is an alternative
    private Date startDate;
    private Date endDate;

    public Dates() {}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
