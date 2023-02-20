package com.appdev.laundarymanagement;

public class newClass {
    private String title;
    private String Value;

    public newClass(String name, String roomno) {
        this.Value = roomno;
        this.title = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }
}
