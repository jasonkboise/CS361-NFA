package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fa.State;

/**
 * Implementation of NFAState class to be used
 * in project 2
 * @author Jason Kuphaldt, Connor Jackson
 *
 */

public class NFAState extends State {

    private Map<Character,Set<NFAState>> delta;//delta
	private boolean isFinal;//remembers its type
    
    /**
	 * Contructor takes a string name
     * and creates an NFAState that
     * is not a finalstate.
	 * @param name the state name as string
	 */
    public NFAState(String name) {
        initDefault(name);
        isFinal = false;
    }
 /**
	 * 
	 * @param name is string that is the name of state
	 * @param isFinal boolean that tracks if final
     * state or not
	 */
    public NFAState(String name, boolean isFinal) {
        initDefault(name);
        this.isFinal = isFinal;
    }

    /**
	 * 
	 * @param name as string
     * creates hashmap for transition.
	 * 
	 */
    private void initDefault(String name) {
        this.name = name;
        delta = new LinkedHashMap<Character,Set<NFAState>>();
    }

    /**
	 * Accessor for the state type
	 * @return true if final and false otherwise
	 */
    public boolean isFinal() {
        return isFinal;
    }

    /**
	 * Add the transition from current state
     * to the given state based on alphabet
     * symbol.
	 * @param onSymb the alphabet symbol
	 * @param toState to NFA state
	 */
	public void addTransition(char onSymb, NFAState toState){
        if (delta.containsKey(onSymb)) {
            delta.get(onSymb).add(toState);
        }
        else {
            Set<NFAState> set = new LinkedHashSet<NFAState>();
            set.add(toState);
            delta.put(onSymb, set);
        }
	}

    /**
	 * Returns the transition based on the
     * symb for the current state.
	 * @param symb char as alphabet symbol
	 * @return set of NFAStates
	 */
	public Set<NFAState> getTo(char symb){
		if(delta.containsKey(symb)){
			 return delta.get(symb);
		}
        else {
            return new LinkedHashSet<NFAState>();
        }
	}

}
