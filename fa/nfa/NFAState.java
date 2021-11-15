package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fa.State;

public class NFAState extends State {

    private Map<Character,Set<NFAState>> delta;//delta
	private boolean isFinal;//remembers its type
    
    /**
	 * Default constructor
	 * @param name the state name
	 */
    public NFAState(String name) {
        initDefault(name);
        isFinal = false;
    }

    public NFAState(String name, boolean isFinal) {
        initDefault(name);
        this.isFinal = isFinal;
    }

    /**
	 * Overlaoded constructor that sets the state type
	 * @param name the state name
	 * @param isFinal the type of state: true - final, false - nonfinal.
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
	 * Add the transition from <code> this </code> object
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
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the set of new states 
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
