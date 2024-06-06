
public class TallyTwo {
	
	public Voter[] candidates;	//all candidates
	public Voter[] winners;
	public int[][][] votes;
	public double thresh; //votes needed to win
	public Voter[] backups; //we will put candidates who did not win outright here, in order of support
	
	public void setThreshhold(double d) {
		this.thresh = d;	
	}
	
	public void countBallots(Ballot[] b, int seats) {		
		
		//this method takes in the stack of ballots, and is told how many seats there are to fill.
		//It figures out how many
		
		winners = new Voter[seats];	//we will only fill this array with candidates who have won
	//	backups = new Voter[this.candidates.length];
		
		int chumps = b[0].getCandidates().length;	//reads the first ballot to figure out how many candidates - chumps = #of candidates
		this.candidates = new Voter[chumps];		//populate the list of possible candidates
		this.votes = new int[chumps][chumps][chumps+1];		//and make a home to tally their votes for each round; the second floor is for undeprecated votes

		for(int i = 0; i < this.candidates.length; i++) {	//put the candidates in the list
			this.candidates[i] = b[0].getCandidates()[i];	// 
			this.candidates[i].setIndex(i);
		}
		
		for(int l = 0; l < chumps; l++) {				//then, set the dropped round to chumps - we're intending to count them
			b[l].setDropped(chumps);					//all, at first
		}
		
//going round by round, countBallots increments votes([][][]) for each candidate, and checks for wins, then deprecates, in that order. 
		
		for(int r = 0; r < seats; r++) { 	//this line presumes that there are as many rounds of counting as there are available seats, at maximum,
												//and that voters select as many candidates as seats, at max, and we keep counting ALL BALLOTS
			
			for(int v = 0; v < b.length; v++) {  //go through all b.length ballots (v = ballot number, or voter number)
			
				boolean isPleased = b[v].getElected();
				
				for(int x = 0; x <this.candidates.length; x++) { //x is the candidate 
	
					if(b[v].getRoundChosenName(r) == candidates[x].getName()) { //increment the candidates tally in 'candidates' if it's undeprecated,
																						//in votes [0] as well, 
						if(isPleased == false) {
							candidates[x].addReceived(r);
							votes[x][r][0] +=1;
//System.out.println(b[v].getVoter().getName()+" voted for "+x+" who now has "+votes[x][r][0]);;
						}
											
						for(int j = 0; j < seats; j++) {			//go through each round on votes[][][], and add the vote
											
							if(b[v].getDropped() == j) {		//increment candidates if undeprecated (get.Elected == false), and votes[x][r][getDropped]
																	//'...and give him a vote in this round'
								votes[x][r][j] +=1;					//level 0 (votes[x][r][0] is for undeprecated only							
							}						
						}
					}
				}
			}		
			for(int i = 0; i < candidates.length; i++) {
				for(int j = 0; j < seats; j++) {
					System.out.print(votes[i][j][0]+" ");
				}
				System.out.println();
			}
			if(winners[winners.length-1] == null) {
			checkVictory(votes, b, thresh, r, seats);
			b = deprecateBallots(b, r);
			}
		}
	}
	
