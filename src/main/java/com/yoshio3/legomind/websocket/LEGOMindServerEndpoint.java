/*
 * Copyright 2013 Yoshio Terada
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yoshio3.legomind.websocket;

import com.yoshio3.legomind.websocket.encoders.ClientMessageEncoder;
import com.yoshio3.legomind.websocket.encoders.ClientMessageDecoder;
import com.yoshio3.legomind.websocket.encoders.ClientMessageObject;
import com.yoshio3.legomind.websocket.encoders.TypeOfMachine;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Yoshio Terada
 */
@ServerEndpoint(value = "/lego-server/{machine-type}",
        encoders = {ClientMessageEncoder.class},
        decoders = {ClientMessageDecoder.class})
public class LEGOMindServerEndpoint {

    private final static String MACHINE_TYPE_KEY = "machineType";
    private final static String BROWSER_TYPE = "browser";
    private final static String LEGO_TYPE = "lego";

    @OnOpen
    public void initOpen(Session session, @PathParam("machine-type") String machineType) throws IOException {
        switch (machineType) {
            case BROWSER_TYPE:
                session.getUserProperties().put(MACHINE_TYPE_KEY, BROWSER_TYPE);
                System.out.println("ブラウザからの接続完了");
                break;
            case LEGO_TYPE:
                session.getUserProperties().put(MACHINE_TYPE_KEY, LEGO_TYPE);
                System.out.println("LEGO からの接続完了");
                break;
            default:
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Invalid Machine Type"));
        }

    }

    @OnClose
    public void closeConnection() {
        System.out.println("切断完了");
    }

    /*
     WebSocket のクライアント・エンドポイント(LEGO,ブラウザ)からメッセージを受信した際の処理
     */
    @OnMessage
    public void receivedMessage(ClientMessageObject message, Session session) throws IOException {

        if (message.getMachineType() == TypeOfMachine.LEGO) {
            executeLEGOOperation(message, session);
        } else if (message.getMachineType() == TypeOfMachine.BROWSER) {
            executeBrowserOperation(message, session);
        }
    }

    /*
     LEGO Mindstorms からメッセージを受信した場合の処理
     */
    private void executeLEGOOperation(ClientMessageObject message, Session session) {
        //ブラウザに対してメッセージを配信
        session.getOpenSessions()
                .stream()
                .filter(otherSession
                        -> otherSession.getUserProperties().get(MACHINE_TYPE_KEY).equals(BROWSER_TYPE)
                ).forEach(otherSession -> sendWebSocketMessage(otherSession, message));
    }

    /*
     ブラウザからメッセージを受信した場合の処理
     */
    private void executeBrowserOperation(ClientMessageObject message, Session session) {
        //LEGO に対してメッセージを配信
        session.getOpenSessions()
                .stream()
                .filter(otherSession
                        -> otherSession.getUserProperties().get(MACHINE_TYPE_KEY).equals(LEGO_TYPE)
                ).forEach(otherSession -> sendWebSocketMessage(otherSession, message));
    }

    /*
     指定したセッションの WebSocket エンドポイントにメッセージ配信
     */
    private void sendWebSocketMessage(Session session, ClientMessageObject message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(LEGOMindServerEndpoint.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
