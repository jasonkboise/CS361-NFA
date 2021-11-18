# Project 2: NFA Creator

* Author: Jason Kuphalt, Connor Jackson
* Class: CS361
* Semester: Fall 2021

## Overview

This Java program receives an alphabet, a set of states (start, and final states as well) and transitions. It then creates a non deterministic finite automata
based on the recieved values from test documents.

## Compiling and Using

First compile the program: javac fa/nfa/NFADriver.java

To run this program in onyx go to the folder that the project is located in and type: java fa.nfa.NFADriver ./tests/"yourtesthere"

example: java fa.nfa.NFADriver ./tests/p2tc1.txt

## Discussion

We went ran with the philosophy that every NFA is also a DFA. This meant we reused 
as much of the code provided for us in the DFA.java, DFAState.java classes and added
the needed changes to these for our NFA.java and NFAState.java classes. 

The most difficult things for use were the eClosure() and getDFA() methods. The rest was 
very straightforward as we learned very similar things from project 1.


## Sources used

Code was reused from the provided DFA.java, and DFAState.Java classes that were provided. Credit 
for those goes to Elena Sherman.
