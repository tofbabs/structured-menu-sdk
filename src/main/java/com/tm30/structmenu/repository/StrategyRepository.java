package com.tm30.structmenu.repository;

import com.tm30.structmenu.strategy.Strategy;

import java.io.*;
import java.util.Optional;

public class StrategyRepository {

    static public void save(Strategy strategy) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File("/tmp/" + strategy.getSlug().replace("//s+", "_").toLowerCase()))
            );

            oos.writeObject(strategy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void saveById(Strategy strategy) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File("/tmp/" + strategy.getUniqueId())));

            oos.writeObject(strategy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public boolean exists(String strategySlug) {
        File fileName = new File("/tmp/" + strategySlug);
        return fileName.exists();
    }

    static public Strategy getStrategy(String strategySlug) {
        Strategy strategy = null;

        try {
            File file = new File("/tmp/" + strategySlug);

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

        if (Optional.ofNullable(strategy).isPresent()) return false;

        File f = new File("/tmp/" + strategy.getUniqueId());

        return f.delete();

    }


    public static Strategy getStrategyById(String strategyId) {

        Strategy strategy = null;

        try {
            File file = new File("/tmp/" + strategyId);

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
}
