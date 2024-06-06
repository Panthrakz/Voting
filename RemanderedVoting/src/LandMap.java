
public class LandMap {
	
	
	public int width;
	public int height;
	public int[][] Inhabited;
	
	
	public void setX(int wide) {
		
		this.width = wide;
		
	}
	
	public void setY(int high) {
		
		this.height = high;
		
	}
	
	public void setInhabited() {
		this.Inhabited = new int[width][height];
	}
	
	public int[][] getMap() {
		
		return this.Inhabited;
	}
	
	public int getXY(int x, int y) {
		return this.Inhabited[x][y];
		
	}
	
	public void onesMap() {
		
		for(int i=0; i<width; i++) {
			
			for(int j=0; j<height; j++) {
			
				Inhabited[i][j] = 1;
				
			}
			
		}
		
	}

	public int getX() {
		return width;
	}
	
	public int getY() {
		return height;
	}
	
	public void addAttractors(PopAttractor[] p) {		//landmap needs to have the strength of each popattractor at their locations,
														//then decrease by one for every x or y distance from the nearest attractor.
		for(int n = 0; n < p.length; n++) {
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				
				int s = Inhabited[i][j];
				
				if(s==0) {
					j++;
				}
				else {
											//city strength - max(your x distance, your y distance)
					int t =  Math.max(1, (p[n].getStrength() - Math.max(    Math.abs(i-p[n].getX())   ,  Math.abs(j-p[n].getY()))    ));
				
					if(t > s) {
							
							Inhabited[i][j] = Math.min(t, 100);
					
					}	
				}
			}
		}
		
		}
	}
	
}
