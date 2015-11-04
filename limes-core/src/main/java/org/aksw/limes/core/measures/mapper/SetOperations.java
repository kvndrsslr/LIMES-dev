package org.aksw.limes.core.measures.mapper;

import org.aksw.limes.core.io.mapping.MemoryMapping;

public class SetOperations {

    public enum Operator {
	AND, OR, DIFF, XOR, MINUS
    };

    /**
     * Relies on operators to perform set operations on mappings
     * 
     * @param source
     *            Source mapping
     * @param target
     *            Target mapping
     * @param op
     *            Set pperator
     * @return Resulting mapping
     */
    public static MemoryMapping getMapping(MemoryMapping source, MemoryMapping target, Operator op) {
	if (op.equals(Operator.AND))
	    return intersection(source, target);
	if (op.equals(Operator.OR))
	    return union(source, target);
	if (op.equals(Operator.DIFF))
	    return difference(source, target);
	if (op.equals(Operator.XOR))
	    return union(difference(source, target), difference(target, source).reverseSourceTarget());
	return new MemoryMapping();
    }

    /**
     * Get runtime approximation for operator
     * 
     * @param op,
     *            operator
     * @param mappingSize1
     *            mapping size of source
     * @param mappingSize2
     *            mapping size of target
     * @return operator runtime as double
     */
    public static double getRuntimeApproximation(Operator op, int MemoryMappingSize1, int MemoryMappingSize2) {
	if (op.equals(Operator.AND)) {
	    return 1d;
	}
	if (op.equals(Operator.OR)) {
	    return 1d;
	}
	return 1d;
    }

    /**
     * Get mapping size approximation for operator
     * 
     * @param op,
     *            operator
     * @param mappingSize1
     *            mapping size of source
     * @param mappingSize2
     *            mapping size of target
     * @return operator mapping size as double
     */
    public static double getMappingSizeApproximation(Operator op, int mappingSize1, int mappingSize2) {
	if (op.equals(Operator.AND))
	    return Math.min(mappingSize1, mappingSize2);
	if (op.equals(Operator.OR))
	    return Math.max(mappingSize1, mappingSize2);
	if (op.equals(Operator.DIFF) || op.equals(Operator.XOR))
	    return Math.max(mappingSize1, mappingSize2) - Math.min(mappingSize1, mappingSize2);
	else
	    return 0d;
    }

    /**
     * Computes the difference of two mappings.
     *
     * @param map1
     *            First mapping
     * @param map2
     *            Second mapping
     * @return map1 \ map2
     */
    public static MemoryMapping difference(MemoryMapping map1, MemoryMapping map2) {
	MemoryMapping map = new MemoryMapping();

	// go through all the keys in map1
	for (String key : map1.map.keySet()) {
	    // if the first term (key) can also be found in map2
	    if (map2.map.containsKey(key)) {
		// then go through the second terms and checks whether they can
		// be found in map2 as well
		for (String value : map1.map.get(key).keySet()) {
		    // if yes, take the highest similarity
		    if (!map2.map.get(key).containsKey(value)) {
			map.add(key, value, map1.map.get(key).get(value));
		    }
		}
	    } else {
		map.add(key, map1.map.get(key));
	    }
	}
	return map;
    }

    /**
     * Computes the intersection of two mappings. In case an entry exists in
     * both mappings the minimal similarity is taken
     *
     * @param map1
     *            First mapping
     * @param map2
     *            Second mapping
     * @return Intersection of map1 and map2
     */
    public static MemoryMapping intersection(MemoryMapping map1, MemoryMapping map2) {
	MemoryMapping map = new MemoryMapping();
	// takes care of not running the filter if some set is empty
	if (map1.size() == 0 || map2.size() == 0) {
	    return new MemoryMapping();
	}
	// go through all the keys in map1
	for (String key : map1.map.keySet()) {
	    // if the first term (key) can also be found in map2
	    if (map2.map.containsKey(key)) {
		// then go through the second terms and checks whether they can
		// be found in map2 as well
		for (String value : map1.map.get(key).keySet()) {
		    // if yes, take the highest similarity
		    if (map2.map.get(key).containsKey(value)) {
			if (map1.map.get(key).get(value) <= map2.map.get(key).get(value)) {
			    map.add(key, value, map1.map.get(key).get(value));
			} else {
			    map.add(key, value, map2.map.get(key).get(value));
			}
		    }
		}
	    }
	}
	return map;
    }

    /**
     * Computes the union of two mappings. In case an entry exists in both
     * mappings the maximal similarity is taken
     *
     * @param map1
     *            First mapping
     * @param map2
     *            Second mapping
     * @return Union of map1 and map2
     */
    public static MemoryMapping union(MemoryMapping map1, MemoryMapping map2) {
	MemoryMapping map = new MemoryMapping();
	// go through all the keys in map1
	for (String key : map1.map.keySet()) {
	    // if the first term (key) can also be found in map2
	    for (String value : map1.map.get(key).keySet()) {
		map.add(key, value, map1.getSimilarity(key, value));
	    }
	}
	for (String key : map2.map.keySet()) {
	    // if the first term (key) can also be found in map2
	    for (String value : map2.map.get(key).keySet()) {
		map.add(key, value, map2.getSimilarity(key, value));
	    }
	}
	
	return map;
    }

    /**
     * Implements the exclusive or operator
     * 
     * @param map1
     *            First map
     * @param map2
     *            Second map
     * @return XOR(map1, map2)
     */
    public static MemoryMapping xor(MemoryMapping map1, MemoryMapping map2) {
	return union(difference(map1, map2), difference(map2, map1).reverseSourceTarget());
    }

    
}
