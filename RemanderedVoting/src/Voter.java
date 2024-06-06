public class Voter {

	public int locX;
	public int locY;
	public Party party;
	public int locLoyalty;			//this is the floor% that the voter will choose a candidate from far away - high locloyalty is LOW location loyalty!
	public int parLoyalty;
	public boolean isCandidate;
	public String name;
	public int votedRound; //if voted for, this is the round they were chosen by a particular voter
	public int tenacity;
	public int electedRound = -1; //if elected, this is when it happened
	public int[] received; //votes received, by round
	public long support; //how much weighted support a candidate received
	public int votes; //how many undeprecated votes a candidate has received
	public boolean winner = false;
	public int index;
	
	public void setIndex(int x) {
		this.index = x;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean didTheyWin() {
		return winner;
	}
	
	public void winTheyDidnt() {
		this.winner = false;
	}
	
	public void winTheyDid() {
		this.winner = true;
	}
	
	public int getVotes() {
		return this.votes;
	}
	
	public void setVotes(int v) {
		this.votes = v;
	}
	
	public void setSupport(long sss) {
		this.support = sss;
	}
	
	public void addSupport(long sup) {
		this.support += sup;
	}
	
	public long getSupport() {
		return this.support;
	}
	
	public void setReceivedLength(int r) {
		this.received = new int[r];
	}
	
	public void setReceived(int i, int j) {
		this.received[i] = j;
	}
	
	public void addReceived(int i) {
		this.received[i] +=1;
	}
	
	public int getReceived(int i) {
		return this.received[i];
	}
	
	public void setElectedRound(int r) {
		this.electedRound = r;
	}
	
	public int getElectedRound() {
		return this.electedRound;
	}
	
	public Party getParty() {
		return this.party;
	}
	public String getName() {
		return this.name;
	}
	
	public int getTenacity() {
		return this.tenacity;
	}
	
	public void setTenacity(int t) {
		this.tenacity = t;
	}
	
	public int getRound() {
		return this.votedRound;
	}

	
	public void votedFor(int r) {
		this.votedRound = r;
	}
	
	public boolean isCandidate() {
		return this.isCandidate;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void makeCandidate() {
		this.isCandidate = true;
		this.electedRound = -1;
	}
	
	public void setX(int x) {
		this.locX = x;
	}
	
	public void setY(int y) {
		this.locY = y;
	}
	
	public int getX() {
		return this.locX;
	}
	
	public int getY() {
		return this.locY;
	}
	
	public void setParty(Party p) {
		this.party = p;
	}
	
	public int getpar() {
		return this.parLoyalty;
	}
	
	public int getloc() {
		return this.locLoyalty;
	}
	
	public void locLoyalty(int ll) {
		this.locLoyalty = ll;
	}
	
	public void parLoyalty(int pl) {
		this.parLoyalty = pl;
	}
	
	public void placeParty(PartyMap p) {
		Party[][][] map = p.getMap();
		
		Party[] newlist = new Party[p.getList().length];
		for(int i = 0; i < p.getList().length; i++) {
			newlist[i] = map[locX][locY][i];
		}		//now I have newlist with all the parties with correct strengths at Voter's location
		
				//wheel has 100 parties filled in for a voter to choose from!
		
		int choice = (int) (Math.floor(Math.random() *100));
		
		for(int k = 0; k <newlist.length; k++) {
			choice = choice - newlist[k].getStrength();
				if(choice < 0) {
					this.party = newlist[k];
					k = newlist.length;
				}
		}		
	}
	
	public void placeVoter(LandMap l) {
		int X = l.getX();		//total map size
		int Y = l.getY();
		
		this.locX = -1;
		
		int[][] L = l.getMap();
		
		while(locX == -1) {
		
		int x = (int) (Math.floor(Math.random() *(X - 1)));		//where are we looking on the map?
		int y = (int) (Math.floor(Math.random() *(Y - 1)));
		
		int dice = (int) Math.floor(Math.random() *100);		//what did we roll?
		int odds = L[x][y];										//what are the odds of settling?
			
	//	System.out.println(this.name+" rolls a "+dice+" against a "+odds+"% chance to live at "+x+" and "+y);
		
			if(dice < odds) {
				this.locX = x;
				this.locY = y;
				
			}
		
		}
		
		
	}
	
}
