package edu.wpi.cs528.lzzz.carpooling_mobile.connection;

/**
 * Created by QQZhao on 10/28/17.
 */

public class HttpResponseMessage {

    private int status;
    private String content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
