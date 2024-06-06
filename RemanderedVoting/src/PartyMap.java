
public class PartyMap {

	public Party[][][] map;
	public Party[] list;
	
	public void setParties(int n) {
		this.list = new Party[n];
		
	}
	
	public void addParty(Party p) {
		for(int i = 0; i < this.list.length; i++) {
			if(this.list[i] == null) {
				this.list[i] = p;
				i = this.list.length;
			}
		}
	}
	
	public void setXY(int x, int y) {
		this.map = new Party[x][y][list.length];
	}

	public Party[][][] getMap() {
		return this.map;
		
	}
	
	public Party[] getList() {
		return this.list;
	}
	
	public void setBaseline() {
		for(int i = 0; i < this.map.length; i++) {
			for(int j = 0; j < this.map[0].length; j++) {
				for(int k = 0; k < this.map[0][0].length; k++) {
					this.map[i][j][k] = list[k];
				}
			}
		}
		
	}
	
	public void useAttractors(PopAttractor[] p) {
		//list contains baseline party strengths already; let's see how 'far' each attractor is from baseline, or from map?
		
		for(int n = 0; n < p.length; n++) {
			
			//for each attractor

			
			//then for each party
			
		for(int i = 0; i < this.map.length; i++) {
			for(int j = 0; j < this.map[0].length; j++) {
				
				int dist = 0;
				
				for(int q = 0; q < p[n].getLean().length; q++) {
				
					
					
					dist += Math.abs(p[n].getLean()[q].getStrength() - this.map[i][j][q].getStrength());
					//compare party strengths for attractors in p[n] to baseline in this.map
					//dist is the total difference between party strengths at this point and what the popattractor is asking for
				}
			
				if(dist < Math.max(Math.abs(p[n].getX() -i),Math.abs(p[n].getY()-j))) {  //am I so far from a city that there's no adjustment to be made?
					j++;
				}
				else {
					int add = dist - Math.max(Math.abs(p[n].getX() -i),Math.abs(p[n].getY()-j)); //if we're within dist of a popattractor, how many point (add) DO we get to distribute?
					
						for(int z = 0; z < add; z++) {
							
							int[] diffs = new int[p.length];
				
							if(add - z % 2 ==0) {			//if I have an even number of points left to distribute, then one party has to go up, and another needs to go down
								
								for(int y = 0; y < diffs.length; y++) {
									diffs[y] = p[n].getLean()[z % list.length].getStrength() - this.map[i][j][z % list.length].getStrength(); //what's the difference for a specific party
								}
								
								for(int w = 0; w < diffs.length; w++) {
									if(diffs[w]<0) {
										this.map[i][j][z % list.length].setStrength( this.map[i][j][z % list.length].getStrength() - 1 ); //if the city should be low, lower it!
										w = diffs.length;
									}
								}
								for(int v = 0; v < diffs.length; v++) {
									if(diffs[v]>0) {
										this.map[i][j][z % list.length].setStrength( this.map[i][j][z % list.length].getStrength() + 1 ); //if the party should be high, raise it!
										v = diffs.length;
									}
								}
								
							z = z+2;
								
							}
							
							else {
								z++;
							}
						}						//we add or subtract a number of times equal to how 'off' the total party lean of the map is from what the attractor
												//wants, based on how far away it is
				}
				
			}
		}
		
		}
		
		
		
	}
	
	
}
