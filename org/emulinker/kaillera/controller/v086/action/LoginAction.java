package org.emulinker.kaillera.controller.v086.action;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.ServerACK;
import org.emulinker.kaillera.controller.v086.protocol.UserInformation;
import org.emulinker.kaillera.controller.v086.protocol.UserJoined;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.ServerEvent;
import org.emulinker.kaillera.model.event.UserJoinedEvent;
import org.emulinker.kaillera.model.impl.KailleraUserImpl;

public class LoginAction implements V086Action, V086ServerEventHandler {
   private static Log log = LogFactory.getLog(LoginAction.class);
   private static LoginAction singleton = new LoginAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static LoginAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "LoginAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      ++this.actionCount;
      UserInformation userInfo = (UserInformation)message;
      KailleraUser user = clientHandler.getUser();
      if(user.isLoggedIn()) {
         Iterator m1 = user.getServer().getUsers().iterator();

         while(m1.hasNext()) {
            KailleraUser temp1 = (KailleraUser)m1.next();
            if(temp1.getConnectSocketAddress().getAddress().getHostAddress().equals(user.getConnectSocketAddress().getAddress().getHostAddress())) {
               try {
                  temp1.quit("Forceful Exit: Dupe");
               } catch (Exception var8) {
                  ;
               }
            }
         }

         throw new IllegalArgumentException("Forceful Exit: Duplicate = " + user.getName() + ", " + user.getSocketAddress().getAddress().getHostAddress());
      } else {
         String m = userInfo.getUserName();
         String temp = m.replace(" ", "");
         if(temp.toLowerCase().contains("ggpo.net")) {
            m = "http://www.Galaxy64.com  ";
         } else if(temp.toLowerCase().contains("2dfighter.com")) {
            m = "http://www.Galaxy64.com  ";
         } else if(temp.toLowerCase().contains("72") && temp.toLowerCase().contains("5.") && temp.toLowerCase().contains("70") && temp.toLowerCase().contains("152")) {
            m = "GODWEAPON IS FOR FAGS.";
         }

         user.setName(m);
         user.setClientType(userInfo.getClientType());
         user.setSocketAddress(clientHandler.getRemoteSocketAddress());
         user.setConnectionType(userInfo.getConnectionType());
         clientHandler.startSpeedTest();

         try {
            clientHandler.send(new ServerACK(clientHandler.getNextMessageNumber()));
         } catch (MessageFormatException var9) {
            log.error("Failed to contruct ServerACK message: " + var9.getMessage(), var9);
         }

      }
   }

   public void handleEvent(ServerEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      UserJoinedEvent userJoinedEvent = (UserJoinedEvent)event;

      try {
         KailleraUserImpl e = (KailleraUserImpl)userJoinedEvent.getUser();
         clientHandler.send(new UserJoined(clientHandler.getNextMessageNumber(), e.getName(), e.getID(), (long)e.getPing(), e.getConnectionType()));
         KailleraUserImpl thisUser = (KailleraUserImpl)clientHandler.getUser();
         if(thisUser.isEmuLinkerClient() && thisUser.getAccess() >= 4 && !e.equals(thisUser)) {
            StringBuilder sb = new StringBuilder();
            sb.append(":USERINFO=");
            sb.append(e.getID());
            sb.append('\u0002');
            sb.append(e.getConnectSocketAddress().getAddress().getHostAddress());
            sb.append('\u0002');
            sb.append(e.getAccessStr());
            sb.append('\u0002');
            sb.append(e.getName());
            sb.append('\u0002');
            sb.append(e.getPing());
            sb.append('\u0002');
            sb.append(e.getStatus());
            sb.append('\u0002');
            sb.append(e.getConnectionType());
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", sb.toString()));
         }
      } catch (MessageFormatException var7) {
         log.error("Failed to contruct UserJoined_Notification message: " + var7.getMessage(), var7);
      }

   }
}
