public class Ballot {

	
	public Voter voter;					//who cast this ballot?
	public Voter[] Candidates;		//who's on it, and did they get voted for?
	public Voter[] chosen; 			//who did they chose, 
	public int dropped = -1;				//what round were they dropped on?
	public boolean elected;			//and has someone chosen been elected? this is the 'deprecate' check
	
	public boolean getElected() {
		return this.elected;
	}
	
	public void setElected(boolean b) {
		this.elected = b;
		
	}
	
	public void setDropped(int d) {
		this.dropped = d;
	}
	
	public int getDropped() {
		return dropped;
	}
	
	public void voteFor(int index, int vote) {
		this.Candidates[index].votedFor(vote);
		this.chosen[vote-1] = this.Candidates[index];
		
	//	System.out.println(this.voter.getName()+" just voted for "+this.chosen[vote-1].getName()+" in round "+ this.chosen[vote-1].getRound());
		
	}
	public int getTenacity() {
		return voter.getTenacity();
	}
	
	public void setChosen(int c) {
		this.chosen = new Voter[c];
		
	}
	
	public Voter[] getChosen() {
		return this.chosen;
	}
	
	public String getRoundChosenName(int r) {
		return this.chosen[r].getName();
		
	}
	
	public boolean wasChosen(Voter v) {
		boolean here = false;
		for(int i = 0; i < this.chosen.length; i++) {
		
			if(this.chosen[i] == v) {
				
				here = true;
			}
	
		}
		return here;
	}
	
	public void setCandidates(Voter[] v) {
		this.Candidates = v;
	}
	
	public Voter[] getCandidates() {
		return this.Candidates;
	}
	
	public void setVoter(Voter v) {
		this.voter = v; 
	}
	
	public Voter getVoter() {
		return this.voter;
	}
	
	public void reduceTenacity() {
		voter.setTenacity(voter.getTenacity() - 1);
	}
	
	public boolean isTenacious() {

		if(voter.getTenacity() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public void shareBallot(int n) {		//share the first n candidates chosen
		System.out.print(this.voter.getName()+" voted for ");
		
		
		for(int i = 0; i < this.chosen.length && i < n; i++) {
	
			String s = this.chosen[i].getName();
			
			System.out.print(s+" in round " + (i)+ ", ");
		}
		
		System.out.println();
	}


	
}
