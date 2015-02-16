/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   gameownercommandaction.java

package org.emulinker.kaillera.controller.v086.action;

import java.net.InetSocketAddress;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.access.AccessManager;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.protocol.GameChat;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.exception.ActionException;
import org.emulinker.kaillera.model.impl.*;
import org.emulinker.util.EmuLang;

// Referenced classes of package org.emulinker.kaillera.controller.v086.action:
//            FatalActionException, V086Action

public class GameOwnerCommandAction
    implements V086Action
{

    public static GameOwnerCommandAction getInstance()
    {
        return singleton;
    }

    private GameOwnerCommandAction()
    {
        actionCount = 0;
    }

    public int getActionPerformedCount()
    {
        return actionCount;
    }

    public String toString()
    {
        return "GameOwnerCommandAction";
    }

    public boolean isValidCommand(String s)
    {
        if(s.startsWith("/help"))
            return true;
        if(s.startsWith("/detectautofire"))
            return true;
        if(s.startsWith("/maxusers"))
            return true;
        if(s.startsWith("/maxping"))
            return true;
        if(s.equals("/start"))
            return true;
        if(s.startsWith("/startn"))
            return true;
        if(s.startsWith("/mute"))
            return true;
        if(s.startsWith("/setemu"))
            return true;
        if(s.startsWith("/unmute"))
            return true;
        if(s.startsWith("/swap"))
            return false;
        if(s.startsWith("/kick"))
            return true;
        if(s.startsWith("/samedelay"))
            return true;
        if(s.startsWith("/lag"))
            return true;
        else
            return s.startsWith("/num");
    }

    public void performAction(V086Message v086message, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws FatalActionException
    {
        GameChat gamechat = (GameChat)v086message;
        String s = gamechat.getMessage();
        KailleraUserImpl kaillerauserimpl = (KailleraUserImpl)v086clienthandler.getUser();
        KailleraGameImpl kailleragameimpl = kaillerauserimpl.getGame();
        if(kailleragameimpl == null)
            throw new FatalActionException((new StringBuilder()).append("GameOwner Command Failed: Not in a game: ").append(s).toString());
        if(!kaillerauserimpl.equals(kailleragameimpl.getOwner()) && kaillerauserimpl.getAccess() < 4)
        {
            log.warn((new StringBuilder()).append("GameOwner Command Denied: Not game owner: ").append(kailleragameimpl).append(": ").append(kaillerauserimpl).append(": ").append(s).toString());
            return;
        }
        try
        {
            if(s.startsWith("/help"))
                processHelp(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/detectautofire"))
                processDetectAutoFire(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/maxusers"))
                processMaxUsers(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/maxping"))
                processMaxPing(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.equals("/start"))
                processStart(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/startn"))
                processStartN(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/mute"))
                processMute(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/setemu"))
                processEmu(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/unmute"))
                processUnmute(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/swap"))
                processSwap(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/kick"))
                processKick(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/lag"))
                processLagstat(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/samedelay"))
                processSameDelay(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            else
            if(s.startsWith("/num"))
            {
                processNum(s, kailleragameimpl, kaillerauserimpl, v086clienthandler);
            } else
            {
                kailleragameimpl.announce((new StringBuilder()).append("Unknown Command: ").append(s).toString(), kaillerauserimpl);
                log.info((new StringBuilder()).append("Unknown GameOwner Command: ").append(kailleragameimpl).append(": ").append(kaillerauserimpl).append(": ").append(s).toString());
            }
        }
        catch(ActionException actionexception)
        {
            log.info((new StringBuilder()).append("GameOwner Command Failed: ").append(kailleragameimpl).append(": ").append(kaillerauserimpl).append(": ").append(s).toString());
            kailleragameimpl.announce(EmuLang.getString("GameOwnerCommandAction.CommandFailed", new Object[] {
                actionexception.getMessage()
            }), kaillerauserimpl);
        }
        catch(MessageFormatException messageformatexception)
        {
            log.error((new StringBuilder()).append("Failed to contruct message: ").append(messageformatexception.getMessage()).toString(), messageformatexception);
        }
    }

    private void processHelp(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        kailleragameimpl.announce(EmuLang.getString("GameOwnerCommandAction.SetAutofireDetection"), kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception) { }
        kailleragameimpl.announce("/maxusers <#> to set capacity of room", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception1) { }
        kailleragameimpl.announce("/maxping <#> to set maximum ping for room", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception2) { }
        kailleragameimpl.announce("/start or /startn <#> start game when n players are joined.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception3) { }
        kailleragameimpl.announce("/mute /unmute  <UserID> or /muteall or /unmuteall to mute player(s).", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception4) { }
        kailleragameimpl.announce("/swap <order> eg. 123..n {n = total # of players; Each slot = new player#} Command is temporarily disabled.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception5) { }
        kailleragameimpl.announce("/kick <Player#> or /kickall to kick a player(s).", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception6) { }
        kailleragameimpl.announce("/setemu <Emulator> To restrict the gameroom to this emulator!", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception7) { }
        kailleragameimpl.announce("/lagstat To check who has the most lag spikes or /lagreset to reset lagstat!", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception8) { }
        kailleragameimpl.announce("/samedelay {true | false} If you want both want to play at the same delay setting. Default is false.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception9) { }
        kailleragameimpl.announce("/me <message> to make personal message eg. /me is bored ...SupraFast is bored.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception10) { }
        kailleragameimpl.announce("/msg <UserID> <msg> to PM somebody. /msgon or /msgoff to turn pm on | off.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception11) { }
        kailleragameimpl.announce("/ignore <UserID> or /unignore <UserID> or /ignoreall or /unignoreall to ignore users.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception12) { }
        kailleragameimpl.announce("/p2pon or /p2poff this option ignores ALL server activity during gameplay.", kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception13) { }
    }

    private void autoFireHelp(KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl)
    {
        int i = kailleragameimpl.getAutoFireDetector().getSensitivity();
        kailleragameimpl.announce(EmuLang.getString("GameOwnerCommandAction.HelpSensitivity"), kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception) { }
        kailleragameimpl.announce(EmuLang.getString("GameOwnerCommandAction.HelpDisable"), kaillerauserimpl);
        try
        {
            Thread.sleep(20L);
        }
        catch(Exception exception1) { }
        kailleragameimpl.announce((new StringBuilder()).append(EmuLang.getString("GameOwnerCommandAction.HelpCurrentSensitivity", new Object[] {
            Integer.valueOf(i)
        })).append(i != 0 ? "" : EmuLang.getString("GameOwnerCommandAction.HelpDisabled")).toString(), kaillerauserimpl);
    }

    private void processDetectAutoFire(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        if(kailleragameimpl.getStatus() != 0)
        {
            kailleragameimpl.announce(EmuLang.getString("GameOwnerCommandAction.AutoFireChangeDeniedInGame"), kaillerauserimpl);
            return;
        }
        StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
        if(stringtokenizer.countTokens() != 2)
        {
            autoFireHelp(kailleragameimpl, kaillerauserimpl);
            return;
        }
        String s1 = stringtokenizer.nextToken();
        String s2 = stringtokenizer.nextToken();
        int i = -1;
        try
        {
            i = Integer.parseInt(s2);
        }
        catch(NumberFormatException numberformatexception) { }
        if(i > 5 || i < 0)
        {
            autoFireHelp(kailleragameimpl, kaillerauserimpl);
            return;
        } else
        {
            kailleragameimpl.getAutoFireDetector().setSensitivity(i);
            kailleragameimpl.announce((new StringBuilder()).append(EmuLang.getString("GameOwnerCommandAction.HelpCurrentSensitivity", new Object[] {
                Integer.valueOf(i)
            })).append(i != 0 ? "" : EmuLang.getString("GameOwnerCommandAction.HelpDisabled")).toString(), null);
            return;
        }
    }

    private void processEmu(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        if(s.length() < 1)
            return;
        String s1 = s.substring("/setemu".length() + 1);
        String s2 = s1;
        String s3 = s2;
        if(kaillerauserimpl.getServer().getAccessManager().isSilenced(kaillerauserimpl.getSocketAddress().getAddress()))
        {
            kaillerauserimpl.getGame().announce("Chat Denied: You're silenced!", kaillerauserimpl);
            return;
        }
        try
        {
            s3 = s3.replace(" ", "");
            if(s3.toLowerCase().contains("ggpo.net"))
                s2 = "http://www.iXiGaming.net/Galaxy64";
            else
            if(s3.toLowerCase().contains("2dfighter.com"))
                s2 = "http://www.iXiGaming.net/Galaxy64";
            else
            if(s3.toLowerCase().contains("69") && s3.toLowerCase().contains("90") && s3.toLowerCase().contains("34") && s3.toLowerCase().contains("245"))
                s2 = "k.god-weapon.com:27888";
            kaillerauserimpl.getGame().setAEmulator(s2);
            kaillerauserimpl.getGame().announce((new StringBuilder()).append("Owner has restricted the emulator to: ").append(s2).toString(), null);
            return;
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kaillerauserimpl.getGame().announce("Emulator Set Error: /setemu <Emulator>", kaillerauserimpl);
        }
    }

    private void processNum(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        kaillerauserimpl.getGame().announce((new StringBuilder()).append(kailleragameimpl.getNumPlayers()).append(" in the room!").toString(), kaillerauserimpl);
    }

    private void processLagstat(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        if(kailleragameimpl.getStatus() != 2)
            kailleragameimpl.announce("Lagstat is only available during gameplay!", kaillerauserimpl);
        if(s.equals("/lagstat"))
        {
            String s1 = "";
            Iterator iterator = kailleragameimpl.getPlayers().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                KailleraUserImpl kaillerauserimpl2 = (KailleraUserImpl)iterator.next();
                if(!kaillerauserimpl2.getStealth())
                    s1 = (new StringBuilder()).append(s1).append("P").append(kaillerauserimpl2.getPlayerNumber()).append(": ").append(kaillerauserimpl2.getTimeouts()).append(", ").toString();
            } while(true);
            s1 = s1.substring(0, s1.length() - ", ".length());
            kailleragameimpl.announce((new StringBuilder()).append(s1).append(" lag spikes").toString(), null);
        } else
        if(s.equals("/lagreset"))
        {
            KailleraUserImpl kaillerauserimpl1;
            for(Iterator iterator1 = kailleragameimpl.getPlayers().iterator(); iterator1.hasNext(); kaillerauserimpl1.setTimeouts(0))
                kaillerauserimpl1 = (KailleraUserImpl)iterator1.next();

            kailleragameimpl.announce("LagStat has been reset!", null);
        }
    }

    private void processSameDelay(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        if(s.equals("/samedelay true"))
        {
            kailleragameimpl.setSameDelay(true);
            kaillerauserimpl.getGame().announce("Players will have the same delay when game starts (restarts)!", null);
        } else
        {
            kailleragameimpl.setSameDelay(false);
            kaillerauserimpl.getGame().announce("Players will have independent delays when game starts (restarts)!", null);
        }
    }

    private void processMute(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        String s1 = scanner.next();
        if(s1.equals("/muteall"))
        {
            for(int i = 1; i <= kailleragameimpl.getPlayers().size(); i++)
                if(kailleragameimpl.getPlayer(i).getAccess() < 4)
                    kailleragameimpl.getPlayer(i).setMute(true);

            kaillerauserimpl.getGame().announce("All players have been muted!", null);
            return;
        }
		try{
        int j = scanner.nextInt();
        if(j < 1)
        {
            kaillerauserimpl.getGame().announce("Player doesn't exist!", kaillerauserimpl);
            return;
        }
        KailleraUserImpl kaillerauserimpl1 = (KailleraUserImpl)kailleragameimpl.getPlayer(j);
        if(kaillerauserimpl1 == null)
        {
            kaillerauserimpl.getGame().announce("Player doesn't exist!", kaillerauserimpl);
            return;
        }
        if(kaillerauserimpl1.getAccess() >= 4 && kaillerauserimpl.getAccess() != 5)
        {
            kaillerauserimpl1.getGame().announce("You can't mute an Admin", kaillerauserimpl);
            return;
        }
        if(kaillerauserimpl1 == v086clienthandler.getUser())
        {
            kaillerauserimpl1.getGame().announce("You can't mute yourself!", kaillerauserimpl);
            return;
        }
        try
        {
            if(j >= 1 && j <= kailleragameimpl.getPlayers().size())
            {
                kaillerauserimpl1.setMute(true);
                KailleraUserImpl kaillerauserimpl2 = (KailleraUserImpl)v086clienthandler.getUser();
                kaillerauserimpl2.getGame().announce((new StringBuilder()).append(kaillerauserimpl1.getName()).append(" has been muted!").toString(), null);
            }
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Mute Player Error: /mute <PlayerNumber>", kaillerauserimpl);
        }
		}
		catch(Exception e){
			KailleraUserImpl kaillerauserimpl2 = (KailleraUserImpl)v086clienthandler.getUser();
			kaillerauserimpl2.getGame().announce("Error muting player!", kaillerauserimpl);
			}
    }

    private void processUnmute(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        String s1 = scanner.next();
        if(s1.equals("/unmuteall"))
        {
            for(int i = 1; i <= kailleragameimpl.getPlayers().size(); i++)
                kailleragameimpl.getPlayer(i).setMute(false);

            kaillerauserimpl.getGame().announce("All players have been unmuted!", null);
            return;
        }
        int j = scanner.nextInt();
        if(j < 1)
        {
            kaillerauserimpl.getGame().announce("Player doesn't exist!", kaillerauserimpl);
            return;
        }
        KailleraUserImpl kaillerauserimpl1 = (KailleraUserImpl)kailleragameimpl.getPlayer(j);
        if(kaillerauserimpl1 == null)
        {
            kaillerauserimpl.getGame().announce("Player doesn't exist!", kaillerauserimpl);
            return;
        }
        if(kaillerauserimpl1.getAccess() >= 4 && kaillerauserimpl.getAccess() != 5)
        {
            kaillerauserimpl1.getGame().announce("You can't unmute an Admin", kaillerauserimpl);
            return;
        }
        if(kaillerauserimpl1 == v086clienthandler.getUser())
        {
            kaillerauserimpl1.getGame().announce("You can't unmute yourself!", kaillerauserimpl);
            return;
        }
        try
        {
            if(j >= 1 && j <= kailleragameimpl.getPlayers().size())
            {
                kaillerauserimpl1.setMute(false);
                KailleraUserImpl kaillerauserimpl2 = (KailleraUserImpl)v086clienthandler.getUser();
                kaillerauserimpl2.getGame().announce((new StringBuilder()).append(kaillerauserimpl1.getName()).append(" has been unmuted!").toString(), null);
            }
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Unmute Player Error: /unmute <PlayerNumber>", kaillerauserimpl);
        }
    }

    private void processStartN(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        try
        {
            scanner.next();
            int i = scanner.nextInt();
            if(i > 0 && i < 101)
            {
                kailleragameimpl.setStartN((byte)i);
                kailleragameimpl.announce((new StringBuilder()).append("This game will start when ").append(i).append(" players have joined.").toString(), null);
            } else
            {
                kailleragameimpl.announce("StartN Error: Enter value between 1 and 100.", kaillerauserimpl);
            }
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Failed: /startn <#>", kaillerauserimpl);
        }
    }

    private void processSwap(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
            throws ActionException, MessageFormatException {
          if(kailleragameimpl.getStatus() != 2) {
             kailleragameimpl.announce("Failed: /swap can only be used during gameplay!", kaillerauserimpl);
          } else {
             scanner2 = new Scanner(s);
			Scanner scanner = scanner2.useDelimiter(" ");

             try {
                boolean e = true;
                scanner.next();
                int test = scanner.nextInt();
                String str = Integer.toString(test);
                if(kailleragameimpl.getPlayers().size() < str.length()) {
                   kailleragameimpl.announce("Failed: You can't swap more than the # of players in the room.", kaillerauserimpl);
                   return;
                }

                PlayerActionQueue temp = kailleragameimpl.getPlayerActionQueue()[0];

                for(int var13 = 0; var13 < str.length(); ++var13) {
                   KailleraUserImpl player = (KailleraUserImpl)kailleragameimpl.getPlayers().get(var13);
                   String w = String.valueOf(str.charAt(var13));
                   player.setPlayerNumber(Integer.parseInt(w));
                   if(Integer.parseInt(w) == 1) {
                      kailleragameimpl.getPlayerActionQueue()[var13] = temp;
                   } else {
                      kailleragameimpl.getPlayerActionQueue()[var13] = kailleragameimpl.getPlayerActionQueue()[Integer.parseInt(w) - 1];
                   }

                   kailleragameimpl.announce(player.getName() + " is now Player#: " + player.getPlayerNumber(), (KailleraUser)null);
                }
             } catch (NoSuchElementException var12) {
                kailleragameimpl.announce("Swap Player Error: /swap <order> eg. 123..n {n = total # of players; Each slot = new player#}", kaillerauserimpl);
             }

          }
       }


    private void processStart(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        kailleragameimpl.start(kaillerauserimpl);
    }

    private void processKick(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        String s1 = scanner.next();
        if(s1.equals("/kickall"))
        {
            for(int i = 1; i <= kailleragameimpl.getPlayers().size(); i++)
                kailleragameimpl.kick(kailleragameimpl.getPlayer(i), kailleragameimpl.getPlayer(i).getID());

            kaillerauserimpl.getGame().announce("All players have been kicked!", null);
            return;
        }
        try
        {
            int j = scanner.nextInt();
            if(kailleragameimpl.getPlayer(j) != null)
                kailleragameimpl.kick(kaillerauserimpl, kailleragameimpl.getPlayer(j).getID());
            else
                kailleragameimpl.announce("Player doesn't exisit!", kaillerauserimpl);
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Failed: /kick <Player#> or /kickall to kick all players.", kaillerauserimpl);
        }
    }

    private void processMaxUsers(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        if(System.currentTimeMillis() - lastMaxUserChange <= 3000L)
        {
            kailleragameimpl.announce("Max User Command Spam Detection...Please Wait!", kaillerauserimpl);
            lastMaxUserChange = System.currentTimeMillis();
            return;
        }
        lastMaxUserChange = System.currentTimeMillis();
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        try
        {
            scanner.next();
            int i = scanner.nextInt();
            if(i > 0 && i < 101)
            {
                kailleragameimpl.setMaxUsers(i);
                kailleragameimpl.announce((new StringBuilder()).append("Max Users has been set to ").append(i).toString(), null);
            } else
            {
                kailleragameimpl.announce("Max Users Error: Enter value between 1 and 100", kaillerauserimpl);
            }
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Failed: /maxusers <#>", kaillerauserimpl);
        }
    }

    private void processMaxPing(String s, KailleraGameImpl kailleragameimpl, KailleraUserImpl kaillerauserimpl, org.emulinker.kaillera.controller.v086.V086Controller.V086ClientHandler v086clienthandler)
        throws ActionException, MessageFormatException
    {
        Scanner scanner = (new Scanner(s)).useDelimiter(" ");
        try
        {
            scanner.next();
            int i = scanner.nextInt();
            if(i > 0 && i < 1001)
            {
                kailleragameimpl.setMaxPing(i);
                kailleragameimpl.announce((new StringBuilder()).append("Max Ping has been set to ").append(i).toString(), null);
            } else
            {
                kailleragameimpl.announce("Max Ping Error: Enter value between 1 and 1000", kaillerauserimpl);
            }
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            kailleragameimpl.announce("Failed: /maxping <#>", kaillerauserimpl);
        }
    }

    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_DETECTAUTOFIRE = "/detectautofire";
    public static final String COMMAND_LAGSTAT = "/lag";
    public static final String COMMAND_MAXUSERS = "/maxusers";
    public static final String COMMAND_MAXPING = "/maxping";
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_STARTN = "/startn";
    public static final String COMMAND_MUTE = "/mute";
    public static final String COMMAND_UNMUTE = "/unmute";
    public static final String COMMAND_SWAP = "/swap";
    public static final String COMMAND_KICK = "/kick";
    public static final String COMMAND_EMU = "/setemu";
    public static final String COMMAND_SAMEDELAY = "/samedelay";
    public static final String COMMAND_NUM = "/num";
    private static long lastMaxUserChange = 0L;
    private static Log log = LogFactory.getLog("org/emulinker/kaillera/controller/v086/action/GameOwnerCommandAction");
    private static final String desc = "GameOwnerCommandAction";
    private static GameOwnerCommandAction singleton = new GameOwnerCommandAction();
    private int actionCount;
	private Scanner scanner2;

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\Users\Agent 21\Downloads\NewZip\emulinker\lib\emulinker.jar
	Total time: 178 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/