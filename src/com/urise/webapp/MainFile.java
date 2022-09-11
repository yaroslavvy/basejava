package com.urise.webapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFile {
    public static void main(String[] args) {
        File projectRootDir = new File("./");
        PrintStream printStream = System.out;
        printDir(projectRootDir, new ArrayList<>(), printStream);
    }

    private static void printDir(File dir, List<Pseudographic> lineBeforeDirName, PrintStream printStream) {
        String filePath = null;
        try {
            filePath = dir.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] elementsOfPath = filePath.split("\\\\");
        if (elementsOfPath.length != 0) {
            String fileName = elementsOfPath[elementsOfPath.length - 1];

            printStream.println(getStringBuilderFromPseudographicList(lineBeforeDirName).append(fileName).toString());

            File[] files = dir.listFiles();
            if (files != null) {
                Arrays.sort(files, (f1, f2) -> f1.isDirectory() ^ f2.isDirectory() ? (f1.isDirectory() && !f2.isDirectory() ? -1 : 1) : 0);//dir are going in first of all.
                List<Pseudographic> nextLine = new ArrayList<>(lineBeforeDirName);
                if (nextLine.size() > 0) {
                    switch (nextLine.get(nextLine.size() - 1)) {
                        case LATERAL_BRANCH:
                            nextLine.set(nextLine.size() - 1, Pseudographic.LINE);
                            break;
                        case LAST_BRANCH:
                            nextLine.set(nextLine.size() - 1, Pseudographic.INDENT);
                            break;
                    }
                }
                for (int i = 0; i < files.length; ++i) {
                    List<Pseudographic> newLine = new ArrayList<>(nextLine);
                    if (i == files.length - 1) {
                        newLine.add(Pseudographic.LAST_BRANCH);
                    } else {
                        newLine.add(Pseudographic.LATERAL_BRANCH);
                    }
                    if (files[i].isDirectory()) {
                        printDir(files[i], newLine, printStream);
                    } else {
                        printStream.println(getStringBuilderFromPseudographicList(newLine).append(files[i].getName()).toString());
                    }
                }
            }
        }
    }

    private static StringBuilder getStringBuilderFromPseudographicList(List<Pseudographic> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pseudographic pseudographic : list) {
            stringBuilder.append(pseudographic);
        }
        return stringBuilder;
    }

    private enum Pseudographic {
        LATERAL_BRANCH("\u2560\u2550"), // ╠═
        LAST_BRANCH("\u255a\u2550"), // ╚═
        LINE("\u2551 "), // ║
        INDENT("  "); // two spaces

        private final String symbols;

        Pseudographic(String symbols) {
            this.symbols = symbols;
        }

        @Override
        public String toString() {
            return symbols;
        }
    }
}
