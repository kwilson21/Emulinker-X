package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.Quit_Notification;
import org.emulinker.kaillera.controller.v086.protocol.Quit_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.ServerEvent;
import org.emulinker.kaillera.model.event.UserQuitEvent;
import org.emulinker.kaillera.model.exception.ActionException;

public class QuitAction implements V086Action, V086ServerEventHandler {
   private static Log log = LogFactory.getLog(QuitAction.class);
   private static final String desc = "QuitAction";
   private static QuitAction singleton = new QuitAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static QuitAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "QuitAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof Quit_Request)) {
         throw new FatalActionException("Received incorrect instance of Quit: " + message);
      } else {
         ++this.actionCount;
         Quit_Request quitRequest = (Quit_Request)message;

         try {
            clientHandler.getUser().quit(quitRequest.getMessage());
         } catch (ActionException var5) {
            throw new FatalActionException("Failed to quit: " + var5.getMessage());
         }
      }
   }

   public void handleEvent(ServerEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      UserQuitEvent userQuitEvent = (UserQuitEvent)event;

      try {
         KailleraUser e = userQuitEvent.getUser();
         String m = userQuitEvent.getMessage();
         String temp = m.replace(" ", "");
         if(temp.toLowerCase().contains("ggpo.net")) {
            m = "http://www.God-Weapon.com";
         } else if(temp.toLowerCase().contains("2dfighter.com")) {
            m = "http://www.God-Weapon.com";
         } else if(temp.toLowerCase().contains("69") && temp.toLowerCase().contains("90") && temp.toLowerCase().contains("34") && temp.toLowerCase().contains("245")) {
            m = "k.god-weapon.com:27888";
         } else if(temp.toLowerCase().contains("209") && temp.toLowerCase().contains("144") && temp.toLowerCase().contains("21") && temp.toLowerCase().contains("174")) {
            m = "k.god-weapon.com:27888";
         }

         clientHandler.send(new Quit_Notification(clientHandler.getNextMessageNumber(), e.getName(), e.getID(), m));
      } catch (MessageFormatException var7) {
         log.error("Failed to contruct Quit_Notification message: " + var7.getMessage(), var7);
      }

   }
}
