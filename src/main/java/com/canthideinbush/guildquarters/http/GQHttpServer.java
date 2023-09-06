package com.canthideinbush.guildquarters.http;

import com.canthideinbush.guildquarters.utils.Utils;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;

public class GQHttpServer {

    private final HttpsServer server;

    public org.slf4j.Logger logger;

    public GQHttpServer() throws UnrecoverableKeyException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException, CertificateException {
        this(null);
    }

    public static GQHttpServer instance;


    public InetSocketAddress address = new InetSocketAddress("localhost", 15535);


    public FileHttpHandler fileHandler;

    public GQHttpServer(Logger logger) throws UnrecoverableKeyException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException, CertificateException {
        try {
            server = HttpsServer.create(address, 0);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        if (logger == null) {
            this.logger = LoggerFactory.getLogger(this.getClass());

        }
        else this.logger = logger;
        initialize();
        instance = this;
    }

    private void initialize() throws UnrecoverableKeyException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException, CertificateException {

        initializeSSL();

        fileHandler = new FileHttpHandler(server);

        hostFiles();


        server.createContext("/editor", new QuarterEditorHandler(this));
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        logger.info("Server started on port 15535");
    }

    private void hostFiles() throws URISyntaxException {
        File css = new File(Utils.getResource("https/css").toURI());
        File images = new File(Utils.getResource("https/images").toURI());
        File js = new File(Utils.getResource("https/js").toURI());
        hostAll(css, css);
        hostAll(images, images);
        hostAll(js, js);
    }

    private void hostAll(File parent, File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                hostAll(parent, f);
            }
        }
        else {
            fileHandler.addFile(file.getAbsolutePath().substring(parent.toString().length() - parent.getName().length()));
        }
    }




    private void initializeSSL() throws IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, URISyntaxException, CertificateException {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        char[] pass = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");

        //In case key is not found use this key



        FileInputStream fis = new FileInputStream(new File(Utils.getResource("https\\key.jks").toURI()));
        ks.load(fis, pass);


        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, pass);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                SSLEngine engine = sslContext.createSSLEngine();
                params.setNeedClientAuth(false);
                params.setCipherSuites(engine.getEnabledCipherSuites());
                params.setProtocols(engine.getEnabledProtocols());
                SSLParameters parameters = sslContext.getSupportedSSLParameters();
                params.setSSLParameters(parameters);
            }


        });
    }

    public static void main(String[] args) throws UnrecoverableKeyException, IOException, KeyStoreException, NoSuchAlgorithmException, URISyntaxException, KeyManagementException, CertificateException {
        Utils.testing = true;
        new GQHttpServer();
    }



}
