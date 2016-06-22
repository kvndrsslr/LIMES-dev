package org.aksw.limes.core.io.query;

import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Enables one to register Jena Models as backends for the SparqlQueryModule
 *
 * @author Claus Stadler <cstadler@informatik.uni-leipzig.de>
 */
public class ModelRegistry {
    static Logger logger = LoggerFactory.getLogger("LIMES");

    private static ModelRegistry instance = null;
    private Map<String, Model> map = new HashMap<String, Model>();

    public static ModelRegistry getInstance() {
        if (instance == null) {
            instance = new ModelRegistry();
        }
        return instance;
    }

    public static void register(String name, Model model) {
        getInstance().getMap().put(name, model);
        logger.info("Registry = " + getInstance().map.keySet());
    }

    public static void unregister(String name) {
        getInstance().getMap().remove(name);
    }

    public Map<String, Model> getMap() {
        return map;
    }
}
