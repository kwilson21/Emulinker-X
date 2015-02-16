package org.emulinker.kaillera.controller.v086.action;

import java.net.InetAddress;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.access.AccessManager;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.*;
import org.emulinker.kaillera.model.exception.ActionException;
import org.emulinker.kaillera.model.impl.*;
import org.emulinker.release.ReleaseInfo;
import org.emulinker.util.*;

// Referenced classes of package org.emulinker.kaillera.controller.v086.action:
//            FatalActionException, V086Action

public class CopyOfAdminCommandAction 
implements V086Action
{

    public static CopyOfAdminCommandAction getInstance()
    {
        return singleton;
    }

    private CopyOfAdminCommandAction()
    {
        actionCount = 0;
    }

    public int getActionPerformedCount()
    {
        return actionCount;
    }

    public String toString()
    {
        return "AdminCommandAction";
    }

    public boolean isValidCommand(String chat)
    {
        if(chat.startsWith("/help"))
            return true;
        if(chat.startsWith("/finduser"))
            return true;
        if(chat.startsWith("/findgame"))
            return true;
        if(chat.startsWith("/closegame"))
            return true;
        if(chat.startsWith("/kick"))
            return true;
        if(chat.startsWith("/ban"))
            return true;
        if(chat.startsWith("/tempelevated"))
            return true;
        if(chat.startsWith("/silence"))
            return true;
        if(chat.startsWith("/announcegame"))
            return true;
        if(chat.startsWith("/announce"))
            return true;
        if(chat.startsWith("/tempadmin"))
            return true;
        if(chat.startsWith("/version"))
            return true;
        if(chat.startsWith("/clear"))
            return true;
        if(chat.startsWith("/stealth"))
            return true;
        if(chat.startsWith("/unscramble"))
            return true;
        if(chat.startsWith("/unsilence"))
            return true;
            return false;
    }
    public void performAction(V086Message message, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws FatalActionException
        {
        Chat chatMessage = (Chat)message;
        String chat = chatMessage.getMessage();
        KailleraServerImpl server = (KailleraServerImpl)clientHandler.getController().getServer();
        AccessManager accessManager = server.getAccessManager();
        KailleraUserImpl user = (KailleraUserImpl)clientHandler.getUser();
        if(accessManager.getAccess(clientHandler.getRemoteInetAddress()) < 4 && !chat.startsWith("/silence") && !chat.startsWith("/kick") && !chat.startsWith("/help") && !chat.startsWith("/unscramble") && !chat.startsWith("/finduser") && !chat.startsWith("/unsilence") || accessManager.getAccess(clientHandler.getRemoteInetAddress()) <= 2)
        {
            try
            {
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Admin Command Error: You are not an admin!"));
            }
            catch(MessageFormatException e) { }
            throw new FatalActionException((new StringBuilder()).append("Admin Command Denied: ").append(user).append(" does not have Admin access: ").append(chat).toString());
        }
        try
        {
            if(chat.startsWith("/help"))
                processHelp(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/finduser"))
                processFindUser(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/findgame"))
                processFindGame(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/closegame"))
                processCloseGame(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/kick"))
                processKick(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/ban"))
                processBan(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/tempelevated"))
                processTempElevated(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/silence"))
                processSilence(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/announcegame"))
                processGameAnnounce(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/announce"))
                processAnnounce(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/tempadmin"))
                processTempAdmin(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/version"))
                processVersion(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/clear"))
                processClear(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/stealth"))
                processStealth(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/unscramble"))
                processUnscramble(chat, server, user, clientHandler);
            else
            if(chat.startsWith("/unsilence"))
                processUnsilence(chat, server, user, clientHandler);
            else
                throw new ActionException((new StringBuilder()).append("Invalid Command: ").append(chat).toString());
        }
        catch(ActionException e)
        {
            log.info((new StringBuilder()).append("Admin Command Failed: ").append(user).append(": ").append(chat).toString());
            try
            {
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.Failed", new Object[] {
                    e.getMessage()
                })));
            }
            catch(MessageFormatException e2)
            {
                log.error((new StringBuilder()).append("Failed to contruct InformationMessage message: ").append(e.getMessage()).toString(), e);
            }
        }
        catch(MessageFormatException e)
        {
            log.error((new StringBuilder()).append("Failed to contruct message: ").append(e.getMessage()).toString(), e);
        }
    }

    private void processHelp(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpVersion")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        if(admin.getAccess() == 5)
        {
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpTempAdmin")));
            try
            {
                Thread.sleep(20L);
            }
            catch(Exception e) { }
        }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpKick")));
        try
        {
            Thread.sleep(20L);
        }
        catch (Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpSilence")));
        try
        {
            Thread.sleep(20L);
        }
        catch (Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "/unscrambleon to start the unscramble bot- /unscramblepause to pause the bot- /unscrambleresume to resume the bot after pause- /unscramblesave to save the bot's scores- /unscrambletime to change the question delay"));
        try
        {
            Thread.sleep(20L);
        }
        catch (Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "/unsilence to unsilence a silenced user"));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        if(admin.getAccess() == 3)
            return;
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpBan")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpClear")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpCloseGame")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpAnnounce")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpAnnounceAll")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpAnnounceGame")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpFindUser")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.HelpFindGame")));
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception e) { }
        clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "/stealthon /stealthoff to join a room invisibly"));
        if(admin.getAccess() == 5)
        {
            try
            {
                Thread.sleep(20L);
            }
            catch(Exception e) { }
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "/tempelevated <UserID> <min> gives elevation."));
        }
    }

    private void processFindUser(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        int space = message.indexOf(' ');
        if(space < 0)
            throw new ActionException(EmuLang.getString("AdminCommandAction.FindUserError"));
        int foundCount = 0;
        String str = message.substring(space + 1);
        Iterator i$ = server.getUsers().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            KailleraUserImpl user = (KailleraUserImpl)i$.next();
            if(user.isLoggedIn() && user.getName().toLowerCase().contains(str.toLowerCase()))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("UserID: ");
                sb.append(user.getID());
                sb.append(", IP: ");
                sb.append(user.getConnectSocketAddress().getAddress().getHostAddress());
                sb.append(", Nick: <");
                sb.append(user.getName());
                sb.append(">, Access: ");
                sb.append(user.getAccessStr());
                if(user.getGame() != null)
                {
                    sb.append(", GameID: ");
                    sb.append(user.getGame().getID());
                    sb.append(", Game: ");
                    sb.append(user.getGame().getRomName());
                }
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", sb.toString()));
                foundCount++;
            }
        } while(true);
        if(foundCount == 0)
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.NoUsersFound")));
    }

    private void processFindGame(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        int space = message.indexOf(' ');
        if(space < 0)
            throw new ActionException(EmuLang.getString("AdminCommandAction.FindGameError"));
        int foundCount = 0;
        WildcardStringPattern pattern = new WildcardStringPattern(message.substring(space + 1));
        Iterator i$ = server.getGames().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            KailleraGameImpl game = (KailleraGameImpl)i$.next();
            if(pattern.match(game.getRomName()))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(game.getID());
                sb.append(": ");
                sb.append(game.getOwner().getName());
                sb.append(" ");
                sb.append(game.getRomName());
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", sb.toString()));
                foundCount++;
            }
        } while(true);
        if(foundCount == 0)
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.NoGamesFound")));
    }

    private void processSilence(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int userID = scanner.nextInt();
            int minutes = scanner.nextInt();
            KailleraUserImpl user = (KailleraUserImpl)server.getUser(userID);
            if(user == null)
                throw new ActionException((new StringBuilder()).append(EmuLang.getString("AdminCommandAction.UserNotFound")).append(userID).toString());
            if(user.getID() == admin.getID())
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotSilenceSelf"));
            int access = server.getAccessManager().getAccess(user.getConnectSocketAddress().getAddress());
            if(access >= 4 && admin.getAccess() != 5)
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotSilenceAdmin"));
            if(access == 3 && admin.getAccess() == 3)
                throw new ActionException("You cannot silence another moderator!");
            if(access == 3 && admin.getStatus() == 3)
                throw new ActionException("You cannot silence an elevated user if you're not an admin!");
            if(admin.getAccess() == 3)
            {
                if(server.getAccessManager().isSilenced(user.getSocketAddress().getAddress()))
                    throw new ActionException("This User has already been Silenced.  Please wait until his time expires.");
                if(minutes > 15)
                    throw new ActionException("Moderators can only silence up to 15 minutes!");
            }
            server.getAccessManager().addSilenced(user.getConnectSocketAddress().getAddress().getHostAddress(), minutes);
            server.announce(EmuLang.getString("AdminCommandAction.Silenced", new Object[] {
                Integer.valueOf(minutes), user.getName()
            }), false, null);
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.SilenceError"));
        }
    }

    private void processKick(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int userID = scanner.nextInt();
            KailleraUserImpl user = (KailleraUserImpl)server.getUser(userID);
            if(user == null)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserNotFound", new Object[] {
                    Integer.valueOf(userID)
                }));
            if(user.getID() == admin.getID())
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotKickSelf"));
            int access = server.getAccessManager().getAccess(user.getConnectSocketAddress().getAddress());
            if(access == 3 && admin.getStatus() == 3)
                throw new ActionException("You cannot kick a moderator if you're not an admin!");
            if(access == 3 && admin.getAccess() == 3)
                throw new ActionException("You cannot boot another moderator!");
            if(access >= 4 && admin.getAccess() != 5)
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotKickAdmin"));
            user.quit(EmuLang.getString("AdminCommandAction.QuitKicked"));
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.KickError"));
        }
    }

    private void processCloseGame(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int gameID = scanner.nextInt();
            KailleraGameImpl game = (KailleraGameImpl)server.getGame(gameID);
            if(game == null)
                throw new ActionException(EmuLang.getString("AdminCommandAction.GameNotFound", new Object[] {
                    Integer.valueOf(gameID)
                }));
            KailleraUserImpl owner = (KailleraUserImpl)game.getOwner();
            int access = server.getAccessManager().getAccess(owner.getConnectSocketAddress().getAddress());
            if(access >= 4 && admin.getAccess() != 5 && owner.isLoggedIn())
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotCloseAdminGame"));
            owner.quitGame();
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.CloseGameError"));
        }
    }

    private void processBan(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int userID = scanner.nextInt();
            int minutes = scanner.nextInt();
            KailleraUserImpl user = (KailleraUserImpl)server.getUser(userID);
            if(user == null)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserNotFound", new Object[] {
                    Integer.valueOf(userID)
                }));
            if(user.getID() == admin.getID())
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotBanSelf"));
            int access = server.getAccessManager().getAccess(user.getConnectSocketAddress().getAddress());
            if(access >= 4 && admin.getAccess() != 5)
                throw new ActionException(EmuLang.getString("AdminCommandAction.CanNotBanAdmin"));
            server.announce(EmuLang.getString("AdminCommandAction.Banned", new Object[] {
                Integer.valueOf(minutes), user.getName()
            }), false, null);
            user.quit(EmuLang.getString("AdminCommandAction.QuitBanned"));
            server.getAccessManager().addTempBan(user.getConnectSocketAddress().getAddress().getHostAddress(), minutes);
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.BanError"));
        }
    }

    private void processTempElevated(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        if(admin.getAccess() != 5)
            throw new ActionException("Only SuperAdmins can give Temp Elevated Status!");
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int userID = scanner.nextInt();
            int minutes = scanner.nextInt();
            KailleraUserImpl user = (KailleraUserImpl)server.getUser(userID);
            if(user == null)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserNotFound", new Object[] {
                    Integer.valueOf(userID)
                }));
            if(user.getID() == admin.getID())
                throw new ActionException(EmuLang.getString("AdminCommandAction.AlreadyAdmin"));
            int access = server.getAccessManager().getAccess(user.getConnectSocketAddress().getAddress());
            if(access >= 4 && admin.getAccess() != 5)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserAlreadyAdmin"));
            if(access == 2)
                throw new ActionException("User is already elevated.");
            server.getAccessManager().addTempElevated(user.getConnectSocketAddress().getAddress().getHostAddress(), minutes);
            server.announce((new StringBuilder()).append("Temp Elevated Granted: ").append(user.getName()).append(" for ").append(minutes).append("min").toString(), false, null);
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("Temp Elevated Error."));
        }
    }

    private void processTempAdmin(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        if(admin.getAccess() != 5)
            throw new ActionException("Only Super Admins can give Temp Admin Status!");
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int userID = scanner.nextInt();
            int minutes = scanner.nextInt();
            KailleraUserImpl user = (KailleraUserImpl)server.getUser(userID);
            if(user == null)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserNotFound", new Object[] {
                    Integer.valueOf(userID)
                }));
            if(user.getID() == admin.getID())
                throw new ActionException(EmuLang.getString("AdminCommandAction.AlreadyAdmin"));
            int access = server.getAccessManager().getAccess(user.getConnectSocketAddress().getAddress());
            if(access >= 4 && admin.getAccess() != 5)
                throw new ActionException(EmuLang.getString("AdminCommandAction.UserAlreadyAdmin"));
            server.getAccessManager().addTempAdmin(user.getConnectSocketAddress().getAddress().getHostAddress(), minutes);
            server.announce(EmuLang.getString("AdminCommandAction.TempAdminGranted", new Object[] {
                Integer.valueOf(minutes), user.getName()
            }), false, null);
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.TempAdminError"));
        }
    }

    private void processStealth(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        if(admin.getGame() != null)
            throw new ActionException("Can't use /stealth while in a gameroom.");
        if(admin.getAccess() != 5)
            throw new ActionException("Only Super Admins can use stealth!");
        if(message.equals("/stealthon"))
        {
            admin.setStealth(true);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Stealth Mode is on."));
        } else
        if(message.equals("/stealthoff"))
        {
            admin.setStealth(false);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "Stealth Mode is off."));
        } else
        {
            throw new ActionException("Stealth Mode Error: /stealthon /stealthoff");
        }
    }
    
    private void processUnscramble(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        if(message.equals("/unscramblereset"))
        {
            if(server.getSwitchTrivia())
            {
                server.getTrivia().saveScores(true);
                server.getTriviaThread().stop();
            }
            server.announce("<Unscramble> EmuLinker X Unscramble has been reset!", false, null);
            Trivia trivia = new Trivia(server);
            Thread triviaThread = new Thread(trivia);
            triviaThread.start();
            server.setTriviaThread(triviaThread);
            server.setTrivia(trivia);
            server.setSwitchTrivia(true);
            trivia.setTriviaPaused(false);
        } else
        if(message.equals("/unscrambleon"))
        {
            if(server.getTrivia() != null)
                throw new ActionException("Unscrambler already started!");
            server.announce("EmuLinker X Unscramble has been started!",false, null);
            Trivia trivia = new Trivia(server);
            Thread triviaThread = new Thread(trivia);
            triviaThread.start();
            server.setTriviaThread(triviaThread);
            server.setTrivia(trivia);
            server.setSwitchTrivia(true);
            trivia.setTriviaPaused(false);
        } else
        if(message.equals("/unscramblepause"))
        {
            if(server.getTrivia() == null)
                throw new ActionException("Unscrambler needs to be started first!");
            server.getTrivia().setTriviaPaused(true);
            server.announce("<Unscramble> EmuLinker X Unscramble will be paused after this question!", false, null);
        } else
        if(message.equals("/unscrambleresume"))
        {
            if(server.getTrivia() == null)
                throw new ActionException("Unscrambler needs to be started first!");
            server.getTrivia().setTriviaPaused(false);
            server.announce("<Unscramble> EmuLinker X Unscramble has been resumed!", false, null);
        } else
        if(message.equals("/unscramblesave"))
        {
            if(server.getTrivia() == null)
                throw new ActionException("Unscrambler needs to be started first!");
            server.getTrivia().saveScores(true);
        } else
        if(message.startsWith("/unscrambletime"))
        {
            if(server.getTrivia() == null)
                throw new ActionException("Unscrambler needs to be started first!");
            Scanner scanner = (new Scanner(message)).useDelimiter(" ");
            try
            {
                scanner.next();
                int questionTime = scanner.nextInt();
                server.getTrivia().setQuestionTime(questionTime * 1000);
                server.announce((new StringBuilder()).append("<Unscramble> EmuLinker X Unscramble's question delay has been changed to ").append(questionTime).append("s!").toString(), false, admin);
            }
            catch(Exception e)
            {
                throw new ActionException("Invalid Unscramble Time!");
            }
        }
    }

    private void processAnnounce(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        int space = message.indexOf(' ');
        if(space < 0)
            throw new ActionException(EmuLang.getString("AdminCommandAction.AnnounceError"));
        boolean all = false;
        if(message.startsWith("/announceall"))
            all = true;
        String announcement = message.substring(space + 1);
        if(announcement.startsWith(":"))
            announcement = announcement.substring(1);
        server.announce(announcement, all, null);
    }

    private void processGameAnnounce(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(message)).useDelimiter(" ");
        try
        {
            scanner.next();
            int gameID = scanner.nextInt();
            StringBuilder sb = new StringBuilder();
            for(; scanner.hasNext(); sb.append(" "))
                sb.append(scanner.next());

            KailleraGameImpl game = (KailleraGameImpl)server.getGame(gameID);
            if(game == null)
                throw new ActionException((new StringBuilder()).append(EmuLang.getString("AdminCommandAction.GameNoutFound")).append(gameID).toString());
            game.announce(sb.toString(), null);
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.AnnounceGameError"));
        }
    }

    private void processClear(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        int space = message.indexOf(' ');
        if(space < 0)
            throw new ActionException(EmuLang.getString("AdminCommandAction.ClearError"));
        String addressStr = message.substring(space + 1);
        InetAddress inetAddr = null;
        try
        {
            inetAddr = InetAddress.getByName(addressStr);
        }
        catch(Exception e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.ClearAddressFormatError"));
        }
        if(server.getAccessManager().clearTemp(inetAddr))
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.ClearSuccess")));
        else
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.ClearNotFound")));
    }

    private void processUnsilence(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        int space = message.indexOf(' ');
        if(space < 0)
            throw new ActionException("/unsilence <ip address>");
        String addressStr = message.substring(space + 1);
        InetAddress inetAddr = null;
        try
        {
            inetAddr = InetAddress.getByName(addressStr);
        }
        catch(Exception e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.ClearAddressFormatError"));
        }
        if(server.getAccessManager().clearTempSilence(inetAddr))
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", "User successfully unsilenced!"));
        else
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", EmuLang.getString("AdminCommandAction.ClearNotFound")));

    }

    private void processVersion(String message, KailleraServerImpl server, KailleraUserImpl admin, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler clientHandler)
        throws ActionException, MessageFormatException
    {
        try
        {
            ReleaseInfo releaseInfo = server.getReleaseInfo();
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("VERSION: ").append(releaseInfo.getProductName()).toString()));
            sleep(20);
            Properties props = System.getProperties();
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("JAVAVER: ").append(props.getProperty("java.version")).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("JAVAVEND: ").append(props.getProperty("java.vendor")).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("OSNAME: ").append(props.getProperty("os.name")).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("OSARCH: ").append(props.getProperty("os.arch")).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("OSVER: ").append(props.getProperty("os.version")).toString()));
            sleep(20);
            Runtime runtime = Runtime.getRuntime();
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("NUMPROCS: ").append(runtime.availableProcessors()).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("FREEMEM: ").append(runtime.freeMemory()).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("MAXMEM: ").append(runtime.maxMemory()).toString()));
            sleep(20);
            clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("TOTMEM: ").append(runtime.totalMemory()).toString()));
            sleep(20);
            Map env = System.getenv();
            if(EmuUtil.systemIsWindows())
            {
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("COMPNAME: ").append((String)env.get("COMPUTERNAME")).toString()));
                sleep(20);
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("USER: ").append((String)env.get("USERNAME")).toString()));
                sleep(20);
            } else
            {
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("COMPNAME: ").append((String)env.get("HOSTNAME")).toString()));
                sleep(20);
                clientHandler.send(new InformationMessage(clientHandler.getNextMessageNumber(), "server", (new StringBuilder()).append("USER: ").append((String)env.get("USERNAME")).toString()));
                sleep(20);
            }
        }
        catch(NoSuchElementException e)
        {
            throw new ActionException(EmuLang.getString("AdminCommandAction.VersionError"));
        }
    }

    private void sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(Exception e) { }
    }

    public static final String COMMAND_ANNOUNCE = "/announce";
    public static final String COMMAND_ANNOUNCEALL = "/announceall";
    public static final String COMMAND_ANNOUNCEGAME = "/announcegame";
    public static final String COMMAND_BAN = "/ban";
    public static final String COMMAND_CLEAR = "/clear";
    public static final String COMMAND_CLOSEGAME = "/closegame";
    public static final String COMMAND_FINDGAME = "/findgame";
    public static final String COMMAND_FINDUSER = "/finduser";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_KICK = "/kick";
    public static final String COMMAND_SILENCE = "/silence";
    public static final String COMMAND_TEMPADMIN = "/tempadmin";
    public static final String COMMAND_VERSION = "/version";
    public static final String COMMAND_TRIVIA = "/trivia";
    public static final String COMMAND_STEALTH = "/stealth";
    public static final String COMMAND_TEMPELEVATED = "/tempelevated";
    private static Log log = LogFactory.getLog("org/emulinker/kaillera/controller/v086/action/AdminCommandAction");
    private static final String desc = "AdminCommandAction";
    private static CopyOfAdminCommandAction singleton = new CopyOfAdminCommandAction();
    private int actionCount;

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\EmulinkerXSources\SourceCode\org\emulinker\emulinker.jar
	Total time: 121 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/