package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.protocol.GameKick;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.exception.GameKickException;

public class GameKickAction implements V086Action {
   private static Log log = LogFactory.getLog(GameKickAction.class);
   private static GameKickAction singleton = new GameKickAction();
   private int actionCount = 0;

   public static GameKickAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public String toString() {
      return "GameKickAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      ++this.actionCount;
      GameKick kickRequest = (GameKick)message;

      try {
         clientHandler.getUser().gameKick(kickRequest.getUserID());
      } catch (GameKickException var5) {
         log.debug("Failed to kick: " + var5.getMessage());
      }

   }
}
