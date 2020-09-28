package com.kiss.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @ClassName RunShell
 * @Description TODO
 * @Author kiss
 * @Date 2020/9/11 10:52
 * @Version 1.0
 */
public class RunShell
{
    public static String exec(String command) throws InterruptedException {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            System.err.println("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            Logger.getLogger(RunShell.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnString;
    }


}
