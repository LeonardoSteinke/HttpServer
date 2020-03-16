package http.server.socket;

import http.server.socket.Requests.NotFound;
import http.server.socket.Requests.Teapot;
import http.server.socket.Requests.ResponseStatus;
import http.server.socket.Requests.OK;
import http.server.socket.Requests.BadRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author leona
 */
public class HTTPServerSocket {

    static final File ROOT = new File(".");
    static ServerSocket server;
    private final int PORT = 80;

    ResponseStatus res;

    OutputStream out;
    BufferedReader input;

    String[] getDir;

    private String method;
    private String dir;
    private String requestBody;
    private String[] dataPOST;

    public HTTPServerSocket() throws Exception {
        server = new ServerSocket(PORT);
        server.setReuseAddress(true);

    }

    public void start() throws Exception {

        while (true) {
            System.out.println("Aguardando conexão...");
            try ( Socket conn = server.accept();) {
                try {
                    System.out.println("Conectado com: " + conn.getInetAddress().getHostAddress());
                    input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    out = conn.getOutputStream();
                    String res = input.readLine();
                    getDir = res.split(" ");

                    method = getDir[0];
                    dir = getDir[1];
                    System.out.println("Diretório: " + dir);
                    System.out.println("-----------------------------------------");
                    if (!method.equals("GET") && !method.equals("HEAD")) {
                        doPost();
                    } else {
                        doGet();
                    }
                } catch (Exception e) {
                    conn.close();
                }
                out.flush();
                conn.close();
            }
        }
    }

    private void doPost() throws IOException {
        String[] lineHeader;
        int contentLength = 0;
        System.out.println("Requisição POST: ");
        //leio o header
        String linha = input.readLine();
        while (!linha.isEmpty()) {
            lineHeader = linha.split(" ");
            //salvo o Content-length
            if (lineHeader[0].equals("Content-Length:")) {
                contentLength = Integer.parseInt(lineHeader[1]);
            }
            linha = input.readLine();
        }
        //leio o conteudo
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            b.append((char) input.read());
        }

        requestBody = b.toString();

        dataPOST = getDados(requestBody.split("&"));
        for (int i = 0; i < dataPOST.length; i++) {
            System.out.println(dataPOST[i]);
        }

        File fileDirectory = new File("./public_html/" + dataPOST[0]);

        //resposta do post
        if (fileDirectory.exists()) {

            res = new OK();
            writeTxt(dataPOST[0]);
            File file = new File(ROOT, "/public_html/enviado.html");
            res.printFile(file, out);

        } else if (dataPOST[0].equals("teapot")) {

            res = new Teapot();
            File file = new File(ROOT, "/public_html/responsestatus/teapot.png");
            res.printFile(file, out);
        } else {

            res = new BadRequest();
            File file = new File(ROOT, "/public_html/responsestatus/400.html");
            res.printFile(file, out);

        }

    }

    private void doGet() throws IOException {

        if (this.dir.equals("/")) {
            dir += Caminhos.INDEX.toString();
            dir = Caminhos.ROOT + dir;
        } else {
            dir = Caminhos.ROOT + dir;
        }

        File file = new File(ROOT, dir);

        if (!file.exists()) {
            res = new NotFound();
            File filePrint = new File(ROOT, "/public_html/responsestatus/404.html");
            res.printFile(filePrint, out);

        } else {

            try {
                res = new OK();
                res.printFile(file, out);
            } catch (Exception e) {
                if (file.isDirectory()) {
                    if (file.toString().endsWith("responsestatus")) {

                        res = new BadRequest();
                        File filePrint = new File(ROOT, "/public_html/responsestatus/403.html");
                        res.printFile(filePrint, out);

                    } else {

                        contentList(file, res.getContentType(file.toString()));
                    }
                }
            }
        }
    }

    private void contentList(File file, String content) throws IOException {
        String[] files = file.list();
        boolean a = false;
        if (files.length == 1) {
            res = new OK();
            dir += "/" + files[0];
            file = new File(ROOT, dir);
            res.printFile(file, out);
        } else {
            for (String file1 : files) {
                if (file1.equalsIgnoreCase("index.html")) {
                    res = new OK();
                    dir += "/" + files[0];
                    file = new File(ROOT, dir);
                    res.printFile(file, out);
                    a = true;
                }
            }
            if (a == false) {
                for (String file1 : files) {
                    out.write(("<a href='" + getDir[1] + "/" + file1 + "'>" + "<h2>" + file1 + "</h2>" + "</a>").getBytes());
                }

            }
        }
    }

    private String[] getDados(String[] array) {
        String[] dados = new String[array.length];
        boolean gravar = false;
        StringBuilder b;
        for (int i = 0; i < array.length; i++) {
            b = new StringBuilder();
            for (int j = 0; j < array[i].length(); j++) {
                if (gravar) {
                    b.append(array[i].charAt(j));
                }
                if (array[i].charAt(j) == '=') {
                    gravar = true;
                }
            }
            gravar = false;
            dados[i] = b.toString();
        }
        return dados;
    }

    private void writeTxt(String directory) {

        File arq = new File("./public_html/" + directory + "/" + directory + ".txt");
        try {
            if (!arq.exists()) {
                arq.createNewFile();//arquivo criado
            }
            FileWriter fileW = new FileWriter(arq, true);//arquivo para escrita
            try ( BufferedWriter buffW = new BufferedWriter(fileW)) {
                for (int i = 1; i < dataPOST.length; i++) {
                    buffW.write(dataPOST[i] + ";");
                }

                buffW.write("\n");
            }
        } catch (IOException io) {

        }
    }

}