	public void checkVictory(int[][][] v, Ballot[] b, double t, int r, int seats) {	
		
		//take in all votes, look at the [0]th level of v, and make sure that winners[] is populated by candidates who have won.
		//on any round, for all candidates, check if they already won (electedRound != null), skip them if they did, and setSupport
		//for the rest.
		
		int el = 0;				  //let's keep track of how many candidates have been elected on any previous round
		
		for(int i = 0; i < candidates.length; i++){			//go through each candidate
						//if they aren't already elected, for round by round adding up votes, and incrementing support as you do in case of tie-breakers
			if(candidates[i].getElectedRound() > -1) {
				el++;
			}
			
			if(candidates[i].getElectedRound() == -1) {	//setElected is -1 by default, and if elected first-round, gets set to 0
				candidates[i].setReceived(r, v[i][r][0]);						//increment the round-by-round tally
				candidates[i].setVotes(v[i][r][0] + candidates[i].getVotes()); 	//increment the total undeprecated votes received this round
//System.out.println(candidates[i].getName()+" has "+candidates[i].getVotes()+" votes after round "+r);
			}
			
			if(r<=1) {			//for the first 2 rounds, setsupport for each candidate in case of tie-breaking
				candidates[i].setSupport(candidates[i].getReceived(0)*b.length  + candidates[i].getReceived(1));
			}	
			
			if((candidates[i].getVotes() > t) && (candidates[i].getElectedRound() == -1)) {			//if they have the votes over threshhold, say they got elected			
				candidates[i].setElectedRound(r);
System.out.println(candidates[i].getName()+" was elected on round "+r+" with "+candidates[i].getVotes()+" votes total, and "+candidates[i].getSupport()+" support.");
			}
		}
		
		// now go through each candidate again, knowing their total received votes, support on the first two rounds, and see if they won outright, and against each other
		
		for(int j = 0; j < candidates.length; j++) {			//go through every candidate
			for(int w = el; w < seats; w++) {					//and compare them to all winners
				
				if((candidates[j].getElectedRound() == r)&&(candidates[j].didTheyWin()==false)) { 		//only sweat the elected candidates

					if(winners[w] == null) {					//if there's an empty spot, fill it
						candidates[j].winTheyDid();
						winners[w] = candidates[j];
//	System.out.println(winners[w].getName()+" is in the "+w+" spot on round "+r);
						w = seats;
					}
					
					else if((candidates[j].getSupport() > winners[w].getSupport())&&(candidates[j].didTheyWin()==false)) {	//if spots are occupied, compare support. 
						candidates[j].winTheyDid();
						candidates[winners[w].getIndex()].winTheyDidnt();
						
//	System.out.println(candidates[j].getName()+ " bumped "+winners[w].getName()+ " for the "+w+" spot.");
		
						winners[w] = candidates[j];				//if better, replace, and start over with all candidates (j = -1, ++ -> j = 0)
						w = seats;
						j= -1;
					}	
				}	
			}
		}
		
		for(int i = 0; i < seats; i++) {
			if(winners[i] != null) {
System.out.println(winners[i].getName()+ " won, after much conflict, and sits in "+i);
			}
		}
	}	
	
	public Ballot[] deprecateBallots(Ballot[] b, int r) {
		//c is a list of all the voters elected by round r, but it has length (seats), so has many null entries 
		//every ballot that voted for a candidate who was elected on or before round r needs to be setElected = true
		//ONLY deprecate ballots in the first entry in winners[0] is non-empty:
		
		if(winners[0]!= null) {
		
			Voter[] v = new Voter[b[0].getChosen().length]; //let's make a list for keeping track of the first r votes cast by each voter
				
			for(int i = 0; i < b.length ; i++) {	//don't worry about deprecating ballots which  && b[i].getElected() == false
				if(b[i].getElected() == false) {
			
					for(int j = 0; j < winners.length && j < b[i].getChosen().length; j++) {		//are already deprecated && j < 
				
						if(winners[j]!= null && b[i].getChosen()[j].getName() != null) {
					
							v[j] = b[i].getChosen()[j];  //now we have a list of UP TO the first r votes cast by the i-th voter(ballot)

						}
					}								//next, check to see if any preferred candidates have been elected
		
					for(int k = 0; k < winners.length && winners[k] != null; k++) {	//c is all elected candidates
						for(int l = 0; l < r+1 &&b[i].getElected() == false && v[l] != null; l++) {
						
							if(v[l].getName() == winners[k].getName()) {		
							
								b[i].setElected(true);
								b[i].setDropped(r);;
System.out.println(b[i].getVoter().getName()+"'s ballot was deprecated for having voted for "+v[l].getName()+" who won in round "+winners[k].getElectedRound()+", in round "+r);
							}			
						}
					}
				}
			}
		}
		
		return b;
		
	}
	
	
//	public Ballot[] deprecateBallots(Ballot[] b, int r) {
		//w is a list of all the voters elected by round r (thi.winners), but it has length (seats), so has many null entries 
		//every ballot that voted for a candidate who was elected on or before round r needs to be setElected = true
		//ONLY deprecate ballots if the first entry in winners[0] is non-empty:
		
//		Ballot[] minib = new Ballot[r];		//we're going to put the first r votes anyone has cast into minib, and compare them to all the winners so far
		
//		int officials = 0;
		
//			for(int i = 0; i < winners.length; i++) {
//				if(winners[i] != null) {
//					officials ++;
//				}
//			}
			
			
			
			
		
		
//		return b;
		
//	}
	
	
	
}
