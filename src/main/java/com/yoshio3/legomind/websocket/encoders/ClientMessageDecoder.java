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
package com.yoshio3.legomind.websocket.encoders;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author Yoshio Terada
 */
public class ClientMessageDecoder implements Decoder.Text<ClientMessageObject> {

    @Override
    public ClientMessageObject decode(String jsonMessage) throws DecodeException {
        // ["machineType":"LEGO", "order":"ORDER_NUM","message":"MESSAGE"]

        ClientMessageObject returnObject = new ClientMessageObject();

        try (StringReader reader = new StringReader(jsonMessage);
                JsonReader jReader = Json.createReader(reader)) {
            JsonObject jObj = jReader.readObject();
            if (jObj.containsKey("machineType")) {
                String machineType = jObj.getString("machineType");
                switch (machineType) {
                    case "LEGO":
                        returnObject.setMachineType(TypeOfMachine.LEGO);
                        break;
                    case "BROWSER":
                        returnObject.setMachineType(TypeOfMachine.BROWSER);
                        break;
                }
            }
            if (jObj.containsKey("order")) {
                String order = jObj.getString("order");
                switch (order) {
                    //FORWARD, BACKWARD, LEFTTURN, RIGHTTURN, STOP, SPPEDUP, SPEEDDOWN
                    case "LEGO_FORWARD":
                        returnObject.setOrder(ClientOrderString.LEGO_FORWARD);
                        break;
                    case "LEGO_BACKWARD":
                        returnObject.setOrder(ClientOrderString.LEGO_BACKWARD);
                        break;
                    case "LEGO_LEFTTURN":
                        returnObject.setOrder(ClientOrderString.LEGO_LEFTTURN);
                        break;
                    case "LEGO_RIGHTTURN":
                        returnObject.setOrder(ClientOrderString.LEGO_RIGHTTURN);
                        break;
                    case "LEGO_STOP":
                        returnObject.setOrder(ClientOrderString.LEGO_STOP);
                        break;
                    case "LEGO_SPPEDUP":
                        returnObject.setOrder(ClientOrderString.LEGO_SPPEDUP);
                        break;
                    case "LEGO_SPEEDDOWN":
                        returnObject.setOrder(ClientOrderString.LEGO_SPEEDDOWN);
                        break;
                    case "BRWS_ERROR":
                        returnObject.setOrder(ClientOrderString.BRWS_ERROR);
                        break;
                    case "BRWS_INFO":
                        returnObject.setOrder(ClientOrderString.BRWS_INFO);
                        break;
                    case "BRWS_WARN":
                        returnObject.setOrder(ClientOrderString.BRWS_WARN);
                        break;
                }
            }
            if (jObj.containsKey("message")) {
                String message = jObj.getString("message");
                returnObject.setMessage(message);
            }
        }
        return returnObject;
    }

    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

    @Override
    public void init(EndpointConfig ec) {
        ;
    }

    @Override
    public void destroy() {
        ;
    }
}
