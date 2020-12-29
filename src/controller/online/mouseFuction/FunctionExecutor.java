package controller.online.mouseFuction;

import java.io.IOException;

public interface FunctionExecutor {
	
	public void executeClick() throws IOException;
	
	public void executeMove(int x, int y);
	
}
