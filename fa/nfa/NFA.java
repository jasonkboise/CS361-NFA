package fa.nfa;

import java.util.HashMap;
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
        
        NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if(from == null){
			System.err.println("ERROR: No NFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null){
			System.err.println("ERROR: No NFA state exists with name " + toState);
			System.exit(2);
		}
		from.addTransition(onSymb, to);
		
		if(!ordAbc.contains(onSymb)){
			ordAbc.add(onSymb);
		}
        
    }

    @Override
    public Set<? extends State> getStates() {
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

    @Override
    public DFA getDFA() {
        DFA dfa = new DFA();
		Map<Set<NFAState>, String> visited = new LinkedHashMap<Set<NFAState>, String>();

        Set<NFAState> states = eClosure(start);

        visited.put(states, states.toString());

        LinkedList<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();

        queue.add(states);

        dfa.addStartState(visited.get(states));

        while (!queue.isEmpty()) {
            states = queue.poll();

            for (char c: ordAbc) {
                LinkedHashSet<NFAState> temp = new LinkedHashSet<>();
                for (NFAState st : states) {
                /* Adds all of the elements from 'st.getTo(c)' to temp */
                    temp.addAll(st.getTo(c));
                }
                LinkedHashSet<NFAState> temp1 = new LinkedHashSet<>();
                for(NFAState st : temp){
                    temp1.addAll(eClosure(st));
                }
                if(!visited.containsKey(temp1)){
                    visited.put(temp1, temp1.toString());
                    queue.add(temp1);

                    if(containsFinalState(temp1)){
                        dfa.addFinalState(visited.get(temp1));
                    }else{
                        dfa.addState(visited.get(temp1));
                    }
                }
                /* Add transitions to the DFA */
                dfa.addTransition(visited.get(states), c, visited.get(temp1));
            }
        }
		return dfa;
    }

    private boolean containsFinalState(LinkedHashSet<NFAState> temp1) {
        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> visited = new LinkedHashSet<NFAState>();
        return eClosure(s, visited);
    }

    private Set<NFAState> eClosure(NFAState s, Set<NFAState> visited) {
        //Set<NFAState> visitedStates = visited;
        Set<NFAState> list = new LinkedHashSet<NFAState>();
        if(!s.getTo('e').isEmpty() && !visited.contains(s)) {
            visited.add(s);
            for (NFAState curr: s.getTo('e')) {
                list.addAll(eClosure(curr, visited));
            }
        }
        return list;
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
