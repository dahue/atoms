/* Trabajo Práctico N°2 - Atoms
 * Autores:
 *          Gustavo Martinez
 *          Adrian Tissera
 */
package p02e01;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import util.Pair;

/**
 * Clase que implementa la interfaz gráfica
 */
public class AtomsView {

    private static final int height = AtomsState.getHeight();
    private static final int width = AtomsState.getWidth();
    private final int levelEasy = 1;
    private final int levelHard = 10;

    private AtomsState state;
    private AtomsProblem problem;
    private MiniMaxAlphaBetaSearchEngine<AtomsProblem, AtomsState> engine;
    private Pair<Integer, Integer> rule = new Pair<>();

    private final JFrame frame      = new JFrame("Atoms");
    private JButton[][] buttons     = new JButton[height][width];
    private final Container grid    = new Container();
    private final JMenuBar menuBar  = new JMenuBar();
    private final JMenu mainMenu    = new JMenu("Menu"),
                        newGame     = new JMenu("New Game");
    private final JMenuItem reset   = new JMenuItem("Reset"),
                            exit    = new JMenuItem("Exit"),
                            easy    = new JMenuItem("Easy"),
                            hard    = new JMenuItem("Hard");
    private final ImageIcon[] atomsIcons;

    /**
     * Constructor por defecto, crea la ventana de juego
     */
    public AtomsView() {
        newGame.add(easy);
        newGame.add(hard);
        easy.addActionListener(newGameListener);
        hard.addActionListener(newGameListener);

        mainMenu.add(reset);
        mainMenu.add(newGame);
        mainMenu.add(exit);
        reset.addActionListener(resetListener);
        exit.addActionListener(exitListener);
        menuBar.add(mainMenu);
        
        frame.setSize(50*width+4,50*height+48);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(menuBar, BorderLayout.NORTH);

        grid.setLayout(new GridLayout(height, width));
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBackground(java.awt.Color.DARK_GRAY);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(gameButtonListener);
                grid.add(buttons[i][j]);
                
            }
        }
        grid.setVisible(false);
        frame.add(grid, BorderLayout.CENTER);
        
        atomsIcons = new ImageIcon[7];
        atomsIcons[0] = new ImageIcon();
        atomsIcons[1] = new ImageIcon(appAtoms.class.getResource("assets/images/1R.png"));
        atomsIcons[2] = new ImageIcon(appAtoms.class.getResource("assets/images/1B.png"));
        atomsIcons[3] = new ImageIcon(appAtoms.class.getResource("assets/images/2R.png"));
        atomsIcons[4] = new ImageIcon(appAtoms.class.getResource("assets/images/2B.png"));
        atomsIcons[5] = new ImageIcon(appAtoms.class.getResource("assets/images/3R.png"));
        atomsIcons[6] = new ImageIcon(appAtoms.class.getResource("assets/images/3B.png"));
    }

    /**
     * Cuando se pulsa el boton 'Exit', cierra la ventana del juego
     */
    ActionListener exitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    };

    /**
     * Cuando de pulsa New Game/(Easy || Hard) inicializa un nuevo juego con la
     * dificultad seleccionada
     */
    ActionListener newGameListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            grid.setVisible(true);
            frame.add(grid, BorderLayout.CENTER);
            int[][] a = new int[height][width];
            a[0][0] = -1;
            a[height - 1][width - 1] = 1;
            state = new AtomsState(a);
            problem = new AtomsProblem(state);
            refreshWindow(state);
            if (event.getSource().equals(hard)) {
                engine = new MiniMaxAlphaBetaSearchEngine(problem, levelHard);
            } else {
                engine = new MiniMaxAlphaBetaSearchEngine(problem, levelEasy);
            }

        }
    };

    /**
     * Al pulsar Reset reinicia el juego con la dificultad que habia sido 
     * seleccionada previamente
     */
    ActionListener resetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            int[][] a = new int[height][width];
            a[0][0] = -1;
            a[height - 1][width - 1] = 1;
            state = new AtomsState(a);
            problem = new AtomsProblem(state);
            refreshWindow(state);
        }
    };

    /**
     * Cuando se pulsa un casillero del juego, coloca un átomo para el jugador
     * azul y luego realiza la jugada el jugador rojo. Al finalizar actualiza
     * la grilla y queda esperando por una nueva juagada de azul.
     */
    ActionListener gameButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            boolean condition = false;
            boolean found = false;
            int x = 0, y = 0;
            state.setMax(false);
            for (int i = 0; i < buttons.length && !found; i++) {
                for (int j = 0; j < buttons[0].length && !found; j++) {
                    found = event.getSource().equals(buttons[i][j]);
                    if (found) {
                        condition = state.getBoard()[i][j] >= 0 && state.isMax();
                        condition = condition || state.getBoard()[i][j] <= 0 && !state.isMax();
                        x = i;
                        y = j;
                    }
                }
            }
            if (condition) {
                problem.addAtom(x, y, state);
                refreshWindow(state);
                if (problem.end(state)) {
                    JOptionPane.showMessageDialog(frame, "You win!!!", "Finished match", 1);
                    resetListener.actionPerformed(event);
                } else {
                    state.setMax(true);
                    rule = (Pair<Integer, Integer>) engine.computeSuccessor(state).ruleApplied();
                    problem.addAtom(rule.fst(), rule.snd(), state);
                    refreshWindow(state);
                    if (problem.end(state)) {
                        JOptionPane.showMessageDialog(frame, "You lose...", "Finished match", 1);
                        resetListener.actionPerformed(event);
                    }
                }
            }//endif
        }//endMethod
    };

    /**
     * Metodo que actualiza la ventana de juego
     * @param state es el nuevo estao al cual la ventana debe ser actualizada
     */
    private void refreshWindow(AtomsState state) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                int aux = state.getBoard()[i][j];
                if (aux == 0) {
                    buttons[i][j].setIcon(atomsIcons[0]);
                } else {
                    if (aux == 1) {
                        buttons[i][j].setIcon(atomsIcons[1]);
                    } else if (aux == -1) {
                        buttons[i][j].setIcon(atomsIcons[2]);
                    } else if (aux == 2) {
                        buttons[i][j].setIcon(atomsIcons[3]);
                    } else if (aux == -2) {
                        buttons[i][j].setIcon(atomsIcons[4]);
                    } else if (aux == 3) {
                        buttons[i][j].setIcon(atomsIcons[5]);
                    } else if (aux == -3) {
                        buttons[i][j].setIcon(atomsIcons[6]);
                    }
                }
            }
        }
    }
}
