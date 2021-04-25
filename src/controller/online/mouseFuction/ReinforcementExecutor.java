package controller.online.mouseFuction;

import controller.online.OnlineGameSceneController;
import controller.online.OnlineSceneController;
import model.entities.Territory;
import model.entities.online.RisikoGame;
import model.util.Pixel;

import java.io.IOException;

public class ReinforcementExecutor implements FunctionExecutor {

	@Override
	public void executeClick() throws IOException {
		if(OnlineGameSceneController.getInstance().getSelTerr() != null) {
			OnlineGameSceneController.getInstance().placeTank();
			OnlineGameSceneController.getInstance().setStatusBar();
			OnlineGameSceneController.getInstance().setPlayerStatus();
			OnlineGameSceneController.getInstance().missionControl();

			if(OnlineGameSceneController.getInstance().getGame().getCurrentTurn().getBonusTanks() == 0) {
				if(OnlineGameSceneController.getInstance().getGame().getGamePhase().equals(RisikoGame.GAME_PHASE.BATTLE) && OnlineSceneController.amIaServer == false) {
					return;
				}
				if(OnlineGameSceneController.getInstance().getCurrentPlayer().getBonusTanks() == 0)
				OnlineGameSceneController.getInstance().nextPhase();
			}
		}
	}

	@Override
	public void executeMove(int x, int y) {
		int check = 0;
		for(Territory t : OnlineGameSceneController.getInstance().getTerritories()) {
			check = 0;
			for(Pixel p : OnlineGameSceneController.getInstance().getPixelMap(t)) {
				
				if((p.getX() == x) && (p.getY() == y)) {
					check = 1;
					OnlineGameSceneController.getInstance().setTerritoryLabel(100, t);
					if(OnlineGameSceneController.getInstance().getCurrentPlayer().equals(t.getOwner())) {
						OnlineGameSceneController.getInstance().changeColor(OnlineGameSceneController.getInstance().getPixelMap(t));
						OnlineGameSceneController.getInstance().setSelTerritory(t);
					}
					break;
				} else {
					OnlineGameSceneController.getInstance().resetImage();
					OnlineGameSceneController.getInstance().setTerritoryLabel(0, t);
					OnlineGameSceneController.getInstance().setSelTerritory(null);
				}
					
			}
			if(check == 1) {
				break;
			}
		}
		
	}

}
