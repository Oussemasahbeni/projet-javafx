package com.esprit.hitgym.Entity;

public class Package2 extends Package {
    public Package2(int PACKAGE_NO, String title, String amount, String description) {
        super(PACKAGE_NO, "Starter Plan", "70", description);
    }

    public Package2() {
        super("Starter Plan", "70");
    }
}
