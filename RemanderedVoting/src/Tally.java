
public class Tally {
	
	public Voter[] candidates;	//all candidates
	public Voter[] winners;
	public int[][] votes;
	public double thresh;
	public Voter[] backups;
	
	public void setThreshhold(double d) {
		this.thresh = d;	
	}
	
	public void countBallots(Ballot[] b, int seats) {		
		
		//this method takes in the stack of ballots, and is told how many seats there are to fill.
		//It figures out how many
		
		
		int chumps = b[0].getCandidates().length;
		this.candidates = new Voter[chumps];		//populate the list of possible candidates
		this.votes = new int[chumps][chumps];		//and make a home to tally their votes for each round
		
		
		winners = new Voter[seats];	//we will only fill this array with candidates who have won
		backups = new Voter[this.candidates.length];//we will put candidates who did not win outright here, in order of support
		
		for(int i = 0; i < this.candidates.length; i++) {	//with the same candidates (voters) that 
			this.candidates[i] = b[0].getCandidates()[i];	// have been fed in
		}
		
		for(int l = 0; l < chumps; l++) {				//then, set the dropped round to chumps - we're intending to count them
			b[l].setDropped(chumps);					//all, at first
		}
		

		
			for(int r = 0; r < seats && winners[seats-1] == null ; r++) {			//r is the voting round, minus one
				for(int v = 0; v < b.length; v++) {		//for every ballot (voter) ... (should be 20)//as long as this guy is still voting(?)
				
					if(b[v].getElected() == false) {					//only look at un-deprecated ballots
						String zv = b[v].getChosen()[r].getName();		//this is the candidate we're going to increment ('Look for Bobby ...')
					//	System.out.println(b[v].getVoter().getName()+" voted for "+b[v].getChosen()[r].getName()+" on round "+ r);
		
						for(int x = 0; x <this.candidates.length; x++) {
						
							//String zc = this.candidates[x].getName();					//if the voted round for the candidate equals r...
						
							if(zv == this.candidates[x].getName()) {
							
								votes[x][r] +=1;												//'...and give him a vote in this round'
								candidates[x].setReceived(r, candidates[x].getReceived(r)+1);
							}
						}
					}
				}
				//BEFORE GOING TO THE NEXT ROUND, we need to check the victory condition, and exclude ballots if it's met
				for(int ii = 0; ii < votes.length; ii++) {
					for(int jj = 0; jj < votes[0].length; jj++ ) {
						System.out.print(votes[ii][jj]+ " ");
					}
					System.out.println();
				
				}
	
				checkVictory(votes, b, thresh, r, seats);			
				b = deprecateBallots(b, winners, r);
				
				boolean inc = true;		//inc should be false as long as any voters remain who have not gotten a candidate elected
				int qq = 0;
				
				while(inc == true && qq < b.length) {
					inc = b[qq].getElected();
					qq++;
					
				}
				
				if(winners[seats-1] == null && inc == true) { 		//everyone has been deprecated, but not all seats are filled.
					
					for(int pp = 0; pp < b.length; pp++) {
						System.out.println("MMAAJJOORR RREESSEETTT");
						b[pp].setElected(false);			//let everyone count from the rth round onwards, and get a second bite at the apple
					}
				}
				
				
			}
			
			for(int y = 0; y <b.length; y++) {
				if(b[y].getElected() == true) {
			//		System.out.println(b[y].getVoter().getName()+" has been deprecated.");
				}
				else{
			//		System.out.println(b[y].getVoter().getName()+" is still voting!");
					
				}
			}
			
													
		

		
		for(int q = 0; q <winners.length ; q++) { //reports final results as winner name and the round they were elected in
			if(winners[q] != null) {
				System.out.println(winners[q].getName()+" "+winners[q].getElectedRound());
			}
			else {		//if there is a blank in winners by the time we're printing it, not all seats have been filled
						//We need to go back, and see who of the remaining candidates got the most support
				System.out.println(q+ "is blank!");
				
					for(int z = 0; z < candidates.length; z++) {
						System.out.println("Candidate "+candidates[z].getName()+" has "+candidates[z].getSupport()+" support.");
							if(candidates[z].getElectedRound() != -1) {
								System.out.println("...but they were elected in round "+candidates[z].getElectedRound());
								candidates[z].setSupport(0);
								System.out.println("so their support is now "+candidates[z].getSupport());
							}	
					}
				
					//choose from the remaining candidates, based on support, to fill the remaining spot(s)
					
					int spots = winners.length - q;
					int electees = winners.length-spots;
				//	int pp = 0;
					
					while(electees != spots && electees>0) {
						
						System.out.println("There are "+electees+" electees for "+spots+" seats.");
						long max = candidates[0].getSupport();
						long drop = candidates[0].getSupport();
						
						for(int i = 0; i < electees; i++) {		//get the lowest weight (needs fixing)
							
							if(candidates[i].getSupport()< drop) {
								drop = candidates[i].getSupport();
							}
							if(candidates[i].getSupport() > max) {
								max = candidates[i].getSupport();
							}
							
						}
						
						for(int j = 0; j <candidates.length && electees != spots; j++) {		//remove the electee with the lowest weight
							if(candidates[j].getSupport() == drop && candidates[j] != null) {
								System.out.println("This round, there are "+ electees+" electees left for "+spots+" seats.");
								System.out.println(candidates[j].getName()+" has been dropped!");
								candidates[j].setSupport(0); 
								electees--;

							}
						}
						
					}
					
					
					
					
			}
				
		}
	}
	
