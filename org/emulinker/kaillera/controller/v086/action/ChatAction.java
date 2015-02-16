package org.emulinker.kaillera.controller.v086.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.access.AccessManager;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.AdminCommandAction;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.Chat;
import org.emulinker.kaillera.controller.v086.protocol.Chat_Notification;
import org.emulinker.kaillera.controller.v086.protocol.Chat_Request;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.ChatEvent;
import org.emulinker.kaillera.model.event.ServerEvent;
import org.emulinker.kaillera.model.exception.ActionException;
import org.emulinker.kaillera.model.impl.KailleraGameImpl;
import org.emulinker.kaillera.model.impl.KailleraServerImpl;
import org.emulinker.kaillera.model.impl.KailleraUserImpl;
import org.emulinker.release.ReleaseInfo;
import org.emulinker.util.EmuLang;

public class ChatAction
implements V086Action,
V086ServerEventHandler {
    public static final String ADMIN_COMMAND_ESCAPE_STRING = "/";
    private static Log log = LogFactory.getLog((Class)ChatAction.class);
    private static final String desc = "ChatAction";
    private static ChatAction singleton = new ChatAction();
    private int actionCount = 0;
    private int handledCount = 0;

    public static ChatAction getInstance() {
        return singleton;
    }

    private ChatAction() {
    }

    public int getActionPerformedCount() {
        return this.actionCount;
    }

    public int getHandledEventCount() {
        return this.handledCount;
    }


    public String toString() {
        return "ChatAction";
    }


    public void performAction(V086Message v086Message, V086Controller.V086ClientHandler v086ClientHandler) throws FatalActionException {
        if (!(v086Message instanceof Chat_Request)) {
            throw new FatalActionException("Received incorrect instance of Chat: " + v086Message);
        }
        if (((Chat)v086Message).getMessage().startsWith("/")) {
            if (v086ClientHandler.getUser().getAccess() <= 2) {
                this.checkCommands(v086Message, v086ClientHandler);
                return;
            }
            try {
                if (AdminCommandAction.getInstance().isValidCommand(((Chat)v086Message).getMessage())) {
                    AdminCommandAction.getInstance().performAction(v086Message, v086ClientHandler);
                    if (!((Chat)v086Message).getMessage().equals("/help")) return;
                    this.checkCommands(v086Message, v086ClientHandler);
                    return;
                }
                this.checkCommands(v086Message, v086ClientHandler);
                return;
            }
            catch (FatalActionException var3_3) {
                log.warn((Object)("Admin command failed: " + var3_3.getMessage()));
            }
            return;
        }
        ++this.actionCount;
        try {
            v086ClientHandler.getUser().chat(((Chat)v086Message).getMessage());
            return;
        }
        catch (ActionException var3_4) {
            log.info((Object)("Chat Denied: " + v086ClientHandler.getUser() + ": " + ((Chat)v086Message).getMessage()));
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", EmuLang.getString("ChatAction.ChatDenied", var3_4.getMessage())));
                return;
            }
            catch (MessageFormatException var4_5) {
                log.error((Object)("Failed to contruct InformationMessage message: " + var3_4.getMessage()), (Throwable)var3_4);
            }
        }
    }

    private void checkCommands(V086Message v086Message, V086Controller.V086ClientHandler v086ClientHandler) throws FatalActionException {
        int n;
        String string;
        Iterator<KailleraUserImpl> iterator;
        boolean bl = true;
        KailleraUserImpl kailleraUserImpl = (KailleraUserImpl)v086ClientHandler.getUser();
        if (kailleraUserImpl.getAccess() < 2) {
            try {
                v086ClientHandler.getUser().chat(":USER_COMMAND");
            }
            catch (ActionException var5_5) {
                bl = false;
            }
            if (!bl) {
                kailleraUserImpl.getServer().announce("Denied: Flood Control", false, kailleraUserImpl);
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/alivecheck")) {
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", ":ALIVECHECK=EmulinkerSF Alive Check: You are still logged in."));
                return;
            }
            catch (Exception var5_6) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/version") && v086ClientHandler.getUser().getAccess() < 4) {
            ReleaseInfo releaseInfo = v086ClientHandler.getUser().getServer().getReleaseInfo();
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "VERSION: " + releaseInfo.getProductName() + ": " + releaseInfo.getVersionString() + ": " + releaseInfo.getReleaseDate()));
                return;
            }
            catch (Exception var6_37) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/myip")) {
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Your IP Address is: " + v086ClientHandler.getUser().getConnectSocketAddress().getAddress().getHostAddress()));
                return;
            }
            catch (Exception var5_8) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/msgon")) {
            v086ClientHandler.getUser().setMsg(true);
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Private messages are now on."));
                return;
            }
            catch (Exception var5_9) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/msgoff")) {
            v086ClientHandler.getUser().setMsg(false);
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Private messages are now off."));
                return;
            }
            catch (Exception var5_10) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().startsWith("/me")) {
            int n2;
            int n3 = ((Chat)v086Message).getMessage().indexOf(32);
            if (n3 < 0) {
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Invalid # of Fields!"));
                    return;
                }
                catch (Exception var6_38) {
                    // empty catch block
                }
                return;
            }
            String string2 = ((Chat)v086Message).getMessage().substring(n3 + 1);
            if (string2.startsWith(":")) {
                string2 = string2.substring(1);
            }
            if ((n2 = v086ClientHandler.getUser().getServer().getAccessManager().getAccess(v086ClientHandler.getUser().getSocketAddress().getAddress())) < 5 && v086ClientHandler.getUser().getServer().getAccessManager().isSilenced(v086ClientHandler.getUser().getSocketAddress().getAddress())) {
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You are silenced!"));
                    return;
                }
                catch (Exception var8_56) {
                    // empty catch block
                }
                return;
            }
            if (!v086ClientHandler.getUser().getServer().checkMe(v086ClientHandler.getUser(), string2)) return;
            String string3 = string2;
            string2 = "*" + v086ClientHandler.getUser().getName() + " " + string3;
            KailleraUserImpl kailleraUserImpl2 = (KailleraUserImpl)v086ClientHandler.getUser();
            v086ClientHandler.getUser().getServer().announce(string2, true, kailleraUserImpl2);
            return;
        }
        if (((Chat)v086Message).getMessage().startsWith("/msg")) {
            KailleraUserImpl kailleraUserImpl3 = (KailleraUserImpl)v086ClientHandler.getUser();
            Scanner scanner = new Scanner(((Chat)v086Message).getMessage()).useDelimiter(" ");
            int n4 = v086ClientHandler.getUser().getServer().getAccessManager().getAccess(v086ClientHandler.getUser().getSocketAddress().getAddress());
            if (n4 < 5 && v086ClientHandler.getUser().getServer().getAccessManager().isSilenced(v086ClientHandler.getUser().getSocketAddress().getAddress())) {
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You are silenced!"));
                    return;
                }
                catch (Exception var8_58) {
                    // empty catch block
                }
                return;
            }
            try {
                scanner.next();
                int n5 = scanner.nextInt();
                KailleraUserImpl kailleraUserImpl4 = (KailleraUserImpl)v086ClientHandler.getUser().getServer().getUser(n5);
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    stringBuilder.append(scanner.next());
                    stringBuilder.append(" ");
                }
                if (kailleraUserImpl4 == null) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                        return;
                    }
                    catch (Exception var11_86) {
                        // empty catch block
                    }
                    return;
                }
                if (kailleraUserImpl4 == v086ClientHandler.getUser()) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You can't private message yourself!"));
                        return;
                    }
                    catch (Exception var11_87) {
                        // empty catch block
                    }
                    return;
                }
                if (!kailleraUserImpl4.getMsg() || kailleraUserImpl4.searchIgnoredUsers(v086ClientHandler.getUser().getConnectSocketAddress().getAddress().getHostAddress())) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "<" + kailleraUserImpl4.getName() + "> Is not accepting private messages!"));
                        return;
                    }
                    catch (Exception var11_88) {
                        // empty catch block
                    }
                    return;
                }
                String string4 = stringBuilder.toString();
                kailleraUserImpl3.setLastMsgID(kailleraUserImpl4.getID());
                kailleraUserImpl4.setLastMsgID(kailleraUserImpl3.getID());
                kailleraUserImpl3.getServer().announce("TO: <" + kailleraUserImpl4.getName() + ">(" + kailleraUserImpl4.getID() + ") <" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string4, false, kailleraUserImpl3);
                kailleraUserImpl4.getServer().announce("<" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string4, false, kailleraUserImpl4);
                if (kailleraUserImpl3.getGame() != null) {
                    kailleraUserImpl3.getGame().announce("TO: <" + kailleraUserImpl4.getName() + ">(" + kailleraUserImpl4.getID() + ") <" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string4, kailleraUserImpl3);
                }
                if (kailleraUserImpl4.getGame() == null) return;
                kailleraUserImpl4.getGame().announce("<" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string4, kailleraUserImpl4);
                return;
            }
            catch (NoSuchElementException var8_60) {
                if (kailleraUserImpl3.getLastMsgID() != -1) {
                    try {
                        KailleraUserImpl kailleraUserImpl5 = (KailleraUserImpl)v086ClientHandler.getUser().getServer().getUser(kailleraUserImpl3.getLastMsgID());
                        StringBuilder stringBuilder = new StringBuilder();
                        while (scanner.hasNext()) {
                            stringBuilder.append(scanner.next());
                            stringBuilder.append(" ");
                        }
                        if (kailleraUserImpl5 == null) {
                            try {
                                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                                return;
                            }
                            catch (Exception var11_90) {
                                // empty catch block
                            }
                            return;
                        }
                        if (kailleraUserImpl5 == v086ClientHandler.getUser()) {
                            try {
                                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You can't private message yourself!"));
                                return;
                            }
                            catch (Exception var11_91) {
                                // empty catch block
                            }
                            return;
                        }
                        if (!kailleraUserImpl5.getMsg()) {
                            try {
                                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "<" + kailleraUserImpl5.getName() + "> Is not accepting private messages!"));
                                return;
                            }
                            catch (Exception var11_92) {
                                // empty catch block
                            }
                            return;
                        }
                        String string5 = stringBuilder.toString();
                        kailleraUserImpl3.getServer().announce("TO: <" + kailleraUserImpl5.getName() + ">(" + kailleraUserImpl5.getID() + ") <" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string5, false, kailleraUserImpl3);
                        kailleraUserImpl5.getServer().announce("<" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string5, false, kailleraUserImpl5);
                        if (kailleraUserImpl3.getGame() != null) {
                            kailleraUserImpl3.getGame().announce("TO: <" + kailleraUserImpl5.getName() + ">(" + kailleraUserImpl5.getID() + ") <" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string5, kailleraUserImpl3);
                        }
                        if (kailleraUserImpl5.getGame() != null) {
                            kailleraUserImpl5.getGame().announce("<" + v086ClientHandler.getUser().getName() + "> (" + v086ClientHandler.getUser().getID() + "): " + string5, kailleraUserImpl5);
                        }
                    }
                    catch (Exception var9_79) {
                        try {
                            v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Private Message Error: /msg <UserID> <message>"));
                            return;
                        }
                        catch (Exception var10_84) {
                            // empty catch block
                        }
                        return;
                    }
                }
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Private Message Error: /msg <UserID> <message>"));
                    return;
                }
                catch (Exception var9_80) {
                    // empty catch block
                }
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/ignoreall")) {
            v086ClientHandler.getUser().setIgnoreAll(true);
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", v086ClientHandler.getUser().getName() + " is now ignoring everyone!"));
                return;
            }
            catch (Exception var5_13) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/unignoreall")) {
            v086ClientHandler.getUser().setIgnoreAll(false);
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", v086ClientHandler.getUser().getName() + " is now unignoring everyone!"));
                return;
            }
            catch (Exception var5_14) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().startsWith("/ignore")) {
            Scanner scanner = new Scanner(((Chat)v086Message).getMessage()).useDelimiter(" ");
            try {
                scanner.next();
                int n6 = scanner.nextInt();
                KailleraUserImpl kailleraUserImpl6 = (KailleraUserImpl)v086ClientHandler.getUser().getServer().getUser(n6);
                if (kailleraUserImpl6 == null) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                        return;
                    }
                    catch (Exception var8_61) {
                        // empty catch block
                    }
                    return;
                }
                if (kailleraUserImpl6.getAccess() >= 3) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You cannot ignore an admin/moderator!"));
                        return;
                    }
                    catch (Exception var8_62) {
                        // empty catch block
                    }
                    return;
                }
                if (kailleraUserImpl6 == v086ClientHandler.getUser()) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You can't ignore yourself!"));
                        return;
                    }
                    catch (Exception var8_63) {
                        // empty catch block
                    }
                    return;
                }
                if (!v086ClientHandler.getUser().findIgnoredUser(kailleraUserImpl6.getConnectSocketAddress().getAddress().getHostAddress())) {
                    v086ClientHandler.getUser().addIgnoredUser(kailleraUserImpl6.getConnectSocketAddress().getAddress().getHostAddress());
                    kailleraUserImpl6.getServer().announce(v086ClientHandler.getUser().getName() + " is now ignoring <" + kailleraUserImpl6.getName() + "> ID: " + kailleraUserImpl6.getID(), false, null);
                    return;
                }
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You can't ignore a user that is already ignored!"));
                    return;
                }
                catch (Exception var8_64) {
                    // empty catch block
                }
                return;
            }
            catch (NoSuchElementException var6_42) {
                KailleraUserImpl kailleraUserImpl7 = (KailleraUserImpl)v086ClientHandler.getUser();
                kailleraUserImpl7.getServer().announce("Ignore User Error: /ignore <UserID>", false, kailleraUserImpl7);
                log.info((Object)("IGNORE USER ERROR: " + kailleraUserImpl7.getName() + ": " + v086ClientHandler.getRemoteSocketAddress().getHostName()));
                return;
            }
        }
        if (((Chat)v086Message).getMessage().startsWith("/unignore")) {
            Scanner scanner = new Scanner(((Chat)v086Message).getMessage()).useDelimiter(" ");
            try {
                scanner.next();
                int n7 = scanner.nextInt();
                KailleraUserImpl kailleraUserImpl8 = (KailleraUserImpl)v086ClientHandler.getUser().getServer().getUser(n7);
                if (kailleraUserImpl8 == null) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                        return;
                    }
                    catch (Exception var8_65) {
                        // empty catch block
                    }
                    return;
                }
                if (!v086ClientHandler.getUser().findIgnoredUser(kailleraUserImpl8.getConnectSocketAddress().getAddress().getHostAddress())) {
                    try {
                        v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "You can't unignore a user that isn't ignored!"));
                        return;
                    }
                    catch (Exception var8_66) {
                        // empty catch block
                    }
                    return;
                }
                if (v086ClientHandler.getUser().removeIgnoredUser(kailleraUserImpl8.getConnectSocketAddress().getAddress().getHostAddress(), false)) {
                    kailleraUserImpl8.getServer().announce(v086ClientHandler.getUser().getName() + " is now unignoring <" + kailleraUserImpl8.getName() + "> ID: " + kailleraUserImpl8.getID(), false, null);
                    return;
                }
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "User Not Found!"));
                    return;
                }
                catch (Exception var8_67) {
                    return;
                }
            }
            catch (NoSuchElementException var6_44) {
                KailleraUserImpl kailleraUserImpl9 = (KailleraUserImpl)v086ClientHandler.getUser();
                kailleraUserImpl9.getServer().announce("Unignore User Error: /ignore <UserID>", false, kailleraUserImpl9);
                log.info((Object)("UNIGNORE USER ERROR: " + kailleraUserImpl9.getName() + ": " + v086ClientHandler.getRemoteSocketAddress().getHostName()));
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/rules")) {
            File file = new File("conf/rules.txt");
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String string6 = null;
                while ((string6 = bufferedReader.readLine()) != null) {
                    stringBuffer.append(string6).append(System.getProperty("line.separator"));
                }
            }
            catch (Exception var8_69) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "********Server Rules********"));
            }
            catch (Exception var8_70) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var8_71) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", stringBuffer.toString()));
            }
            catch (Exception var8_72) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
                return;
            }
            catch (Exception var8_73) {
                return;
            }
        }
        if (((Chat)v086Message).getMessage().equals("/help")) {
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/me <message> to make personal message eg. /me is bored ...SupraFast is bored."));
            }
            catch (Exception var5_18) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_19) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/ignore <UserID> or /unignore <UserID> or /ignoreall or /unignoreall to ignore users."));
            }
            catch (Exception var5_20) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_21) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/msg <UserID> <msg> to PM somebody. /msgon or /msgoff to turn on | off."));
            }
            catch (Exception var5_22) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_23) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/myip to get your IP Address."));
            }
            catch (Exception var5_24) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_25) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/version to get server version."));
            }
            catch (Exception var5_26) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_27) {
                // empty catch block
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/rules for the server's rule list."));
            }
            catch (Exception var5_28) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception var5_29) {
                // empty catch block
            }
            if (v086ClientHandler.getUser().getAccess() == 3) {
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/silence <UserID> <Min> to silence a user. 15min max."));
                }
                catch (Exception var5_30) {
                    // empty catch block
                }
                try {
                    Thread.sleep(20);
                }
                catch (Exception var5_31) {
                    // empty catch block
                }
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/kick <UserID> to kick a user."));
                }
                catch (Exception var5_32) {
                    // empty catch block
                }
                try {
                    Thread.sleep(20);
                }
                catch (Exception var5_33) {
                    // empty catch block
                }
            }
            if (v086ClientHandler.getUser().getAccess() >= 4) return;
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "/finduser <Nick> to get a user's info. eg. /finduser sup ...will return SupraFast info."));
            }
            catch (Exception var5_34) {
                // empty catch block
            }
            try {
                Thread.sleep(20);
                return;
            }
            catch (Exception var5_35) {
                // empty catch block
            }
            return;
        }
        if (((Chat)v086Message).getMessage().startsWith("/finduser") && v086ClientHandler.getUser().getAccess() < 4) {
            int n8 = ((Chat)v086Message).getMessage().indexOf(32);
            if (n8 < 0) {
                try {
                    v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "Finduser Error: /finduser <nick> eg. /finduser sup ...will return SupraFast info."));
                    return;
                }
                catch (Exception var6_46) {
                    // empty catch block
                }
                return;
            }
            n = 0;
            string = ((Chat)v086Message).getMessage().substring(n8 + 1);
            iterator = v086ClientHandler.getUser().getUsers().iterator();
        } else {
            kailleraUserImpl.getServer().announce("Unknown Command: " + ((Chat)v086Message).getMessage(), false, kailleraUserImpl);
            return;
        }
        while (iterator.hasNext()) {
            KailleraUserImpl kailleraUserImpl10 = iterator.next();
            if (!kailleraUserImpl10.isLoggedIn() || !kailleraUserImpl10.getName().toLowerCase().contains((CharSequence)string.toLowerCase())) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UserID: ");
            stringBuilder.append(kailleraUserImpl10.getID());
            stringBuilder.append(", Nick: < ");
            stringBuilder.append(kailleraUserImpl10.getName());
            stringBuilder.append(">");
            stringBuilder.append(", Access: ");
            stringBuilder.append(kailleraUserImpl10.getAccessStr());
            if (kailleraUserImpl10.getGame() != null) {
                stringBuilder.append(", GameID: ");
                stringBuilder.append(kailleraUserImpl10.getGame().getID());
                stringBuilder.append(", Game: ");
                stringBuilder.append(kailleraUserImpl10.getGame().getRomName());
            }
            try {
                v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", stringBuilder.toString()));
            }
            catch (Exception var11_94) {
                // empty catch block
            }
            ++n;
        }
        if (n != 0) return;
        try {
            v086ClientHandler.send(new InformationMessage(v086ClientHandler.getNextMessageNumber(), "server", "No Users Found!"));
            return;
        }
        catch (Exception var8_75) {
            return;
        }
    }

    public void handleEvent(ServerEvent serverEvent, V086Controller.V086ClientHandler v086ClientHandler) {
        ++this.handledCount;
        ChatEvent chatEvent = (ChatEvent)serverEvent;
        try {
            if (v086ClientHandler.getUser().searchIgnoredUsers(chatEvent.getUser().getConnectSocketAddress().getAddress().getHostAddress())) {
                return;
            }
            if (v086ClientHandler.getUser().getIgnoreAll() && chatEvent.getUser().getAccess() < 4 && chatEvent.getUser() != v086ClientHandler.getUser()) {
                return;
            }
            String string = chatEvent.getMessage();
            v086ClientHandler.send(new Chat_Notification(v086ClientHandler.getNextMessageNumber(), chatEvent.getUser().getName(), string));
            return;
        }
        catch (MessageFormatException var4_5) {
            log.error((Object)("Failed to contruct Chat_Notification message: " + var4_5.getMessage()), (Throwable)var4_5);
        }
    }
}

