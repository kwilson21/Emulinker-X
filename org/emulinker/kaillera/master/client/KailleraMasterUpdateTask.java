package org.emulinker.kaillera.master.client;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.connectcontroller.ConnectController;
import org.emulinker.kaillera.master.PublicServerInformation;
import org.emulinker.kaillera.master.StatsCollector;
import org.emulinker.kaillera.master.client.MasterListUpdateTask;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.release.ReleaseInfo;

public class KailleraMasterUpdateTask implements MasterListUpdateTask {
   private static Log log = LogFactory.getLog(KailleraMasterUpdateTask.class);
   private PublicServerInformation publicInfo;
   private ConnectController connectController;
   private KailleraServer kailleraServer;
   private StatsCollector statsCollector;
   private HttpClient httpClient;
   private ReleaseInfo releaseInfo;

   public KailleraMasterUpdateTask(PublicServerInformation publicInfo, ConnectController connectController, KailleraServer kailleraServer, StatsCollector statsCollector, ReleaseInfo releaseInfo) {
      this.publicInfo = publicInfo;
      this.connectController = connectController;
      this.kailleraServer = kailleraServer;
      this.releaseInfo = releaseInfo;
      this.statsCollector = statsCollector;
      this.publicInfo = publicInfo;
      this.httpClient = new HttpClient();
      this.httpClient.setConnectionTimeout(5000);
      this.httpClient.setTimeout(5000);
   }

   public void touchMaster() {
      List createdGamesList = this.statsCollector.getStartedGamesList();
      StringBuilder createdGames = new StringBuilder();
      Iterator params;
      synchronized(createdGamesList) {
         params = createdGamesList.iterator();

         while(true) {
            if(!params.hasNext()) {
               createdGamesList.clear();
               break;
            }

            createdGames.append(params.next());
            createdGames.append("|");
         }
      }

      StringBuilder waitingGames = new StringBuilder();
      params = this.kailleraServer.getGames().iterator();

      while(params.hasNext()) {
         KailleraGame kailleraTouch = (KailleraGame)params.next();
         if(kailleraTouch.getStatus() == 0) {
            waitingGames.append(kailleraTouch.getID());
            waitingGames.append("|");
            waitingGames.append(kailleraTouch.getRomName());
            waitingGames.append("|");
            waitingGames.append(kailleraTouch.getOwner().getName());
            waitingGames.append("|");
            waitingGames.append(kailleraTouch.getOwner().getClientType());
            waitingGames.append("|");
            waitingGames.append(kailleraTouch.getNumPlayers());
            waitingGames.append("|");
         }
      }

      NameValuePair[] params1 = new NameValuePair[]{new NameValuePair("servername", this.publicInfo.getServerName()), new NameValuePair("port", Integer.toString(this.connectController.getBindPort())), new NameValuePair("nbusers", Integer.toString(this.kailleraServer.getNumUsers())), new NameValuePair("maxconn", Integer.toString(this.kailleraServer.getMaxUsers())), new NameValuePair("version", "EMX" + this.releaseInfo.getVersionString()), new NameValuePair("nbgames", Integer.toString(this.kailleraServer.getNumGames())), new NameValuePair("location", this.publicInfo.getLocation()), new NameValuePair("ip", this.publicInfo.getConnectAddress()), new NameValuePair("url", this.publicInfo.getWebsite())};
      GetMethod kailleraTouch1 = new GetMethod("http://www.kaillera.com/touch_server.php");
      kailleraTouch1.setQueryString(params1);
      kailleraTouch1.setRequestHeader("Kaillera-games", createdGames.toString());
      kailleraTouch1.setRequestHeader("Kaillera-wgames", waitingGames.toString());

      try {
         int e = this.httpClient.executeMethod(kailleraTouch1);
         if(e != 200) {
            log.error("Failed to touch Kaillera Master: " + kailleraTouch1.getStatusLine());
         } else {
            log.info("Touching Kaillera Master done");
         }
      } catch (Exception var16) {
         log.error("Failed to touch Kaillera Master: " + var16.getMessage());
      } finally {
         if(kailleraTouch1 != null) {
            try {
               kailleraTouch1.releaseConnection();
            } catch (Exception var15) {
               ;
            }
         }

      }

   }
}
