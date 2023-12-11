package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerActions {
    String arrayString [];
    DataOutputStream out;
    String stringaRicevuta;
    File f;
    
    ServerSocket server;
    BufferedReader in;
    Socket s;
    public ServerActions (){
        try {
            server = new ServerSocket(8080);
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        Boolean exit = false;
        String ANSI_GREEN = "\033[1;32m";
        String ANSI_RESET = "\u001B[0m";
    try {
         System.out.println(ANSI_GREEN + "Server started" + ANSI_RESET);
         s = server.accept();
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());
            while (!exit) {
                stringaRicevuta = in.readLine();
                System.out.println(stringaRicevuta);
                if (stringaRicevuta.isEmpty()) {
                    break;
                }
                else
                {
                    arrayString = stringaRicevuta.split(" ");
                    if (arrayString.length == 3 && arrayString[2].equals("HTTP/1.1")) {
                        f = new File ("." + arrayString[1]);
                        if (f.exists()) {
                            Response send = new Response();
                            String stringa = readFile(f);
                            send.setBody(stringa);
                            sendResponse(send);
                            exit=true;
                        }
                        else
                        {
                            Response send = new Response();
                            send.setResponseCode("404");
                            sendResponse(send);
                            System.out.println("Errore: file non trovato");
                            exit=true;
                        }
                    }
                    else
                    {
                        Response send = new Response();
                            send.setCode("500");
                            sendResponse(send);
                        System.out.println("Internal Server Error");
                        exit=true;
                    }
                }
            }
            
    } catch (Exception e) {
        System.out.println("Errore generico");
    }

    close();
    }
    public static String readFile(File f){
                String content = "";
                try {
                   Scanner myReader = new Scanner(f);
                   while(myReader.hasNextLine()){
                    String data = myReader.nextLine();
                    content +=data;
                    System.out.println(data);
                   }
                   myReader.close();
                } catch (Exception e) {
                   System.out.println("Errore");
                }
                return content;
    }
    public void sendResponse(Response r)
    {
        ArrayList<String> d = r.getData();
        for(int i = 0; i < d.size(); i++)
        {
            try {
                System.out.println(d.get(i));
                out.writeBytes(d.get(i));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Errore risposta");
            }
        }
    }
    public void close()
    {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
