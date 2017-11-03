package kfu.ccsit.tmssks.data;

public class TimelineRecord {

    private int readingId;
    private int type;
    private String holderName;
    private long time;
    private boolean entered;

    public int getReadingId() {
        return readingId;
    }

    public void setReadingId(int readingId) {
        this.readingId = readingId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isEntered() {
        return entered;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public String getMessage() {
        String message = isEntered() ? "Entered " : "Left ";
        message += getType() == DataMedium.TYPE_TRANS ? "the bus " : " the school ";
        message += getHolderName();
        return message;
    }

    public TimelineRecord() {
        this.holderName = "";
        this.readingId = -1;
    }
}
