public class Main {

	public static void main(String[] args) {
		
		
		//Generate Map
			//max xy dimensions, fill with 1s for habitable areas
		
		int xMax = 30;
		int yMax = 30;
		int pop = 10000;
		int seats = 5;
		
		int greed = seats * 3;	//number of voters to promote to candidates		
		double threshhold = pop / seats;	 //pop/seats
		
		
		LandMap landMap = new LandMap();
			landMap.setX(xMax);
			landMap.setY(yMax);
			landMap.setInhabited();
			landMap.onesMap();				//filled with ones
			
		//Generate PopMap (empty)
			
		PopMap popMap = new PopMap();
				popMap.setX(xMax);
				popMap.setY(yMax);
				popMap.setZ(pop);
				popMap.setInhabited();
			
		//Generate PopAttractors
				
		PopAttractor[] attractors = makeAttractors(1);		
				landMap.addAttractors(attractors);	
				
				//printLandMap(landMap);
				
				
		//Generate Parties
		
		PartyMap partyMap= new PartyMap();	
		partyMap.setParties(3);
		Party[] allParties = new Party[3];
		
		Party a = new Party();
			a.setName("Hufflepuff");
			a.setStrength(55);
			partyMap.addParty(a);
			allParties[0] = a;
		Party b = new Party();
			b.setName("Ravenclaw");
			b.setStrength(40);
			partyMap.addParty(b);
			allParties[1] = b;
		Party c = new Party();
			c.setName("Mooseknuckle");
			c.setStrength(5);
			partyMap.addParty(c);
			allParties[2] = c;
	
		setParties(attractors, allParties);	
		partyMap.setXY(xMax, yMax);
		partyMap.setBaseline();
			
		//update partyMap
		Party[] LAParties = new Party[3];
		LAParties[0] = a;
		LAParties[1]=b;
		LAParties[2]=c;
		LAParties[0].setStrength(65);
		LAParties[1].setStrength(20);
		LAParties[2].setStrength(15);
		
		attractors[0].setLean(LAParties);
		
		partyMap.useAttractors(attractors);
		

//Generate Voters
				
		Voter[] voterList = new Voter[pop];		//make a list of all voters we have in the state
			for(int i = 0; i < pop; i++) {
				Voter v = new Voter();
				v.setName("wimp" + i);
				v.placeVoter(landMap); 		//figure out, based on PopAttractors, where voters will live
				v.placeParty(partyMap);		//based on PopAttractors, figure out which party voters are in	
				v.locLoyalty(75);				//Randomize how much location loyalty a voter has			
				v.parLoyalty(75);				//Randomize how much party loyalty a voter has
				v.setTenacity(seats);
				v.setReceivedLength(seats);
				voterList[i] = v;
			//	System.out.println(voterList[i].getName() + " lives at " + voterList[i].getX() + " and " + voterList[i].getY() + ", and votes " + voterList[i].getParty().getName());
			}
		
			
		//Promote Voters to Candidates
		
			for(int i = 0; i < greed; i++) {
				voterList[i].makeCandidate();	//make the first handful of voters become candidates
				voterList[i].setName("chump"+ i);
				//System.out.println(voterList[i].getName());
			}
			
		//Add voters to popMap	
			for(int i = 0; i < pop; i++) {
			placeVoter(voterList[i], popMap); //once a voter is fully made, copy voter to PopMap 
			}
		//Generate Ballots
			
			Ballot[] ballots = makeBallots(voterList, greed);
			
		//Fill Ballots
		
			ballots = fillBallots(ballots, seats);
		//	shareBallots(ballots, 5);
		//Count Ballots	

			TallyTwo tally = new TallyTwo();
			tally.setThreshhold(threshhold);
			tally.countBallots(ballots, seats);
			
	}
	
	public static void shareBallots(Ballot[] b, int n) {	//print the first n selections, in order, that a voter has made
		for(int i = 0; i < b.length; i++) {
			b[i].shareBallot(n);
		}
		
	}
	
	public static void addPartiesToAttractor(PopAttractor pa, Party[] pl) {
		for(int i = 0; i < pl.length; i++) {	//for every party being fed in...
		pa.addParty(pl[i]);						//add that party, with its strength, to the partyLean of the popattractor
		}
		
	}
	
