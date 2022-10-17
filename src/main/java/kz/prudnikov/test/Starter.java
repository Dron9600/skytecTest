package kz.prudnikov.test;

import kz.prudnikov.test.database.Database;
import kz.prudnikov.test.gameMechanics.AddGoldToTreasure;
import kz.prudnikov.test.gameMechanics.QuestReward;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Starter {
    static String folderForDB = "jdbc:sqlite:" + "D:/";   // put directory on your PC for creating DB. e.g. D:/Java/2022/SkyTecTest/DB/
    static String nameDB = "ClansDB.db";  // DB name
    static Random random = new Random();
    static int quantityOfPlayers = 50; // quantity of players in your game. You can choose any quantity
    static int quantityOfProcesses = 50; // number of processes from real life which one doing in real life
    static int quantityOfThread = 10; // quantity of threads, depends on power of the server
    static int quantityOfClans = 5; // quntity of creating clans


    public static void main(String[] args) throws InterruptedException {


        Database.createNewDatabase(folderForDB, nameDB);
        Database.createNewTables(folderForDB, nameDB);

        Database.clearDB(folderForDB, nameDB);                                  //
        addNewPlayers(quantityOfPlayers, quantityOfClans);                      // you can commend this part if you want start process again with same players and clans
        addNewClans(quantityOfClans);                                           //


        AddGoldToTreasure addGoldToTreasure = new AddGoldToTreasure(quantityOfPlayers);
        QuestReward questReward = new QuestReward(quantityOfPlayers);

         Callable<Void> callable1 = () -> {                                     //  put each threads in special method
            addGoldToTreasure.run();
            return null;
        };
        Callable<Void> callable2 = () -> {
            questReward.run();
            return null;
        };
        List<Callable<Void>> taskList = new ArrayList<>();                      //  list fot all thread
        taskList.add(callable1);
        taskList.add(callable2);


        ExecutorService executorService = Executors.newFixedThreadPool(quantityOfThread);
        for (int i = 0; i < quantityOfProcesses; i++) {                                         //  loop for imitating real life processes
            executorService.invokeAll(taskList);
            Database.createLogOfActions(questReward.getName(), questReward.getAction());
            Database.createLogOfActions(addGoldToTreasure.getName(), addGoldToTreasure.getAction());
        }
        executorService.shutdown();                                                                 // shuting down all threads
        while(!executorService.isTerminated()){}

    }

    public static void addNewPlayers(int numberOfPlayers, int quantityOfClans){
        for (int i = 0; i < numberOfPlayers; i++) {
            String player = "player" + i;
            Database.insertNewPlayers(player, random.nextInt(1000),"Clan" + random.nextInt(quantityOfClans));
        }
    }

    public static void addNewClans(int quantityOfClans){
        for (int i = 0; i < quantityOfClans; i++) {
            String clan = "Clan"+ i;
            Database.insertNewClans(clan, random.nextInt(10000));
        }
    }

}


