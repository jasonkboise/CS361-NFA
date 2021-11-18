package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;

/**
 * This is the class that contains the methods
 * for setting up a Non Deterministic Finite
 * Automata. The methods include adding states 
 * (start, final, and neither), creating the 
 * alphabet, getDFA, and adding transitions.
 * 
 * @author Jason Kuphaldt, Connor Jackson
 *
 */

public class NFA implements NFAInterface{
    private Set<NFAState> states;
    
    private NFAState start;
    private Set<Character> ordAbc;

    public NFA() {
        states = new LinkedHashSet<NFAState>();
        ordAbc = new LinkedHashSet<Character>();
    }

    
    /** 
     * @param name is a string that start is
     * set to. It is also added to the set of
     * states. 
     */
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

    
    /** 
     * @param name is the string that 
     * is set as a state which means it
     * is added to the set of states for
     * the NFA.
     */
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
    
    /** 
     * @param s Just as addState above.
     */
    private void addState(NFAState s){
		states.add(s);
	}

    
    /** 
     * @param name is a string added to 
     * the set of finalstate and also 
     * to the set of states. 
     */
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

    
    /** 
     * @param fromState is a string of the beginning state.
     * @param onSymb is the character that represents the
     * transition symbol.
     * @param toState is the sstring that represents the
     * state that will be transitioned to based on the onsymb
     * 
     * This method recieves 3 values and adds them as transition 
     * data to the fromState. 
     */
    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        checkIfExists(fromState).addTransition(onSymb,checkIfExists(toState));
        if (!ordAbc.contains(onSymb) && onSymb != 'e') {
            ordAbc.add(onSymb);
        }
        
    }

    
    /** 
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> getStates() {
        return states;
    }

    
    /** 
     * @return Set<NFAState>
     */
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

    
    /** 
     * @return start State
     */
    @Override
    public State getStartState() {
        return start;
    }

    
    /** 
     * @return Set<Character>
     */
    @Override
    public Set<Character> getABC() {
        return ordAbc;
    }

    
    /** 
     * @param from the beginning state
     * @param onSymb the transition symbol
     * @return the state that will be 
     * transitioned to based on the 
     * transition data.
     */
    private Set<NFAState> getToState(Set<NFAState> from, char onSymb) {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();

		for (NFAState nfaState : from) {
			ret.addAll(nfaState.getTo(onSymb));
		}

		return ret;
	}

    
    /** 
     * @return DFA
     * Takes the NFA and turns it into a DFA.
     * Creates new empty NFA set, puts
     * states into the set. Iterates through
     * the set and turns it into a dfa. The dfa
     * is returned.
     * -ALL NFAs are Also DFAs-
     */
    @Override
    public DFA getDFA() {
        DFA dfa = new DFA();

		Map<Set<NFAState>, String> visited = new LinkedHashMap<Set<NFAState>, String>();

        Set<NFAState> states = eClosure(this.start);

        visited.put(states, states.toString());

        Queue<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();
        queue.add(states);
        
        dfa.addStartState(visited.get(states));

        while (queue.peek() != null) {
            states = queue.poll();

            for (char c: ordAbc) {
                Set<NFAState> temp = getToState(states, c);
                temp = eClosure(temp);
                
                if (!visited.containsKey(temp)) {
                    if (containsFinalState(temp)) {
                        dfa.addFinalState(temp.toString());
                    }
                    else {
                        dfa.addState(temp.toString());
                    }

                    visited.put(temp, temp.toString());
                    queue.add(temp);

                }
                dfa.addTransition(states.toString(), c, temp.toString());
            }
        }
		return dfa;
    }

    
    /** 
     * @param states
     * @return boolean
     * Iterates through set of NFA states
     * and returns true for every state
     * that is also a final state
     */
    private boolean containsFinalState(Set<NFAState> states) {
        boolean ret = false;
        for(NFAState state: states){
            if(state.isFinal()){
                ret = true;
                break;
            }
        }
        return ret;
    }

    
    /** 
     * @param from The start state as a NFAState object
     * @param onSymb The transition symbol as a char
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    
    /** 
     * @param s is a NFAState object. 
     * Creates new hashset. adds s to hashet.
     * returns visted.
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> visited = new LinkedHashSet<NFAState>();
        visited = eClosure(s, visited);
        return visited;
    }

    
    /** 
     * @param s Is a set of NFAState objects
     * Runs eclosure on every NFAState in the set.
     * @return Set<NFAState>
     */
    public Set<NFAState> eClosure(Set<NFAState> s) {
        Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for (NFAState nfaState : s) {
			ret.addAll(eClosure(nfaState));
		}

		return ret;
    }

    
    /** 
     * @param s NFA State object
     * @param visited Set of NFAStates
     * @return Set<NFAState>
     */
    private Set<NFAState> eClosure(NFAState s, Set<NFAState> visited) {
        if (!visited.contains(s)) {
            visited.add(s);

            Set<NFAState> states = getToState(s, 'e');
            for (NFAState state: states) {
                eClosure(state, visited);
            }
        }
        return visited;
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
