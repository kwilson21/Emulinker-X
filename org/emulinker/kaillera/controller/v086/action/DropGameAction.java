package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop_Notification;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.UserDroppedGameEvent;
import org.emulinker.kaillera.model.exception.DropGameException;

public class DropGameAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(DropGameAction.class);
   private static DropGameAction singleton = new DropGameAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static DropGameAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "DropGameAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof PlayerDrop_Request)) {
         throw new FatalActionException("Received incorrect instance of PlayerDrop: " + message);
      } else {
         ++this.actionCount;

         try {
            clientHandler.getUser().dropGame();
         } catch (DropGameException var4) {
            log.debug("Failed to drop game: " + var4.getMessage());
         }

      }
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      UserDroppedGameEvent userDroppedEvent = (UserDroppedGameEvent)event;

      try {
         KailleraUser e = userDroppedEvent.getUser();
         int playerNumber = userDroppedEvent.getPlayerNumber();
         if(!e.getStealth()) {
            clientHandler.send(new PlayerDrop_Notification(clientHandler.getNextMessageNumber(), e.getName(), (byte)playerNumber));
         }
      } catch (MessageFormatException var6) {
         log.error("Failed to contruct PlayerDrop_Notification message: " + var6.getMessage(), var6);
      }

   }
}
