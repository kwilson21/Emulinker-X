package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.UserQuitGameEvent;
import org.emulinker.kaillera.model.exception.CloseGameException;
import org.emulinker.kaillera.model.exception.DropGameException;
import org.emulinker.kaillera.model.exception.QuitGameException;

public class QuitGameAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(QuitGameAction.class);
   private static QuitGameAction singleton = new QuitGameAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static QuitGameAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "QuitGameAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof QuitGame_Request)) {
         throw new FatalActionException("Received incorrect instance of QuitGame: " + message);
      } else {
         ++this.actionCount;

         try {
            clientHandler.getUser().quitGame();
         } catch (DropGameException var5) {
            log.debug("Failed to drop game: " + var5.getMessage());
         } catch (QuitGameException var6) {
            log.debug("Failed to quit game: " + var6.getMessage());
         } catch (CloseGameException var7) {
            log.debug("Failed to close game: " + var7.getMessage());
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var4) {
            log.error("Sleep Interrupted!", var4);
         }

      }
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      UserQuitGameEvent userQuitEvent = (UserQuitGameEvent)event;
      KailleraUser thisUser = clientHandler.getUser();

      try {
         KailleraUser e = userQuitEvent.getUser();
         if(!e.getStealth()) {
            clientHandler.send(new QuitGame_Notification(clientHandler.getNextMessageNumber(), e.getName(), e.getID()));
         }

         if(thisUser == e && e.getStealth()) {
            clientHandler.send(new QuitGame_Notification(clientHandler.getNextMessageNumber(), e.getName(), e.getID()));
         }
      } catch (MessageFormatException var6) {
         log.error("Failed to contruct QuitGame_Notification message: " + var6.getMessage(), var6);
      }

   }
}
