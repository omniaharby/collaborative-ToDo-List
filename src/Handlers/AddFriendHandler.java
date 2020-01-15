/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handlers;

import Connection.SocketHandler;
import ControllerBase.ActionHandler;
import DAOController.FriendRequestController;
import Entities.EntityWrapper;
import Entities.FriendRequestEntity;
import com.google.gson.Gson;
import java.io.PrintStream;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ahmedpro
 */
public class AddFriendHandler implements ActionHandler {

    @Override
    public void handleAction(String requestJsonObject, PrintStream printStream) {
        Gson gson = new Gson();
        FriendRequestController frc = new FriendRequestController();
        try {

            // Create json object
            JSONObject jsonObject = new JSONObject(requestJsonObject);

            // Get request entity string from json.
            String requestEntityJson = jsonObject.getJSONObject("entity").toString();
            FriendRequestEntity requestEntity = gson.fromJson(requestEntityJson, FriendRequestEntity.class);

            // Insert request into friend request table. The result may be true
            // or false if it true then the request is inserted, else the request
            // is already exist
            boolean result = frc.insert(requestEntity);
            if (result) {
                int receivedUserId = requestEntity.getReceivedUserId();
                int sentUserId = requestEntity.getSentUserId();

                // Check if the receiving user is online
                boolean isOnline = false;
                SocketHandler friendSocket = null;
                for (int i = 0; i < SocketHandler.getOnlineIds().size(); i++) {
                    if (receivedUserId == SocketHandler.getOnlineIds().get(i)) {
                        isOnline = true;
                        friendSocket = SocketHandler.socketHandlers.get(i);
                        break;
                    }
                }

                // If the friend is online send notification
                if (isOnline) {
                    // send a notification to the online user
                    EntityWrapper entityWrapper = new EntityWrapper("addFriend", "UserEntity", requestEntity);
                    String entityWrapperJson = gson.toJson(entityWrapper);
                    friendSocket.printResponse(entityWrapperJson);
                }

            } else {
                // Request is already sent
                requestEntity.setId(-1);
                EntityWrapper entityWrapper = 
                        new EntityWrapper("addFriend", "FriendRequestEntity", requestEntity);
                String entityWrapperJson = gson.toJson(entityWrapper);
                printStream.println(entityWrapperJson);
            }
            
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

}
