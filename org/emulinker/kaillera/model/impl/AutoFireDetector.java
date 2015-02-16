package org.emulinker.kaillera.model.impl;

import org.emulinker.kaillera.model.KailleraUser;

public interface AutoFireDetector {
   void start(int var1);

   void addPlayer(KailleraUser var1, int var2);

   void addData(int var1, byte[] var2, int var3);

   void stop(int var1);

   void stop();

   void setSensitivity(int var1);

   int getSensitivity();
}
