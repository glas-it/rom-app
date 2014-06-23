package ar.com.glasit.rom.Model;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by pablo on 21/05/14.
 */
public class Coupon {

    private static final String SEPARATOR = "&";

    private String data;
    private long id = -1;
    private String title;
    private Date start, end;

    public Coupon(String data) {
        String[] split = data.split(SEPARATOR);
        try {
            this.id = Integer.parseInt(split[0]);
            this.title = split[1];
            this.start = new Date(Long.parseLong(split[2]));
            this.end = new Date(Long.parseLong(split[3]));
            this.data = data;
        } catch (Exception e) {
        }
    }

    public Coupon(JSONObject json) {
        try {
            this.id = json.getLong("id");
            this.title = json.getString("nombre");
            this.start = new Date(json.getLong("fechaInicio"));
            this.end = new Date(json.getLong("fechaFin"));
            this.data = id + SEPARATOR + title + SEPARATOR + start.getTime() + SEPARATOR + end.getTime();
        } catch (Exception e) {
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getExpirationDate() {
        return end;
    }

    public Date getStartDate() {
        return start;
    }
    @Override
    public String toString() {
        return this.data;
    }
}
