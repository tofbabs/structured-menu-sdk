package com.tm30.structmenu.repository;

import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.strategy.Strategy;

import java.io.*;

public class InputRepository {

    static public void save(Input input) {

        String fileName = "/tmp/" + input.getRequest().getRecipient() + input.getParamKey();

        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = input.getParamValue().getBytes();
            outputStream.write(strToBytes);

            outputStream.close();

        } catch (IOException e) {
//            e.printStackTrace();
        }

    }

    static public boolean exists(Input input) {

        String fileName = "/tmp/" + input.getRequest().getRecipient() + input.getParamKey();
        File file = new File(fileName);
        return file.exists();

    }

    static public String getUserInput(String phone, String paramKey) {
        String fileName = "/tmp/" + phone + paramKey;

        // How to read file into String before Java 7
        InputStream is = null;
        StringBuilder sb = new StringBuilder();

        try {
            is = new FileInputStream(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();

            while(line != null){
                sb.append(line);
                line = buf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    static public Boolean delete(Input input) {

        try {
            String fileName = "/tmp/" + input.getRequest().getRecipient() + input.getParamKey();
            File f = new File(fileName);

            if (f.delete())
                return true;
        } catch (NullPointerException e){
//            e.printStackTrace();
        }

        return false;

    }


}
