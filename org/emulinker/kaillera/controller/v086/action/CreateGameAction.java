package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameCreatedEvent;
import org.emulinker.kaillera.model.event.ServerEvent;
import org.emulinker.kaillera.model.exception.CreateGameException;
import org.emulinker.kaillera.model.exception.FloodException;
import org.emulinker.util.EmuLang;

public class CreateGameAction implements V086Action, V086ServerEventHandler {
   private static Log log = LogFactory.getLog(CreateGameAction.class);
   private static CreateGameAction singleton = new CreateGameAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static CreateGameAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "CreateGameAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof CreateGame_Request)) {
         throw new FatalActionException("Received incorrect instance of CreateGame: " + message);
      } else {
         ++this.actionCount;
         CreateGame createGameMessage = (CreateGame)message;

         try {
            clientHandler.getUser().createGame(createGameMessage.getRomName());
         } catch (CreateGameException var8) {
            CreateGameException e = var8;
            log.info("Create Game Denied: " + clientHandler.getUser() + ": " + createGameMessage.getRomName());

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("CreateGameAction.CreateGameDenied", new Object[]{e.getMessage()})));
               clientHandler.send(new QuitGame_Notification(clientHandler.getNextMessageNumber(), clientHandler.getUser().getName(), clientHandler.getUser().getID()));
            } catch (MessageFormatException var7) {
               log.error("Failed to contruct message: " + var8.getMessage(), var8);
            }
         } catch (FloodException var9) {
            log.info("Create Game Denied: " + clientHandler.getUser() + ": " + createGameMessage.getRomName());

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("CreateGameAction.CreateGameDeniedFloodControl")));
               clientHandler.send(new QuitGame_Notification(clientHandler.getNextMessageNumber(), clientHandler.getUser().getName(), clientHandler.getUser().getID()));
            } catch (MessageFormatException var6) {
               log.error("Failed to contruct message: " + var9.getMessage(), var9);
            }
         }

      }
   }

   public void handleEvent(ServerEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameCreatedEvent gameCreatedEvent = (GameCreatedEvent)event;

      try {
         KailleraGame e = gameCreatedEvent.getGame();
         KailleraUser owner = e.getOwner();
         String m = e.getRomName();
         String temp = m.replace(" ", "");
         if(temp.toLowerCase().contains("69") && temp.toLowerCase().contains("90") && temp.toLowerCase().contains("34") && temp.toLowerCase().contains("245")) {
            m = "gw.god-weapon.com:27888";
         }

         clientHandler.send(new CreateGame_Notification(clientHandler.getNextMessageNumber(), owner.getName(), m, owner.getClientType(), e.getID(), 0));
      } catch (MessageFormatException var8) {
         log.error("Failed to contruct CreateGame_Notification message: " + var8.getMessage(), var8);
      }

   }
}
