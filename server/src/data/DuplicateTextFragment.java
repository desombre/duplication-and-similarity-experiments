package data;

import java.io.Serializable;

public class DuplicateTextFragment implements Serializable {

    private static final long serialVersionUID = -7788619177798333712L;
    //Issues in Jira!
    Issue i1;
    Issue i2;

    //of Issue i2:
    int startDuplicate;

    int length;


    double score;

    public DuplicateTextFragment(String s1, String s2, int startDuplicate, int length,double score) {
        this.i1 = new Issue(s1);
        this.i2 = new Issue(s2);
        this.startDuplicate = startDuplicate;
        this.score = score;
        this.length = length;
    }

    public DuplicateTextFragment(Issue i1, Issue i2, String s2, int lastIndexOfDuplicate, int length, String fieldUsedForDetection) {
    }

    public void setStartDuplicate(int startDuplicate) {
        this.startDuplicate = startDuplicate;
    }

    public Issue getI1() {
        return i1;
    }

    public Issue getI2() {
        return i2;
    }

    public int getStartDuplicate() {
        return startDuplicate;
    }

    public double getScore() {
        return score;
    }
    public int getLength() {
        return length;
    }


    @Override
    public String toString() {
        return "{}";
        /*
        return "{" +
                "\"i1\":'" + i1.getDescription() +
                "', \"i2\":'" + i2.getDescription() +
                "', \"startDuplicate\":" + startDuplicate +
                ", \"length\":" + length +
                "}";

         */
    }
}
