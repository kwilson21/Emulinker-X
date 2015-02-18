package org.emulinker.kaillera.controller.v086.action;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.PlayerInformation;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.UserJoinedGameEvent;
import org.emulinker.kaillera.model.exception.JoinGameException;
import org.emulinker.util.EmuLang;

public class JoinGameAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(JoinGameAction.class);
   private static JoinGameAction singleton = new JoinGameAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static JoinGameAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "JoinGameAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof JoinGame_Request)) {
         throw new FatalActionException("Received incorrect instance of JoinGame: " + message);
      } else {
         ++this.actionCount;
         JoinGame_Request joinGameRequest = (JoinGame_Request)message;

         try {
            clientHandler.getUser().joinGame(joinGameRequest.getGameID());
         } catch (JoinGameException var7) {
            JoinGameException e = var7;

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("JoinGameAction.JoinGameDenied") + e.getMessage()));
               clientHandler.send(new QuitGame_Notification(clientHandler.getNextMessageNumber(), clientHandler.getUser().getName(), clientHandler.getUser().getID()));
            } catch (MessageFormatException var6) {
               log.error("Failed to contruct new Message", var7);
            }
         }

      }
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      UserJoinedGameEvent userJoinedEvent = (UserJoinedGameEvent)event;
      KailleraUser thisUser = clientHandler.getUser();

      try {
         KailleraGame e = userJoinedEvent.getGame();
         KailleraUser user = userJoinedEvent.getUser();
         if(user.equals(thisUser)) {
            ArrayList players = new ArrayList();
            Iterator i$ = e.getPlayers().iterator();

            while(i$.hasNext()) {
               KailleraUser player = (KailleraUser)i$.next();
               if(!player.equals(thisUser) && !player.getStealth()) {
                  players.add(new PlayerInformation.Player(player.getName(), (long)player.getPing(), player.getID(), player.getConnectionType()));
               }
            }

            clientHandler.send(new PlayerInformation(clientHandler.getNextMessageNumber(), players));
         }

         if(!user.getStealth()) {
            clientHandler.send(new JoinGame_Notification(clientHandler.getNextMessageNumber(), e.getID(), 0, user.getName(), (long)user.getPing(), user.getID(), user.getConnectionType()));
         }
      } catch (MessageFormatException var10) {
         log.error("Failed to contruct JoinGame_Notification message: " + var10.getMessage(), var10);
      }

   }
}
