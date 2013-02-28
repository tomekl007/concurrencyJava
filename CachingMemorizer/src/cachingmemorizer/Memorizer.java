/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cachingmemorizer;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Memorizer <A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memorizer(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        while (true) {
            System.out.println("compute arg " + arg);
            Future<V> f = cache.get(arg);
            System.out.println("f is  " + f);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = cache.putIfAbsent(arg, ft); //Return previous value associated with specified key, or null if there was no mapping for key.
                System.out.println("after putIfAbsent " + f);

                //jesli przez czas tworzenia callable,
                //pozadana wartosc zostala utworzona przez inny 
                //watek, sprawdzam jeszcze raz czy zeczywscie
                //null, jesli tak to run, jesli nie to w f
                //mam juz obliczone czyli nie wywoluje run
                if (f == null) {
                    f = ft;
                    ft.run();//wywoluje metode call z callable
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}