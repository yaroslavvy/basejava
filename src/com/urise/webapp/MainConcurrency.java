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

    public static class Violin {} //скрипка

    public static class Bow {} //смычок

    public static class Musician implements Runnable {
        private final String name;

        public Musician(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            playFirstSong();
            playSecondSong();
        }

        private void playFirstSong () {
            synchronized (violin) {
                System.out.println(name + " takes the VIOLIN and waiting for BOW for playing the first song");
                synchronized (bow) {
                    System.out.println(name + " takes the BOW for playing the first song");
                    System.out.println(name + " is playing the first song");
                }
            }
        }

        private void playSecondSong () {
            synchronized (bow) {
                System.out.println(name + " takes the BOW and waiting for VIOLIN for playing the second song");
                synchronized (violin) {
                    System.out.println(name + " takes the VIOLIN for playing the second song");
                    System.out.println(name + " is playing the second song");
                }
            }
        }
    }
}
