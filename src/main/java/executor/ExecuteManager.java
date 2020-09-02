package executor;

import checker.Checker;

public class ExecuteManager {
    public void runProgram() {
        Checker song = new Checker();
        song.checkTotalWords();
        song.printShortWords();
        song.printPopularWords();
        System.out.println();
        System.out.println("Filtration of song: ");
        song.showSong();
    }
}