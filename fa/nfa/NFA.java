package fa.nfa;

import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;

public class NFA implements NFAInterface{
    private Set<NFAState> states;
    
    private NFAState start;
    private Set<Character> ordAbc;

    public NFA() {
        states = new LinkedHashSet<NFAState>();
        ordAbc = new LinkedHashSet<Character>();
    }

    @Override
    public void addStartState(String name) {
        NFAState s = checkIfExists(name);
        if (s == null) {
            s = new NFAState(name);
            addState(s);
        }
        else {
            System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
        }
        start = s;
    }

    @Override
    public void addState(String name) {
        NFAState s = checkIfExists(name);
        if (s == null) {
            s = new NFAState(name);
            addState(s);
        }
        else {
            System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
        }
    }
    private void addState(NFAState s){
		states.add(s);
	}

    @Override
    public void addFinalState(String name) {
        NFAState s = checkIfExists(name);
        if (s == null) {
            s = new NFAState(name, true);
            addState(s);
        }
        else {
            System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
        }
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<? extends State> getStates() {
        // TODO Auto-generated method stub
        return states;
    }

    @Override
    public Set<NFAState> getFinalStates() {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for(NFAState s : states){
			if(s.isFinal()){
				ret.add(s);
			}
		}
		return ret;
	}

    @Override
    public State getStartState() {
        // TODO Auto-generated method stub
        return start;
    }

    @Override
    public Set<Character> getABC() {
        // TODO Auto-generated method stub
        return ordAbc;
    }

    @Override
    public DFA getDFA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        
        return null;
    }

    public void DFS() {

    }

    /**
	 * Check if a state with such name already exists
	 * @param name
	 * @return null if no state exist, or DFAState object otherwise.
	 */
	private NFAState checkIfExists(String name){
		NFAState ret = null;
		for(NFAState s : states){
			if(s.getName().equals(name)){
				ret = s;
				break;
			}
		}
		return ret;
	}
    
}
