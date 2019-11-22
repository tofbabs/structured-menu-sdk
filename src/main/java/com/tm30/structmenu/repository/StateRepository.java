package com.tm30.structmenu.repository;

import com.tm30.structmenu.context.State;

import java.io.*;
import java.util.Optional;

public class StateRepository {

    static public void save(State state) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    // By using "FileOutputStream" we will
                    // Write it to a File in the file system
                    // It could have been a Socket to another
                    // machine, a database, an in memory array, etc.
                    new FileOutputStream(new File("/tmp/" + state.getUserId())));

            oos.writeObject(state);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public State getMostRecentState(String userId) {
        State state = null;

        try {
            File file = new File("/tmp/" + userId);
            if (file.exists()) {
                ObjectInputStream oos = new ObjectInputStream(
                        new FileInputStream(file));

                state = (State) oos.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return state;
    }


    static public Boolean delete(State state) {

        if(! Optional.ofNullable(state).isPresent()) return false;

        try {
            File file = new File("/tmp/" + state.getUserId());
            if (file.exists()) return file.delete();
        } catch (Exception e) {

        }

        return false;

    }


}
