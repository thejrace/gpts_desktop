/* Gitaş - Obarey Inc 2018 */
package gpts.java.interfaces;
@FunctionalInterface
public interface Callback<P,R> {
    public R call(P param);
}
