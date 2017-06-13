package bw.bushwhack.enums;

/**
 * Created by prodromalex on 5/28/2017.
 */

public enum MarkerTypeEnum {
    START(1), NEXT_GOAL(2), INTERMEDIATE(3),FINISH(4);
    //Start -> trail beginning
    //Next_goal -> next marker to be passed
    //Intermediate -> already passed/will be passed in future, but not first/last/next
    //Finish -> last marker on trail

    private int value;

    private MarkerTypeEnum(int s) {
        this.value = s;
    }

    public int getValue() {
        return this.value;
    }
}
