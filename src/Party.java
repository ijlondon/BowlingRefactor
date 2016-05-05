/*
 * Party.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Party.java,v $
 *   Revision 1.3  2003/02/09 21:21:31  ???
 *   Added lots of comments
 *
 *   Revision 1.2  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 * Container that holds bowlers
 */

import java.util.Comparator;
import java.util.Vector;

public class Party implements Comparable<Party>{

    /** Vector of bowlers in this party */
    private Vector myBowlers;

    /* A "ticket stub" signifying order in line */
    private int posInLine = 0;
    
    /* Has been assigned a lane */
    private boolean isAssigned = false;
    
    /**
     * Constructor for a Party
     *
     * @param bowlers    Vector of bowlers that are in this party
     */

    public Party(Vector bowlers) {
        myBowlers = new Vector(bowlers);
    }

    /**
     * Accessor for members in this party
     *
     * @return A vector of the bowlers in this party
     */

    public Vector getMembers() {
        return myBowlers;
    }
	
	/*
	 * Meant to assign a position in line for this party.
	 */
	public void setPos(int pos) {
		posInLine = pos;
	}
	
	
	public int getPos() {
		return posInLine;
	}

	@Override
	public int compareTo(Party arg0) {
		if(((Party)arg0).posInLine > (this).posInLine){
			return -1;
		}
		if(((Party)arg0).posInLine < (this).posInLine){
			return 1;
		}
		
		return 0;
	}
	
	public boolean getAssigned() {
		return isAssigned;
	}
	
	public void setAssigned(boolean assignment) {
		isAssigned = assignment;
	}
	
}
