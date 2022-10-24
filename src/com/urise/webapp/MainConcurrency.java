package com.urise.webapp;

public class MainConcurrency {

    private static final Violin violin = new Violin();
    private static final Bow bow = new Bow();

    public static void main(String[] args) {
        while (true) {
            Thread thread1 = new Thread(new Musician("Mike"));
            Thread thread2 = new Thread(new Musician("Jack"));
            thread1.start();
            thread2.start();
        }
    }

    public static class Violin {
        @Override
        public String toString() {
            return "Violin";
        }
    } //скрипка

    public static class Bow {
        @Override
        public String toString() {
            return "Bow";
        }
    } //смычок

    public static class Musician implements Runnable {
        private final String name;

        public Musician(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            playSong("first song", bow, violin);
            playSong("second song", violin, bow);
        }

        private void playSong(String songName, Object firstBlockObject, Object secondBlockObject) {
            synchronized (firstBlockObject) {
                System.out.println(name + " takes the " + firstBlockObject + " and waiting for " + secondBlockObject + " for playing the " + songName);
                synchronized (secondBlockObject) {
                    System.out.println(name + " takes the " + secondBlockObject + " for playing the " + songName);
                    System.out.println(name + " is playing the " + songName);
                }
            }
        }
    }
}
