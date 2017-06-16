package bw.bushwhack.data.enums;

/**
 * Created by prodromalex on 5/28/2017.
 */

/*  Update by Liis.
    Start -> trail beginning
    Next_goal -> next marker to be passed
    Intermediate -> already passed/will be passed in future, but not first/last/next
    Finish -> last marker on trail
*/
public enum MarkerTypeEnum {
    START(0), NEXT_GOAL(1), INTERMEDIATE(2), FINISH(3);

    private int value;

    MarkerTypeEnum(int s) {
        this.value = s;
    }

    public int getValue() {
        return this.value;
    }
}