	public void checkVictory(int[][] v, Ballot[] b, double t, int r, int seats) {		//if a candidate from v is over the threshhold t, after
		
		System.out.println("Checking Victory after round " + r);
		
		int nl = 0;
		for(int ll = 0; ll < winners.length && winners[ll] != null; ll++) {
			nl++;
		}
		int os = seats - nl;
		System.out.println("There are "+os+" open seats going into round "+r);
		
		Voter[] open = new Voter[candidates.length]; //open seats, for sitting candidates who pass this round
		
		int[]  tallies = new int[v.length];								// round r, then amend candidates
		for(int i = 0; i < v.length; i++) {									//for every candidate
			for(int j = 0; j < r+1; j++) {
			tallies[i] += v[i][j];
			}
		}
		
		int el = 0;

		for(int k = 0; k < v.length; k++) { // k is the candidate
			
			if(tallies[k] > (t-1) && b[0].getCandidates()[k].getElectedRound() == -1) {  //EXCLUDE ELECTED CANDIDATES
				Voter zz = b[0].getCandidates()[k];		//get the winning candidate;
				zz.setElectedRound(r);							//tell them that they won
				open[el] = zz;							//put them in the winners' annex
				System.out.println(open[el].getName()+" made the mark!");
				el++;
			}
		}
		

		
		long[] weight = new long[el];
		for(int i = 0; i < el; i++) { //for a candidate who was elected
			weight[i] = 0;

			for(int j = 0; j < r+1; j++) {	//add up their support
				weight[i] += (open[i].getReceived(j) * Math.pow(b.length, ((seats -j))));
			}
			
			open[i].setSupport(weight[i]);
			System.out.println(open[i].getName()+ " got in with "+open[i].getSupport()+" support.");

		}
		
		weight = new long[candidates.length]; //resize!
		for(int i = 0; i < candidates.length; i++) { //for everyone
			
			weight[i] = 0;

			for(int j = 0; j < r+1; j++) {	//add up their support
				weight[i] += (candidates[i].getReceived(j) * Math.pow(b.length, ((seats -j))));
			}
			
			candidates[i].setSupport(weight[i]);
			System.out.println(candidates[i].getName()+ " has "+candidates[i].getSupport()+" support.");
		}
		
		//if there are more winners than open seats, only pick the candidates with the most 1st round votes,
		//then 2nd round votes, &c. (STOP AFTER 2 rounds, or use biginteger?
		
		if(el > os) {	

			while(el != os && el>0) {
				
				System.out.println("There are "+el+" electees for "+os+" seats.");
				long max = weight[0];
				long drop = weight[0];
				
				for(int i = 0; i < el; i++) {		//get the lowest weight (needs fixing)
					
					if(weight[i]< drop) {
						drop = weight[i];
					}
					if(weight[i] > max) {
						max = weight[i];
					}					
				}
				
				for(int j = 0; j <weight.length && el != os; j++) {		//remove the electee with the lowest weight
					if(weight[j] == drop && open[j] != null) {
						System.out.println("This round, there are "+ el+" electees left for "+os+" seats.");
						System.out.println(open[j].getName()+" has been dropped!");
						open[j] = null;
						weight[j] = max;
						el--;
					}
				}			
			}
		}
			
		int qq = 0;
		int s = 0;
		System.out.println(" There are "+el+" candidates for "+os+" seats, after culling.");
		
		while(s < winners.length && qq < open.length && el!= 0) {
			if(winners[s] == null) {	//and put them on the list of winning candidates in the next available spot			
				if(open[qq] != null) { //...somehow, null open entries are making it into winners
					winners[s] = open[qq];
					System.out.println(winners[s].getName()+" is the "+ (s+1) +"th official winner!");
					qq++;
					s++;
					el--;
	//				System.out.println("There are"+os+"seats, and");
				}
				else {
					System.out.println("The "+qq+"th element of open is empty, so we can't put it in the "+s+"th place in winners.");
					qq++;	
				}
			}
			else {
				s++;
			}	
		}	
	}
	
	
	public Ballot[] deprecateBallots(Ballot[] b, Voter[] c, int r) {
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

							}			
						}
					}
				}
			}
		}
		
		return b;
		
	}
}
