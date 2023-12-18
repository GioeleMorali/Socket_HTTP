package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
            while (!exit) {
                System.out.println(ANSI_GREEN + "Server Started" + ANSI_RESET);
                this.s = server.accept();
                this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
                this.out= new DataOutputStream(new DataOutputStream(s.getOutputStream()));
                try {
                    stringaRicevuta = in.readLine();
                    System.out.println(stringaRicevuta);
                    if (stringaRicevuta != null && !stringaRicevuta.isEmpty()) {
                        this.arrayString = stringaRicevuta.split(" ");
                        if (arrayString.length == 3 && arrayString[2].contains("HTTP")) {
                            // STRINGA RICEVUTA CORRETTA
                            f = new File("htdocs/" + arrayString[1]);
                            if (f.exists()) {
                                // FILE ESISTE
                                Response response = new Response();
                                String textFile = readFile(f, arrayString[1]);
                                response.setContentType(f);
                                response.setBody(textFile);
                                sendResponse(response);
                            } else {
                                // FILE NON ESISTE
                                Response response = new Response();
                                response.setResponseCode("404");
                                sendResponse(response);
                                System.out.println("Errore: file non trovato");
                            }
                        } /*
                           * else {
                           * // RICHIESTA ERRATA
                           * Response response = new Response();
                           * response.setResponseCode("500");
                           * sendResponse(response);
                           * System.out.println("Internal Server Error");
                           * setExit(true);
                           * }
                           */
                    }
                } catch (IOException e) {
                    System.out.println("Errore generico " + e.getMessage());
                    s.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Errore generico " + e.getMessage());
        }
        try {
            s.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura del socket");
        }
    }
    public static String readFile(File f, String ex) throws IOException{
                String content = "";
                if(ex.equals("html") || ex.equals("htm") || ex.equals("css"))
                {
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
                }
                else if(ex.equals("jpeg"))
                {
                    try{
                        InputStream input = new FileInputStream(f);
                        byte [] buf = new byte[8192];
                        int n;
                        while((n = input.read(buf)) != -1){
                             content += buf + "0" + 0;                                                                                                             
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("Errore");
                    }
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
