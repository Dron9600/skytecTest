package kz.prudnikov.test.gameMechanics;

import kz.prudnikov.test.database.Database;

import java.util.Random;

public class QuestReward implements Runnable{

    int createPlayers, questGold;
    String player;
    Random random = new Random();

    public QuestReward(int createPlayers) {
        this.createPlayers = createPlayers;
    }

    public synchronized void getGoldFromQuest(){                             // method imitating the process of getting gold after quest
        String pl = "player"  + random.nextInt(createPlayers);
        int questGold = random.nextInt(1000);
        Database.putGoldAfterQuest(pl, questGold);
        this.player =pl;
        this.questGold = questGold;
    }

    public String getAction(){
        String action = "got gold after quest " + questGold;
        return action;
    }

    public String getName(){
        String name = player;
        return name;
    }

    @Override
    public void run() {
        getGoldFromQuest();
        System.out.println(" Quest thread = " +  Thread.currentThread().getName());
        System.out.println();

    }
}
