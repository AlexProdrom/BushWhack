package bw.bushwhack.enums;

/**
 * Created by prodromalex on 5/28/2017.
 */

public enum StatusEnum {
    STARTED(1), IN_PROGRESS(2), FINISHED(3);

    private int value;

    private StatusEnum(int s) {
        this.value = s;
    }

    public int getValue() {
        return this.value;
    }
}
