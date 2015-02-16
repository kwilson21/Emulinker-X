package org.emulinker.util;

public interface Executable extends Runnable {
   boolean isRunning();

   void stop();
}
