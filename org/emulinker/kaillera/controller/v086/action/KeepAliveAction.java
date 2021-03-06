package org.emulinker.kaillera.controller.v086.action;

import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.protocol.*;

public class KeepAliveAction implements V086Action
{
	//private static Log				log			= LogFactory.getLog(KeepAliveAction.class);
	private static final String		desc		= "KeepAliveAction";
	private static KeepAliveAction	singleton	= new KeepAliveAction();

	public static KeepAliveAction getInstance()
	{
		return singleton;
	}

	private int	actionCount	= 0;

	private KeepAliveAction()
	{

	}

	public int getActionPerformedCount()
	{
		return actionCount;
	}

	public String toString()
	{
		return desc;
	}

	public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException
	{
		actionCount++;
		clientHandler.getUser().updateLastKeepAlive();
	}
}

