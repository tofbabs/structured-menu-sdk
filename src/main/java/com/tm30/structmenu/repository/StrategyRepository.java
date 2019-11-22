package com.tm30.structmenu.repository;

import com.tm30.structmenu.strategy.Strategy;

import java.io.*;

public class StrategyRepository {

    static public void save(Strategy strategy) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File("/tmp/" + strategy.getUniqueId())));

            oos.writeObject(strategy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public boolean exists(String id) {

        File file = new File("/tmp/" + id);
        return file.exists();

    }

    static public Strategy getStrategy(String id) {
        Strategy strategy = null;

        try {
            File file = new File("/tmp/" + id);

            if (file.exists()) {
                ObjectInputStream oos = new ObjectInputStream(
                        new FileInputStream(file));

                strategy = (Strategy) oos.readObject();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return strategy;
    }


    static public Boolean delete(Strategy strategy) {

        File f = new File("/tmp/" + strategy.getUniqueId());

        return f.delete();

    }


}
