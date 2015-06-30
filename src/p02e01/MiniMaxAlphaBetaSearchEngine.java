/* Trabajo Práctico N°2 - Atoms
 * Autores:
 *          Gustavo Martinez
 *          Adrian Tissera
 */
package p02e01;

import engines.AdversarySearchEngine;
import java.util.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import stateSpace.AdversarySearchProblem;
import stateSpace.AdversarySearchState;

/**
 * Clase que implementa el motor de búsqueda para Atoms.
 * @param <P>
 * @param <State>
 */
public class MiniMaxAlphaBetaSearchEngine<P extends AdversarySearchProblem<State>, State extends AdversarySearchState> extends AdversarySearchEngine<P, State> {

    private State nextMove;
    private int depth;

    /**
     * Constructor por defecto
     */
    public MiniMaxAlphaBetaSearchEngine() {
        super();
    }

    /**
     * Constructor que inicializa el motor de búsqueda con el problema p
     * @param p es el problema usado para inicializar
     */
    public MiniMaxAlphaBetaSearchEngine(P p) {
        super(p);
    }

    /**
     * 
     * Constructor que inicializa el motor de búsqueda con el problema p y 
     * profundidad máxima maxDepth
     * @param p es el problema usado para inicializar
     * @param maxDepth es la profundidad máxima de búsqueda
     */
    public MiniMaxAlphaBetaSearchEngine(P p, int maxDepth) {
        super(p, maxDepth);
    }

    /**
     * Computa un valor para un estado
     * @param state es el estado a evaluar
     * @return devuelve un 'int' con el valor de un estado 'state'
     */
    @Override
    public int computeValue(State state) {
        return miniMaxAlphaBeta(state, problem.minValue(), problem.maxValue(), maxDepth);
    }

    /**
     * Implementación recursiva del algoritmo minimax con poda alfa-beta
     * @param state es el estado a evaluar
     * @param alpha es el limite superior de una evaluación
     * @param beta es el limite inferior de una evaluación
     * @param depth es la prfundidad máxima de búsqueda
     * @return devuelve una valoracion para el estado 'state'
     */
    private int miniMaxAlphaBeta(State state, int alpha, int beta, int depth) {
        if (this.problem.end(state) || depth == 0) {
            return this.problem.value(state);
        } else {
            List<State> successors = this.problem.getSuccessors(state);
            for (State successor : successors) {
                if (alpha < beta) {
                    if (state.isMax()) {
                        alpha = max(alpha, miniMaxAlphaBeta(successor, alpha, beta, depth - 1));
                    } else {
                        beta = min(beta, miniMaxAlphaBeta(successor, alpha, beta, depth - 1));
                    }
                }
            }//endfor
            if (state.isMax()) {
                return alpha;
            } else {
                return beta;
            }//endif
        }//endif
    }

    /**
     * Devuelve el mejor estado sucesor de 'state' para min/max
     * @param state es el estado a evaluar
     * @return devuelve un estado sucesor de 'state'
     */
    @Override
    public State computeSuccessor(State state) {
        boolean isMax = state.isMax();
        List<State> successors = this.problem.getSuccessors(state);
        State result = successors.get(0);
        successors.remove(0);
        int resultValue = this.computeValue(result);
        int successorValue;
        for (State successor : successors) {
            successorValue = this.computeValue(successor);
            if (isMax) {
                if (resultValue < successorValue) {
                    result = successor;
                    resultValue = successorValue;
                }
            } else {
                if (resultValue > successorValue) {
                    result = successor;
                    resultValue = successorValue;
                }
            }
        }//endfor
        nextMove = result;
        return result;
    }

    @Override
    public void report() {
        System.out.println("El siguiente estado es: " + this.nextMove.toString());
    }
}
