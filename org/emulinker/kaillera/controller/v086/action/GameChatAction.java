package org.emulinker.kaillera.controller.v086.action;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.GameOwnerCommandAction;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameChat;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Request;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.event.GameChatEvent;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.exception.ActionException;
import org.emulinker.kaillera.model.exception.GameChatException;
import org.emulinker.kaillera.model.impl.KailleraUserImpl;

public class GameChatAction implements V086Action, V086GameEventHandler {
   public static final String ADMIN_COMMAND_ESCAPE_STRING = "/";
   private static Log log = LogFactory.getLog(GameChatAction.class);
   private static final String desc = "GameChatAction";
   public static final byte STATUS_IDLE = 1;
   private static GameChatAction singleton = new GameChatAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static GameChatAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "GameChatAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      if(!(message instanceof GameChat_Request)) {
         throw new FatalActionException("Received incorrect instance of GameChat: " + message);
      } else if(clientHandler.getUser() == null) {
         throw new FatalActionException("User does not exist: GameChatAction " + message);
      } else {
         if(((GameChat)message).getMessage().startsWith("/") && (clientHandler.getUser().getAccess() >= 4 || clientHandler.getUser().equals(clientHandler.getUser().getGame().getOwner()))) {
            try {
               if(GameOwnerCommandAction.getInstance().isValidCommand(((GameChat)message).getMessage())) {
                  GameOwnerCommandAction.getInstance().performAction(message, clientHandler);
               } else {
                  this.checkCommands(message, clientHandler);
               }

               return;
            } catch (FatalActionException var6) {
               log.warn("GameOwner command failed: " + var6.getMessage());
            }
         }

         ++this.actionCount;
         GameChat_Request gameChatMessage = (GameChat_Request)message;

         try {
            clientHandler.getUser().gameChat(gameChatMessage.getMessage(), gameChatMessage.getNumber());
         } catch (GameChatException var5) {
            log.debug("Failed to send game chat message: " + var5.getMessage());
         }

      }
   }

   private void checkCommands(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      boolean doCommand = true;
      if(clientHandler.getUser().getAccess() < 2) {
         try {
            clientHandler.getUser().chat(":USER_COMMAND");
         } catch (ActionException var37) {
            doCommand = false;
         }
      }

      if(doCommand) {
         if(((GameChat)message).getMessage().equals("/msgon")) {
            clientHandler.getUser().setMsg(true);

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Private messages are now on."));
            } catch (Exception var14) {
               ;
            }

            return;
         }

         if(((GameChat)message).getMessage().equals("/msgoff")) {
            clientHandler.getUser().setMsg(false);

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Private messages are now off."));
            } catch (Exception var15) {
               ;
            }

            return;
         }

         KailleraUserImpl user;
         KailleraUserImpl access1;
         if(((GameChat)message).getMessage().startsWith("/p2p")) {
            user = (KailleraUserImpl)clientHandler.getUser();
            Iterator e3;
            if(((GameChat)message).getMessage().equals("/p2pon")) {
               clientHandler.getUser().setP2P(true);
               if(clientHandler.getUser().getGame().getOwner().equals(clientHandler.getUser())) {
                  clientHandler.getUser().getGame().setP2P(true);
                  e3 = clientHandler.getUser().getGame().getPlayers().iterator();

                  while(e3.hasNext()) {
                     access1 = (KailleraUserImpl)e3.next();
                     if(access1.isLoggedIn()) {
                        access1.getGame().announce("This game will NOT receive any server activity during gameplay!", access1);
                     }
                  }
               } else {
                  e3 = clientHandler.getUser().getGame().getPlayers().iterator();

                  while(e3.hasNext()) {
                     access1 = (KailleraUserImpl)e3.next();
                     if(access1.isLoggedIn()) {
                        access1.getGame().announce(clientHandler.getUser().getName() + " will NOT receive any server activity during gameplay!", access1);
                     }
                  }
               }
            } else if(((GameChat)message).getMessage().equals("/p2poff")) {
               clientHandler.getUser().setP2P(false);
               if(clientHandler.getUser().getGame().getOwner().equals(clientHandler.getUser())) {
                  clientHandler.getUser().getGame().setP2P(false);
                  e3 = clientHandler.getUser().getGame().getPlayers().iterator();

                  while(e3.hasNext()) {
                     access1 = (KailleraUserImpl)e3.next();
                     if(access1.isLoggedIn()) {
                        access1.getGame().announce("This game will NOW receive ALL server activity during gameplay!", access1);
                     }
                  }
               } else {
                  e3 = clientHandler.getUser().getGame().getPlayers().iterator();

                  while(e3.hasNext()) {
                     access1 = (KailleraUserImpl)e3.next();
                     if(access1.isLoggedIn()) {
                        access1.getGame().announce(clientHandler.getUser().getName() + " will NOW receive ALL server activity during gameplay!", access1);
                     }
                  }
               }
            } else {
               user.getGame().announce("Failed P2P: /p2pon or /p2poff", user);
            }

            return;
         }

         int access;
         if(((GameChat)message).getMessage().startsWith("/msg")) {
            user = (KailleraUserImpl)clientHandler.getUser();
            Scanner e2 = (new Scanner(((GameChat)message).getMessage())).useDelimiter(" ");
            access = clientHandler.getUser().getServer().getAccessManager().getAccess(clientHandler.getUser().getSocketAddress().getAddress());
            if(access < 5 && clientHandler.getUser().getServer().getAccessManager().isSilenced(clientHandler.getUser().getSocketAddress().getAddress())) {
               try {
                  clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You are silenced!"));
               } catch (Exception var16) {
                  ;
               }

               return;
            }

            String m1;
            KailleraUserImpl i$1;
            StringBuilder user4;
            try {
               e2.next();
               int m = e2.nextInt();
               i$1 = (KailleraUserImpl)clientHandler.getUser().getServer().getUser(m);
               user4 = new StringBuilder();

               while(e2.hasNext()) {
                  user4.append(e2.next());
                  user4.append(" ");
               }

               if(i$1 == null) {
                  user.getGame().announce("User not found!", user);

                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                  } catch (Exception var17) {
                     ;
                  }

                  return;
               }

               if(i$1 == clientHandler.getUser()) {
                  user.getGame().announce("You can\'t private message yourself!", user);

                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You can\'t private message yourself!"));
                  } catch (Exception var18) {
                     ;
                  }

                  return;
               }

               if(i$1.getMsg() && !i$1.searchIgnoredUsers(clientHandler.getUser().getConnectSocketAddress().getAddress().getHostAddress())) {
                  m1 = user4.toString();
                  user.setLastMsgID(i$1.getID());
                  i$1.setLastMsgID(user.getID());
                  user.getServer().announce("TO: <" + i$1.getName() + ">(" + i$1.getID() + ") <" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, false, user);
                  i$1.getServer().announce("<" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, false, i$1);
                  if(user.getGame() != null) {
                     user.getGame().announce("TO: <" + i$1.getName() + ">(" + i$1.getID() + ") <" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, user);
                  }

                  if(i$1.getGame() != null) {
                     i$1.getGame().announce("<" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, i$1);
                  }

                  return;
               }

               user.getGame().announce("<" + i$1.getName() + "> Is not accepting private messages!", user);

               try {
                  clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "<" + i$1.getName() + "> Is not accepting private messages!"));
               } catch (Exception var19) {
                  ;
               }

               return;
            } catch (NoSuchElementException var39) {
               if(user.getLastMsgID() != -1) {
                  try {
                     i$1 = (KailleraUserImpl)clientHandler.getUser().getServer().getUser(user.getLastMsgID());
                     user4 = new StringBuilder();

                     while(e2.hasNext()) {
                        user4.append(e2.next());
                        user4.append(" ");
                     }

                     if(i$1 == null) {
                        user.getGame().announce("User not found!", user);

                        try {
                           clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                        } catch (Exception var13) {
                           ;
                        }

                        return;
                     }

                     if(i$1 == clientHandler.getUser()) {
                        user.getGame().announce("You can\'t private message yourself!", user);

                        try {
                           clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You can\'t private message yourself!"));
                        } catch (Exception var12) {
                           ;
                        }

                        return;
                     }

                     if(!i$1.getMsg()) {
                        user.getGame().announce("<" + i$1.getName() + "> Is not accepting private messages!", user);

                        try {
                           clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "<" + i$1.getName() + "> Is not accepting private messages!"));
                        } catch (Exception var11) {
                           ;
                        }

                        return;
                     }

                     m1 = user4.toString();
                     user.getServer().announce("TO: <" + i$1.getName() + ">(" + i$1.getID() + ") <" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, false, user);
                     i$1.getServer().announce("<" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, false, i$1);
                     if(user.getGame() != null) {
                        user.getGame().announce("TO: <" + i$1.getName() + ">(" + i$1.getID() + ") <" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, user);
                     }

                     if(i$1.getGame() != null) {
                        i$1.getGame().announce("<" + clientHandler.getUser().getName() + "> (" + clientHandler.getUser().getID() + "): " + m1, i$1);
                     }

                     return;
                  } catch (Exception var38) {
                     user.getGame().announce("Private Message Error: /msg <UserID> <message>", user);
                     return;
                  }
               }

               user.getGame().announce("Private Message Error: /msg <UserID> <message>", user);
               return;
            }
         }

         if(((GameChat)message).getMessage().equals("/ignoreall")) {
            clientHandler.getUser().setIgnoreAll(true);

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", clientHandler.getUser().getName() + " is now ignoring everyone!"));
            } catch (Exception var20) {
               ;
            }

            return;
         }

         if(((GameChat)message).getMessage().equals("/unignoreall")) {
            clientHandler.getUser().setIgnoreAll(false);

            try {
               clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", clientHandler.getUser().getName() + " is now unignoring everyone!"));
            } catch (Exception var21) {
               ;
            }

            return;
         }

         Scanner user3;
         int e1;
         if(((GameChat)message).getMessage().startsWith("/ignore")) {
            user3 = (new Scanner(((GameChat)message).getMessage())).useDelimiter(" ");

            try {
               user3.next();
               e1 = user3.nextInt();
               access1 = (KailleraUserImpl)clientHandler.getUser().getServer().getUser(e1);
               if(access1 == null) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                  } catch (Exception var22) {
                     ;
                  }

                  return;
               }

               if(access1.getAccess() >= 3) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You cannot ignore an admin!"));
                  } catch (Exception var23) {
                     ;
                  }

                  return;
               }

               if(access1 == clientHandler.getUser()) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You can\'t ignore yourself!"));
                  } catch (Exception var24) {
                     ;
                  }

                  return;
               }

               if(clientHandler.getUser().findIgnoredUser(access1.getConnectSocketAddress().getAddress().getHostAddress())) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You can\'t ignore a user that is already ignored!"));
                  } catch (Exception var25) {
                     ;
                  }

                  return;
               }

               clientHandler.getUser().addIgnoredUser(access1.getConnectSocketAddress().getAddress().getHostAddress());
               access1.getServer().announce(clientHandler.getUser().getName() + " is now ignoring <" + access1.getName() + "> ID: " + access1.getID(), false, (KailleraUserImpl)null);
               return;
            } catch (NoSuchElementException var26) {
               access1 = (KailleraUserImpl)clientHandler.getUser();
               access1.getServer().announce("Ignore User Error: /ignore <UserID>", false, access1);
               log.info("IGNORE USER ERROR: " + access1.getName() + ": " + clientHandler.getRemoteSocketAddress().getHostName());
               return;
            }
         }

         if(((GameChat)message).getMessage().startsWith("/unignore")) {
            user3 = (new Scanner(((GameChat)message).getMessage())).useDelimiter(" ");

            try {
               user3.next();
               e1 = user3.nextInt();
               access1 = (KailleraUserImpl)clientHandler.getUser().getServer().getUser(e1);
               if(access1 == null) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                  } catch (Exception var27) {
                     ;
                  }

                  return;
               }

               if(clientHandler.getUser().findIgnoredUser(access1.getConnectSocketAddress().getAddress().getHostAddress())) {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You can\'t unignore a user that isn\'t ignored!"));
                  } catch (Exception var28) {
                     ;
                  }

                  return;
               }

               if(clientHandler.getUser().removeIgnoredUser(access1.getConnectSocketAddress().getAddress().getHostAddress(), false)) {
                  access1.getServer().announce(clientHandler.getUser().getName() + " is now unignoring <" + access1.getName() + "> ID: " + access1.getID(), false, (KailleraUserImpl)null);
               } else {
                  try {
                     clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                  } catch (Exception var29) {
                     ;
                  }
               }

               return;
            } catch (NoSuchElementException var30) {
               access1 = (KailleraUserImpl)clientHandler.getUser();
               access1.getServer().announce("Unignore User Error: /ignore <UserID>", false, access1);
               log.info("UNIGNORE USER ERROR: " + access1.getName() + ": " + clientHandler.getRemoteSocketAddress().getHostName());
               return;
            }
         }

         if(((GameChat)message).getMessage().startsWith("/me")) {
            int user2 = ((GameChat)message).getMessage().indexOf(32);
            if(user2 < 0) {
               try {
                  clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Invalid # of Fields!"));
               } catch (Exception var31) {
                  ;
               }

               return;
            }

            String e = ((GameChat)message).getMessage().substring(user2 + 1);
            if(e.startsWith(":")) {
               e = e.substring(1);
            }

            access = clientHandler.getUser().getServer().getAccessManager().getAccess(clientHandler.getUser().getSocketAddress().getAddress());
            if(access < 5 && clientHandler.getUser().getServer().getAccessManager().isSilenced(clientHandler.getUser().getSocketAddress().getAddress())) {
               try {
                  clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "You are silenced!"));
               } catch (Exception var32) {
                  ;
               }

               return;
            }

            e = "*" + clientHandler.getUser().getName() + " " + e;
            Iterator i$ = clientHandler.getUser().getGame().getPlayers().iterator();

            while(i$.hasNext()) {
               KailleraUserImpl user1 = (KailleraUserImpl)i$.next();
               user1.getGame().announce(e, user1);
            }

            return;
         }

         if(((GameChat)message).getMessage().equals("/help")) {
            user = (KailleraUserImpl)clientHandler.getUser();
            user.getGame().announce("/me <message> to make personal message eg. /me is bored ...SupraFast is bored.", user);

            try {
               Thread.sleep(20L);
            } catch (Exception var36) {
               ;
            }

            user.getGame().announce("/msg <UserID> <msg> to PM somebody. /msgon or /msgoff to turn pm on | off.", user);

            try {
               Thread.sleep(20L);
            } catch (Exception var35) {
               ;
            }

            user.getGame().announce("/ignore <UserID> or /unignore <UserID> or /ignoreall or /unignoreall to ignore users.", user);

            try {
               Thread.sleep(20L);
            } catch (Exception var34) {
               ;
            }

            user.getGame().announce("/p2pon or /p2poff this option ignores all server activity during gameplay.", user);

            try {
               Thread.sleep(20L);
            } catch (Exception var33) {
               ;
            }

            return;
         }

         clientHandler.getUser().getGame().announce("Uknown Command: " + ((GameChat)message).getMessage(), clientHandler.getUser());
      } else {
         clientHandler.getUser().getGame().announce("Denied: Flood Control", clientHandler.getUser());
      }

   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameChatEvent gameChatEvent = (GameChatEvent)event;

      try {
         if(clientHandler.getUser().searchIgnoredUsers(gameChatEvent.getUser().getConnectSocketAddress().getAddress().getHostAddress())) {
            return;
         }

         if(clientHandler.getUser().getIgnoreAll() && gameChatEvent.getUser().getAccess() < 4 && gameChatEvent.getUser() != clientHandler.getUser()) {
            return;
         }

         String e = gameChatEvent.getMessage();
         clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), gameChatEvent.getUser().getName(), e));
      } catch (MessageFormatException var5) {
         log.error("Failed to contruct GameChat_Notification message: " + var5.getMessage(), var5);
      }

   }
}
