package org.emulinker.release;

public interface ReleaseInfo {
   String getProductName();

   int getMajorVersion();

   int getMinorVersion();

   int getBuildNumber();

   String getReleaseDate();

   String getVersionString();

   String getLicenseInfo();

   String getWebsiteString();

   String getWelcome();
}
