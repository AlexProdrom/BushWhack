package bw.bushwhack.data.enums;

/**
 * Created by prodromalex on 5/28/2017.
 */

public enum StatusEnum {
    STARTED(0), IN_PROGRESS(1), FINISHED(2);

    private int value;

    private StatusEnum(int s) {
        this.value = s;
    }

    public int getValue() {
        return this.value;
    }
}
