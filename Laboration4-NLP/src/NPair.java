import java.util.ArrayList;

public class NPair<E, V> {

	public E e;
	public V v;
	public NPair<E,V> next;
	
	public NPair(E e, V v) {
		this.e = e;
		this.v = v;
		this.next = null;
	}
	
	public NPair(E e, V v, NPair<E, V> next) {
		this.e = e;
		this.v = v;
		this.next = next;
	}
	
	public ArrayList<NPair<E, V>> asList() {
		ArrayList<NPair<E,V>> toReturn = new ArrayList<NPair<E,V>>();
		addToList(toReturn);
		return toReturn;
	}
	
	private void addToList(ArrayList<NPair<E,V>> list) {
		list.add(this);
		if(next != null) {
			next.addToList(list);
		}
	}
	
	
}
