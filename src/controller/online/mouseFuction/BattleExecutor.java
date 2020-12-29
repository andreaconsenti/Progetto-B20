package controller.online.mouseFuction;

import controller.online.OnlineGameSceneController;
import model.entities.Territory;
import model.util.Pixel;

public class BattleExecutor implements FunctionExecutor {

	@Override
	public void executeClick() {
		if(OnlineGameSceneController.getInstance().getTerritory1() == null) {
			OnlineGameSceneController.getInstance().setTerritory12(OnlineGameSceneController.getInstance().getSelTerr(), null);
			OnlineGameSceneController.getInstance().setStatusBar();
			OnlineGameSceneController.getInstance().setPlayerStatus();
			
		} else if(OnlineGameSceneController.getInstance().getTerritory2() == null) {
			if(OnlineGameSceneController.getInstance().getSelTerr() == null || OnlineGameSceneController.getInstance().getSelTerr().equals(OnlineGameSceneController.getInstance().getTerritory1())) {
				OnlineGameSceneController.getInstance().setTerritory12(OnlineGameSceneController.getInstance().getSelTerr(), null);
				OnlineGameSceneController.getInstance().setStatusBar();
				OnlineGameSceneController.getInstance().setPlayerStatus();
			} else {
				OnlineGameSceneController.getInstance().setTerritory2(OnlineGameSceneController.getInstance().getSelTerr());
				OnlineGameSceneController.getInstance().attackerAndDefenderChosen();
				OnlineGameSceneController.getInstance().updateTanks();
				OnlineGameSceneController.getInstance().missionControl();
				OnlineGameSceneController.getInstance().setTerritory12(null, null);
				OnlineGameSceneController.getInstance().setStatusBar();
				OnlineGameSceneController.getInstance().setPlayerStatus();
			}
		} else {
			OnlineGameSceneController.getInstance().setTerritory12(null, null);
			OnlineGameSceneController.getInstance().setStatusBar();
			OnlineGameSceneController.getInstance().setPlayerStatus();
		}
	}

	@Override
	public void executeMove(int x, int y) {
		int check = 0;
		
		if(OnlineGameSceneController.getInstance().getTerritory1() == null) {
			for(Territory t : OnlineGameSceneController.getInstance().getTerritories()) {
				check = 0;
				for(Pixel p : OnlineGameSceneController.getInstance().getPixelMap(t)) {
					if((p.getX() == x) && (p.getY() == y)) {
						check = 1;
						OnlineGameSceneController.getInstance().setTerritoryLabel(100, t);
						if(OnlineGameSceneController.getInstance().getCurrentPlayer().equals(t.getOwner()) && t.getTanks() > 1 ) {
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
				if( check == 1 ) {
					break;
				}
			}
		} else if(OnlineGameSceneController.getInstance().getTerritory2() == null) {
			for(Territory t : OnlineGameSceneController.getInstance().getTerritories()) {
				check = 0;
				for(Pixel p : OnlineGameSceneController.getInstance().getPixelMap(t)) {
					if((p.getX() == x) && (p.getY() == y) && !t.equals(OnlineGameSceneController.territory1)) {
						check = 1;
						OnlineGameSceneController.getInstance().setTerritoryLabel(100, t);
						if(t.isAttaccabileFrom(OnlineGameSceneController.territory1)) {
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
				if( check == 1 ) {
					break;
				}
			}
			
		} else {
			OnlineGameSceneController.getInstance().resetImage();
		}
	}
}