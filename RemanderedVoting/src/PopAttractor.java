
public class PopAttractor {
	
	public String name;
	public int locX;
	public int locY;
	public int strength;	//this goes from 1 to 100. The default landmap value is 1 for habitable areas.
							//PopAttractors replace the value on landmap at their location with strength. This decreases
							//by one every x or y-step away from PopAttractor. This is the chance% that if
							//placeVoter looks at that spot randomly, the voter will live there.
	public Party[] PartyLean;

	public void setName(String s) {
		this.name = s;
	}
	
	public void makePartyNum(int n) {
		this.PartyLean = new Party[n];
		
	}
	
	public String getName() {
	return this.name;
	}
	
	public void setX(int x) {
		this.locX = x;
	}
	
	public void setY(int y) {
		this.locY = y;
	}
	
	public int getX() {
		return locX;
	}
	
	public int getY() {
		return locY;
	}
	
	public void setStrength(int s) {
		this.strength = s;
	}
	
	public void setLean(Party[] p) {
		this.PartyLean = p;
	}
	
	public void addParty(Party p) {
		for(int i = 0; PartyLean[i]!=null; i++) {	
			this.PartyLean[i] = p;
			System.out.println(p.getName() + " " + p.getStrength());
		}	
	}
	
	public void addPartyS(Party p, int s) {
		for(int i = 0; PartyLean[i]!=null; i++) {
			p.setStrength(s);	
			this.PartyLean[i] = p;
			System.out.println(p.getName() + " " + p.getStrength());
		}	
	}
	
	public Party[] getLean() {
		return this.PartyLean;
	}

	public int getStrength() {
		return strength;
	}
	
	
}
