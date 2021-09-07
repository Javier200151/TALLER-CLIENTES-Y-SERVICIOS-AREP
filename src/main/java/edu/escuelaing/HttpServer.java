package edu.escuelaing;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class HttpServer {
    private static final HttpServer _instance = new HttpServer();
    private static final String HTTP_MESSAGE = "HTTP/1.1 200 OK\n"
                                                + "Content-Type: text/html\r\n"
                                                + "\r\n";
    private static final String JAVA_MESSAGE = "HTTP/1.1 200 OK\n"
                                                + "Content-Type: text/javascript\r\n"
                                                + "\r\n";
    private static final String CSS_MESSAGE = "HTTP/1.1 200 OK\n"
                                                + "Content-Type: text/css\r\n"
                                                + "\r\n";
    public static HttpServer getInstance(){
        return _instance;
    }

    private HttpServer(){}

    public void start(String[] args) throws IOException{
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            try {
                serverConnection(clientSocket);
            } catch (URISyntaxException e) {
                System.err.println("URI incorrect.");
                System.exit(1);
            }
        }
        serverSocket.close();
    }

    public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<>();
        String sv="";
        

        while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("GET /app.js")){
                    sv="js";
                }else if(inputLine.contains("GET /index.html")){
                    sv="html";
                }else if(inputLine.contains("GET /style.css")){
                    sv="css";
                }
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        outputLine = getResource( sv);
        out.println(outputLine);
        out.close();
        in.close();
        clientSocket.close();
    }
    public String getResource( String sv) throws URISyntaxException{
        if(sv == "js"){
            return computeJSResponse();
        }else if(sv == "html"){
            return computeHTMLResponse();
        }else if(sv == "css"){
            return computeCSSResponse();
        }
       return null;
    }

    public String computeHTMLResponse(){
        String content = HTTP_MESSAGE;
        System.out.println("HTML");
        File file = new File("arep/src/main/resources/public/index.html");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line =  br.readLine()) != null) content += line; 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public String computeJSResponse(){
        String content = JAVA_MESSAGE;
        System.out.println("JS");
        File file = new File("arep/src/main/resources/public/app.js");
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line =  br.readLine()) != null) content += line; 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        return content;
    }
    public String computeCSSResponse(){
        String content = CSS_MESSAGE;
        System.out.println("CSS");
        File file = new File("arep/src/main/resources/public/style.css");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line =  br.readLine()) != null) content += line; 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String computeDefaultResponse(){
        String outputLine =
                "HTTP/1.1 200 OK\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Title of the document</title>\n"
                        + "</head>"
                        + "<body>"
                        + "My Web Site"
                        + "</body>"
                        + "</html>";
        return outputLine;
    }
    public static void main(String[] args) throws IOException {
        HttpServer.getInstance().start(args);
    }
}
