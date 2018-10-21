package com.playtech.ptargame4.server.task.lobby;

import com.playtech.ptargame4.api.lobby.HostTableGameRequest;
import com.playtech.ptargame4.api.lobby.HostTableGameResponse;
import com.playtech.ptargame4.api.lobby.Team;
import com.playtech.ptargame4.api.table.SetUserOnMapRequest;
import com.playtech.ptargame4.api.token.TokenLocationUpdateMessage;
import com.playtech.ptargame4.api.token.TokenType;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.server.ContextConstants;
import com.playtech.ptargame4.server.task.AbstractLogic;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class HostTableGameLogic extends AbstractLogic {

	private static final Logger logger = Logger.getLogger(HostTableGameLogic.class.getName());

	public static final AtomicInteger count = new AtomicInteger();

	public HostTableGameLogic(LogicResources logicResources) {
		super(logicResources);
	}

	@Override
	public void execute(Task task) {
		logger.info("Executing HostTableGameLogic task");
		HostTableGameRequest request = getInputRequest(task, HostTableGameRequest.class);
		String clientId = request.getHeader().getClientId();
		String name = getLogicResources().getClientRegistry().getName(clientId);
		boolean tableGame = getLogicResources().getClientRegistry().isTableSession(clientId);
		String gameId = getLogicResources().getGameRegistry().createGame(clientId, name + " " + count.incrementAndGet(),
				4, false, "", tableGame);
		HostTableGameResponse response = getResponse(task, HostTableGameResponse.class);
		response.setGameId(gameId);
		task.getContext().put(ContextConstants.RESPONSE, response);
	}

	@Override
	public void finishSuccess(Task task) {
		super.finishSuccess(task);
		HostTableGameResponse response = task.getContext().get(ContextConstants.RESPONSE, HostTableGameResponse.class);
		getLogicResources().getCallbackHandler().sendMessage(response);
		logger.info("Finished successfully HostTableGameLogic task, gameId=" + response);
	}

	@Override
	public void finishError(Task task, Exception e) {
		super.finishError(task, e);
		HostTableGameResponse response = getResponse(task, HostTableGameResponse.class, e);
		getLogicResources().getCallbackHandler().sendMessage(response);
		logger.info("Failed HostTableGameLogic task: " + e.getMessage());
	}

}
