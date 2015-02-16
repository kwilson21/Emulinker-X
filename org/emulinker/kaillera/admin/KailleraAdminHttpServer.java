package org.emulinker.kaillera.admin;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.connectcontroller.ConnectController;
import org.emulinker.kaillera.model.KailleraServer;
import org.mortbay.http.HashUserRealm;
import org.mortbay.http.SecurityConstraint;
import org.mortbay.http.handler.NotFoundHandler;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.http.handler.SecurityHandler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.util.InetAddrPort;
import org.picocontainer.Startable;

public class KailleraAdminHttpServer implements Startable {
   private static Log log = LogFactory.getLog(KailleraAdminHttpServer.class);
   protected Server appServer = new Server();

   public KailleraAdminHttpServer(Configuration config, ThreadPoolExecutor threadPool, ConnectController connectController, KailleraServer kailleraServer) throws Exception {
      int port = config.getInt("adminserver.httpport");
      String jspDir = config.getString("adminserver.jspdir");
      this.appServer = new Server();
      this.appServer.addListener(new InetAddrPort(port));
      ServletHttpContext context = (ServletHttpContext)this.appServer.getContext(jspDir);
      context.setAttribute("threadPool", threadPool);
      context.setAttribute("connectController", connectController);
      context.setAttribute("kailleraServer", kailleraServer);
      this.setupSecurity(context, config);
      context.addHandler(new ServletHandler());
      context.addHandler(new ResourceHandler());
      context.addHandler(new NotFoundHandler());
      context.setResourceBase(jspDir);
      context.addServlet("JSP", "*.jsp", "org.apache.jasper.servlet.JspServlet");
   }

   private void setupSecurity(ServletHttpContext context, Configuration config) throws IOException {
      boolean isSecurity = config.getBoolean("adminserver.authenticate", false);
      if(isSecurity) {
         log.info("Configuring admin server security.");
         String realmName = config.getString("adminserver.realmname");
         if(realmName == null || realmName.trim().length() == 0) {
            realmName = "Emulinker";
         }

         String userFile = config.getString("adminserver.userfile");
         if(userFile == null || userFile.trim().length() == 0) {
            userFile = "conf/user.properties";
         }

         log.info("Establishing realm " + realmName);
         log.info("Loading usernames and passwords from " + userFile);
         SecurityHandler sh = new SecurityHandler();
         sh.setAuthMethod("BASIC");
         sh.setName(realmName);
         context.addHandler(sh);
         SecurityConstraint sc = new SecurityConstraint();
         sc.setAuthenticate(true);
         sc.setName("BASIC");
         sc.addRole("*");
         context.addSecurityConstraint("/*", sc);
         HashUserRealm hur = new HashUserRealm();
         hur.setName(realmName);
         hur.load(userFile);
         context.setRealm(hur);
      } else {
         log.info("Admin server security is disabled.");
      }

   }

   public void start() {
      log.info("Starting Web-based Admin Interface.");
      if(!this.appServer.isStarted()) {
         try {
            this.appServer.start();
         } catch (Exception var2) {
            log.error("Failed to start admin server: " + var2.getMessage(), var2);
         }
      }

   }

   public void stop() {
      log.info("Stoping!");
      if(this.appServer.isStarted()) {
         try {
            this.appServer.stop();
         } catch (Exception var2) {
            log.error("Failed to stop admin server: " + var2.getMessage(), var2);
         }
      }

   }
}
