
public class PopMap {
	
	public int width;
	public int height;
	public int depth;
	public Voter[][][] Inhabited;
	
	
	public void setX(int wide) {
		
		this.width = wide;
		
	}
	
	public void setY(int high) {
		
		this.height = high;
		
	}
	
	public void setZ(int deep) {
		
		this.depth = deep;
	}

	public int getX() {
		return this.width;
	}
	
	public int getY() {
		return this.height;
	}
	
	public int getZ() {
		return this.depth;
	}

	public void setInhabited() {
		this.Inhabited = new Voter[width][height][depth];
	}
	
	public Voter[][][] getVoters(){
		return this.Inhabited;
	}
	
	
	public void addVoter(Voter v, int i, int j, int k) {
		Inhabited[i][j][k] = v;
	}
}
