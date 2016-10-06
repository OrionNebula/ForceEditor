package forceEditor.maps;

import java.util.ArrayList;

public class ModulusList<E> extends ArrayList<E>
{
	private static final long serialVersionUID = -2383711398743752768L;

	public E get(int index)
	{
		if(size() != 0)
			return super.get((index < 0) ? (this.size() - (-index % this.size()) ) % this.size() : (index % this.size()));
		else
			throw new IndexOutOfBoundsException();
	}
}
