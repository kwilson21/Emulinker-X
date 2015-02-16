package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086UserEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.model.event.InfoMessageEvent;
import org.emulinker.kaillera.model.event.UserEvent;

public class InfoMessageAction implements V086UserEventHandler {
   private static Log log = LogFactory.getLog(InfoMessageAction.class);
   private static final String desc = "InfoMessageAction";
   private static InfoMessageAction singleton = new InfoMessageAction();
   private int handledCount = 0;

   public static InfoMessageAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "InfoMessageAction";
   }

   public void handleEvent(UserEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      InfoMessageEvent infoEvent = (InfoMessageEvent)event;

      try {
         clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", infoEvent.getMessage()));
      } catch (MessageFormatException var5) {
         log.error("Failed to contruct InformationMessage message: " + var5.getMessage(), var5);
      }

   }
}
