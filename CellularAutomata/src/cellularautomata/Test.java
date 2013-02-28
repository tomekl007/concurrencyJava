/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularautomata;

import cellularautomata.CellularAutomata.Board;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CellularAutomata ca = new CellularAutomata(new Board() {

            @Override
            public int getMaxX() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getMaxY() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getValue(int x, int y) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int setNewValue(int x, int y, int value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void commitNewValues() {
               System.out.println("commitNEwValues");
            }

            @Override
            public boolean hasConverged() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void waitForConvergence() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Board getSubBoard(int numPartitions, int index) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}
