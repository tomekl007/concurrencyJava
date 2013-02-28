/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlockinsinglethreadexecuor;

import deadlockinsinglethreadexecuor.ThreadDeadlock.RenderPageTask;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ThreadDeadlock td = new ThreadDeadlock();
        
        RenderPageTask rpt = td.new RenderPageTask();
        rpt.call();
        
    }
}
