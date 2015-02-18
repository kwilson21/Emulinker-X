package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.GameStartedEvent;
import org.emulinker.kaillera.model.exception.StartGameException;

public class StartGameAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(StartGameAction.class);
   private static StartGameAction singleton = new StartGameAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static StartGameAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "StartGameAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof StartGame_Request)) {
         throw new FatalActionException("Received incorrect instance of StartGame: " + message);
      } else {
         ++this.actionCount;

         try {
            clientHandler.getUser().startGame();
         } catch (StartGameException var6) {
            StartGameException e = var6;
            log.debug("Failed to start game: " + var6.getMessage());

            try {
               clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), "Error", e.getMessage()));
            } catch (MessageFormatException var5) {
               log.error("Failed to contruct GameChat_Notification message: " + var6.getMessage(), var6);
            }
         }

      }
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameStartedEvent gameStartedEvent = (GameStartedEvent)event;

      try {
         KailleraGame e = gameStartedEvent.getGame();
         clientHandler.getUser().setTempDelay(e.getDelay() - clientHandler.getUser().getDelay());
         int delay;
         if(e.getSameDelay()) {
            delay = e.getDelay();
         } else {
            delay = clientHandler.getUser().getDelay();
         }

         int playerNumber = e.getPlayerNumber(clientHandler.getUser());
         clientHandler.send(new StartGame_Notification(clientHandler.getNextMessageNumber(), (short)delay, (short)((byte)playerNumber), (short)((byte)e.getNumPlayers())));
      } catch (MessageFormatException var7) {
         log.error("Failed to contruct StartGame_Notification message: " + var7.getMessage(), var7);
      }

   }
}
