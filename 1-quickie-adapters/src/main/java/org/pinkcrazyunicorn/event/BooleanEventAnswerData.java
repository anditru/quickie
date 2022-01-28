package org.pinkcrazyunicorn.event;

public class BooleanEventAnswerData implements EventAnswerData {
    private boolean data;

    public BooleanEventAnswerData(boolean data) {
        this.data = data;
    }

    @Override
    public String toJson() {
        if (this.data) {
            return "true";
        }
        return "false";
    }
}