	public static Ballot[] fillBallots(Ballot[] b, int votes) {
		
		//let b[i].getVoter vote for the 'votes' number of b[i].getCandidates of their choosing!
		
		for(int i = 0; i < b.length; i++) {			
			
			Voter v = b[i].getVoter();							//and this is the voter we're trying to please
			int votX = v.getX();								//They live at X
			int votY = v.getY();								//and Y
			int ll = v.getloc();  								//they have this much location loyalty			
			Party votP = v.getParty();							//they belong to this party
			int pl = v.getpar();								//and have this much party loyalty
			int choices = 1;									//how many votes have they cast
			
			
			b[i].setChosen(v.getTenacity());					//tell the ballot how many blanks to choices to have
//	System.out.println(v.getTenacity());
			
		//	for(int z = 0; z < b.length; z++) {				//make sure there are no null values in the 'chosen' array on the ballot
		//		b[i].voteFor(z,  -1);
		//	}
			
			while(b[i].isTenacious()) {  //as long as a voter hasn't used up all their patience with voting..
				
				//randomly select a candidate, and see if they meet the criteria
				
				int cindex = (int) (Math.floor(Math.random() *(b[i].getCandidates().length)));	//this is the index of the candidate
				
				Voter c = b[i].getCandidates()[cindex];												//I now have a random Candidate
				
				
				if(b[i].wasChosen(c) == false) {
				
					//if b[i].getCandidates()[cindex] is already on b[i].getChosen(),
				
	//			System.out.println(v.getName()+" is considering "+c.getName());
				
					int canX = c.getX();
					int canY = c.getY();
					Party canP = c.getParty();
				
					int dist = Math.max(Math.abs(canX - votX), Math.abs(canY - votY));
				
					boolean islocal = false;
					boolean isparty = false;
					
					int proll = (int) (Math.floor(Math.random() * 99));
						if(canP.getName() == votP.getName() && proll < pl) {
							isparty = true;
						}
						else if(canP.getName() != votP.getName() && proll > pl) {
							isparty = true;
						}
				
						int lroll = (int) (Math.floor(Math.random() * 99));
				
						if(lroll < Math.max(ll, 100 - dist)) {
								islocal = true;
						}

				//if they're acceptable, mark them as voted for in the votes - getTenacity round, and decrement tenacity
				
						if(islocal == true && isparty == true) {
							b[i].voteFor(cindex, choices); //which candidate, and in which round
					
		//			System.out.println(v.getName()+" chose "+c.getName()+" in round "+ choices);
							b[i].reduceTenacity();
							choices++;
						}	
				}
			}		
		}
		
		return b;
		
	}
	
	public static PopAttractor[] makeAttractors(int choice) {
		
		int cities = 1;
		PopAttractor[] attractors = new PopAttractor[cities];
		
		if(choice == 1) {
			
			for(int i = 0; i < cities; i++) {
				PopAttractor LA = new PopAttractor();
				
				LA.setName("losangeles");
				LA.setX(20); 					
				LA.setY(20);					
				LA.setStrength(30);				
				attractors[i] = LA;

			}	
		}
		
		return attractors;
		
	}

	public static void placeVoter(Voter v, PopMap p) {
		
		Voter[][][] P = p.getVoters();
		
		int x = v.getX();
		int y = v.getY();
		
				for(int k = 0; k < p.getZ(); k++) {
					
					if(P[x][y][k] == null) {
						p.addVoter(v, x, y, k);
						k = p.getZ();
						
					}	
		}
	}
	
	public static Ballot[] makeBallots(Voter[] v, int n) {
		
		Voter[] candidates = new Voter[n];
		
		
		for(int k = 0; k < n; k++) {
			for(int j = 0; j < v.length; j++) {
				if(v[j].isCandidate() == true) {
					candidates[k] =  v[j];
					k++;
				}
			}
		}	
		
		Ballot[] ballots = new Ballot[v.length];
		
			for(int i = 0; i < v.length; i++) {
				Ballot b = new Ballot();
				b.setCandidates(candidates);
				b.setVoter(v[i]);
				b.setElected(false);
				ballots[i] = b;
			}
		
		return ballots;
		
	}

	public static void printLandMap(LandMap l) {
		for(int i = 0; i < l.getY(); i++) {
			String s = new String();
			for(int j = 0; j < l.getX(); j++){
				s = s  + l.getXY(i,j)+ " ";
			}
			System.out.println(s);
		}
	}
	
	public static void setParties(PopAttractor[] pa, Party[] p) {
		int pl = p.length; //number of parties
		int pal = pa.length; //number of pop attractors
		
		for (int i = 0; i < pal; i++) { //for every pop attractors
			pa[i].makePartyNum(pl);		//make PartyLean[] be the same size as number of parties
		}
		
	}
}
