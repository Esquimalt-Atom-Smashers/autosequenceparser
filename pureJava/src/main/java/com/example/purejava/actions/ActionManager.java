package com.example.purejava.actions;

public class ActionManager {
    public static class PrintAction implements Action {
        String message;

        public PrintAction(String message) {
            this.message = message;
        }

        @Override
        public boolean run() {
            System.out.println(message);
            return false;
        }
    }

    public static Action goToPoseAction(double x, double y, double h) {return new PrintAction("GO.TO" + x + ", " + y + ", " + h);}
}
