package controller.online.mouseFuction;

import controller.GameSceneController;
import controller.online.OnlineGameSceneController;
import model.entities.Territory;
import model.util.Pixel;

import java.io.IOException;

public class FirstturnExecutor implements FunctionExecutor {

	@Override
	public void executeClick() throws IOException {
	if(OnlineGameSceneController.getInstance().getSelTerr() != null) {
		OnlineGameSceneController.getInstance().placeTank();
		OnlineGameSceneController.getInstance().setStatusBar();
		OnlineGameSceneController.getInstance().setPlayerStatus();
		OnlineGameSceneController.getInstance().setSelTerritory(null);
		OnlineGameSceneController.getInstance().resetImage();
		OnlineGameSceneController.getInstance().nextTurn();
		OnlineGameSceneController.getInstance().firstPhaseEnded();
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