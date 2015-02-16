package org.emulinker.kaillera.master;

import org.apache.commons.configuration.Configuration;

public class PublicServerInformation {
   private String serverName;
   private String serverLocation;
   private String serverWebsite;
   private String serverAddress;

   public PublicServerInformation(Configuration config) {
      this.serverName = config.getString("masterList.serverName", "Emulinker Server");
      this.serverLocation = config.getString("masterList.serverLocation", "Unknown");
      this.serverWebsite = config.getString("masterList.serverWebsite", "");
      this.serverAddress = config.getString("masterList.serverConnectAddress", "");
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getLocation() {
      return this.serverLocation;
   }

   public String getWebsite() {
      return this.serverWebsite;
   }

   public String getConnectAddress() {
      return this.serverAddress;
   }
}
