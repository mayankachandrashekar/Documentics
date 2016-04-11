package com.documentics.machinelearning.namedentity;

import java.io.*;

/**
 * Created by Mayanka on 10-Apr-16.
 */
public class CreatingTraining {
    public static void main(String[] args){
        try {
            BufferedWriter bw= new BufferedWriter(new FileWriter(new File("university.txt")));
            BufferedReader br=new BufferedReader(new FileReader(new File("data\\university_database.csv")));
            String d;
            while((d=br.readLine())!=null)
            {
               String data[]= d.split(",");
                if(data[1].equals("Institution_Name"))
                {

                }
                else
                {
                    bw.write("<START:university> "+data[1]+" <END>\n");
                }

            }
            bw.close();
            br.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
