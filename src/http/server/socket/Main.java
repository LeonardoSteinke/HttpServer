/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server.socket;

/**
 *
 * @author leona
 */
public class Main {

    public static void main(String[] args) throws Exception {
        HTTPServerSocket server = new HTTPServerSocket();
        server.start();

    }
}
