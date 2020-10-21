package org.WhatIsTheBpm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.WhatIsTheBpm.Controllers.SceneController;
import org.WhatIsTheBpm.KeyHandling.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;

import java.io.File;
import java.net.http.HttpClient;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;


/**
 * JavaFX App
 */
public class App extends Application {

    private SceneController controller;

    @Override
    public void start(Stage stage) throws IOException {

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        controller = new SceneController();

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalKeyListener listener = new GlobalKeyListener();
        GlobalScreen.addNativeKeyListener(listener);

        listener.addController(controller);



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("Screen.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                return controller;
            }
        });

        try2();

        AnchorPane root = (AnchorPane) loader.load();

        stage.setTitle("BPM");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void try2() {
        try {
            File file = new File("a");
            String path = file.getAbsolutePath();
            int length = path.length();
            String rpath = path.substring(0,length-2);
            String executablePath = rpath + "\\GifToTheBeatDataProvider.exe";
            Runtime runTime = Runtime.getRuntime();
            //String executablePath = absolutePath + "/GifToTheBeatOsuDataFeed.exe";
            Process process = runTime.exec(executablePath);
            CountDownLatch latch = new CountDownLatch(1);
            WebSocketClient client = new WebSocketClient(latch);
            WebSocket ws = HttpClient
                    .newHttpClient()
                    .newWebSocketBuilder()
                    .buildAsync(URI.create("ws://localhost:7270/GifToTheBeatOsuDataFeed"), client)
                    .join();
            client.addEventHandler(new OsuEventHandler(controller));
            latch.await();
            //websocket handle = new websocket("ws://localhost:7270/GifToTheBeatOsuDataFeed");
            //handle.connect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private class WebSocketClient implements WebSocket.Listener {
        private final CountDownLatch latch;
        private OsuEventHandler handler;

        public WebSocketClient(CountDownLatch latch) { this.latch = latch; }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            //System.out.println("onText received " + data);
            JSONObject obj;
            try {
                obj = new JSONObject(data.toString());
                String status = obj.getString("status");
                int mapTime = obj.getInt("mapTime");
                String isoTime = obj.getString("isoTime");
                double bpmMultiplier = obj.getDouble("bpmMultiplier");
                String relativeOsuFilePath = obj.getString("relativeOsuFilePath");
                OsuEvent event = new OsuEvent(status,mapTime,isoTime,bpmMultiplier,relativeOsuFilePath);
                handler.parse(event);
            } catch(Exception e) {
                latch.countDown();
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }
            latch.countDown();
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("Bad day! " + webSocket.toString());
            error.printStackTrace();
            try2();
            WebSocket.Listener.super.onError(webSocket, error);
        }

        public void addEventHandler(OsuEventHandler eventParser) {
            handler = eventParser;
        }
    }

}