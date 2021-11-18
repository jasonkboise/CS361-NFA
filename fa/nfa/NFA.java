package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
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
        checkIfExists(fromState).addTransition(onSymb,checkIfExists(toState));
        if (!ordAbc.contains(onSymb) && onSymb != 'e') {
            ordAbc.add(onSymb);
        }
        
    }

    @Override
    public Set<NFAState> getStates() {
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
        return start;
    }

    @Override
    public Set<Character> getABC() {
        return ordAbc;
    }

    private Set<NFAState> getToState(Set<NFAState> from, char onSymb) {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();

		for (NFAState nfaState : from) {
			ret.addAll(nfaState.getTo(onSymb));
		}

		return ret;
	}

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

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> visited = new LinkedHashSet<NFAState>();
        visited = eClosure(s, visited);
        return visited;
    }

    public Set<NFAState> eClosure(Set<NFAState> s) {
        Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for (NFAState nfaState : s) {
			ret.addAll(eClosure(nfaState));
		}

		return ret;
    }

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
