package data;

import java.io.Serializable;

public class DuplicateTextFragment implements Serializable {

    private static final long serialVersionUID = -7788619177798333712L;
    //Issues in Jira!
    Issue i1;
    Issue i2;

    //of Issue i2:
    int startDuplicate, length;

    public DuplicateTextFragment(String s1, String s2, int startDuplicate, int length) {
        this.i1 = new Issue(s1);
        this.i2 = new Issue(s2);
        this.startDuplicate = startDuplicate;
        this.length = length;
    }

    public void setStartDuplicate(int startDuplicate) {
        this.startDuplicate = startDuplicate;
    }

    public void setLength(int length) {
        this.length = length;
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

    public int getLength() {
        return length;
    }

}
