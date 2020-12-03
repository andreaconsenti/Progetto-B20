package controller.online.mouseFuction;

import controller.GameSceneController;
import model.entities.Territory;
import model.util.Pixel;

public class BattleExecutor implements FunctionExecutor {

	@Override
	public void executeClick() {
		if(GameSceneController.getInstance().getTerritory1() == null) {
			GameSceneController.getInstance().setTerritory12(GameSceneController.getInstance().getSelTerr(), null);
			GameSceneController.getInstance().setStatusBar();
			GameSceneController.getInstance().setPlayerStatus();
			
		} else if(GameSceneController.getInstance().getTerritory2() == null) {
			if(GameSceneController.getInstance().getSelTerr() == null || GameSceneController.getInstance().getSelTerr().equals(GameSceneController.getInstance().getTerritory1())) {
				GameSceneController.getInstance().setTerritory12(GameSceneController.getInstance().getSelTerr(), null);
				GameSceneController.getInstance().setStatusBar();
				GameSceneController.getInstance().setPlayerStatus();
			} else {
				GameSceneController.getInstance().setTerritory2(GameSceneController.getInstance().getSelTerr());
				GameSceneController.getInstance().attackerAndDefenderChosen();
				GameSceneController.getInstance().updateTanks();
				GameSceneController.getInstance().missionControl();
				GameSceneController.getInstance().setTerritory12(null, null);
				GameSceneController.getInstance().setStatusBar();
				GameSceneController.getInstance().setPlayerStatus();
			}
		} else {
			GameSceneController.getInstance().setTerritory12(null, null);
			GameSceneController.getInstance().setStatusBar();
			GameSceneController.getInstance().setPlayerStatus();
		}
	}

	@Override
	public void executeMove(int x, int y) {
		int check = 0;
		
		if(GameSceneController.getInstance().getTerritory1() == null) {
			for(Territory t : GameSceneController.getInstance().getTerritories()) {
				check = 0;
				for(Pixel p : GameSceneController.getInstance().getPixelMap(t)) {
					if((p.getX() == x) && (p.getY() == y)) {
						check = 1;
						GameSceneController.getInstance().setTerritoryLabel(100, t);
						if(GameSceneController.getInstance().getCurrentPlayer().equals(t.getOwner()) && t.getTanks() > 1 ) {
							GameSceneController.getInstance().changeColor(GameSceneController.getInstance().getPixelMap(t));
							GameSceneController.getInstance().setSelTerritory(t);
						}
						break;
					} else {
						GameSceneController.getInstance().resetImage();
						GameSceneController.getInstance().setTerritoryLabel(0, t);
						GameSceneController.getInstance().setSelTerritory(null);
					}
				}
				if( check == 1 ) {
					break;
				}
			}
		} else if(GameSceneController.getInstance().getTerritory2() == null) {
			for(Territory t : GameSceneController.getInstance().getTerritories()) {
				check = 0;
				for(Pixel p : GameSceneController.getInstance().getPixelMap(t)) {
					if((p.getX() == x) && (p.getY() == y) && !t.equals(GameSceneController.territory1)) {
						check = 1;
						GameSceneController.getInstance().setTerritoryLabel(100, t);
						if(t.isAttaccabileFrom(GameSceneController.territory1)) {
							GameSceneController.getInstance().changeColor(GameSceneController.getInstance().getPixelMap(t));
							GameSceneController.getInstance().setSelTerritory(t);
						}
						break;
					} else {
						GameSceneController.getInstance().resetImage();
						GameSceneController.getInstance().setTerritoryLabel(0, t);
						GameSceneController.getInstance().setSelTerritory(null);
					}
				}
				if( check == 1 ) {
					break;
				}
			}
			
		} else {
			GameSceneController.getInstance().resetImage();
		}
	}
}