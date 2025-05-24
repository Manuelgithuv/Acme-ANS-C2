
package acme.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisabledAircrafts {

	private static final List<Integer> integers = Collections.synchronizedList(new ArrayList<>());


	public static void add(final Integer value) {
		DisabledAircrafts.integers.add(value);
	}

	public static List<Integer> getAll() {
		return new ArrayList<>(DisabledAircrafts.integers); // Retorna copia para evitar modificación directa
	}

	public static void clear() {
		DisabledAircrafts.integers.clear();
	}

}
