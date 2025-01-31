package com.esprit.hitgym.Entity;

public class Package1 extends Package {
    public Package1(int PACKAGE_NO, String description) {
        super(PACKAGE_NO, "Beginner Plan", "50", description);
    }

    public Package1() {
        super("Beginner Plan", "50");
    }
}
