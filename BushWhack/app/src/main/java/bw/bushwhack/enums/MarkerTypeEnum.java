package bw.bushwhack.enums;

/**
 * Created by prodromalex on 5/28/2017.
 */

public enum MarkerTypeEnum {
    START(1), NEXT_GOAL(2), BREAK(3),FINISH(4);

    private int value;

    private MarkerTypeEnum(int s) {
        this.value = s;
    }

    public int getValue() {
        return this.value;
    }
}
