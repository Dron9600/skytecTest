package kz.prudnikov.test.gameMechanics;


import kz.prudnikov.test.database.Database;
import kz.prudnikov.test.objects.Player;

import java.util.Random;

public class AddGoldToTreasure implements Runnable {

   Random random = new Random();
   int createPlayers;
   Player pla;
   String player;

    public AddGoldToTreasure( int createPlayers) {
        this.createPlayers = createPlayers;
    }

    public synchronized void putGoldToTreasure() {
        Player p;// imitating the process of putting gold to treasure from players
            String pl = "player"  + random.nextInt(createPlayers);
            p = Database.checkClan(pl);
            Database.putGoldToClanTreasure(p, pl);
            this.player = pl;
            this.pla = p;
    }

    public String getAction(){
        String action = "add gold to treasure " + pla.getGold();
        return action;
    }

    public String getName(){
        String name = player;
        return name;
    }


    @Override
    public void run() {                                                         // method starting new thread
        putGoldToTreasure();
        System.out.println("Treasure thread= " +  Thread.currentThread().getName());
        System.out.println();
    }
}
