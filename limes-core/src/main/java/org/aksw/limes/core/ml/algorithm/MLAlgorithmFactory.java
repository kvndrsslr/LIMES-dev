package org.aksw.limes.core.ml.algorithm;

import org.aksw.limes.core.exceptions.UnsupportedMLImplementationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLAlgorithmFactory {

    
    public static final String EAGLE = "eagle";
    public static final String WOMBAT_SIMPLE = "wombat simple";
    public static final String WOMBAT_COMPLETE = "wombat complete";
    public static final String LION = "lion";
//    public static final String DECISION_TREE_LEARNING = "dtl";

    public static final String SUPERVISED_ACTIVE = "supervised active";
    public static final String SUPERVISED_BATCH = "supervised batch";
    public static final String UNSUPERVISED = "unsupervised";
    public static final String DEFAULT_ML_ALGORITHM = WOMBAT_SIMPLE;

    public static final Logger logger = LoggerFactory.getLogger(MLAlgorithmFactory.class);

    /**
     * @param name algorithm name
     * @return the core ML algorithm
     */
    public static Class<? extends ACoreMLAlgorithm> getAlgorithmType(String name) {
        if (name.equalsIgnoreCase(EAGLE)) {
            return Eagle.class;
        }
        if (name.equalsIgnoreCase(WOMBAT_SIMPLE)) {
            return WombatSimple.class;
        }
        if (name.equalsIgnoreCase(WOMBAT_COMPLETE)) {
            //@todo: fix this
            return null;
        }
        if (name.equalsIgnoreCase(LION)) {
            //@todo: fix this
            return null;
        }
//        if(name.equalsIgnoreCase(DECISION_TREE_LEARNING)){
//            return DecisionTreeLearning.class;
//        }
        logger.warn(name + " is not implemented yet. Using '" + DEFAULT_ML_ALGORITHM + "'...");
        return WombatSimple.class;
    }

    /**
     * @param name implementation type as string
     * @return the implementation type as enum
     */
    public static MLImplementationType getImplementationType(String name) {
        if (name.equalsIgnoreCase(SUPERVISED_ACTIVE)) {
            return MLImplementationType.SUPERVISED_ACTIVE;
        }
        if (name.equalsIgnoreCase(SUPERVISED_BATCH)) {
            return MLImplementationType.SUPERVISED_BATCH;
        }
        if (name.equalsIgnoreCase(UNSUPERVISED)) {
            return MLImplementationType.UNSUPERVISED;
        }
        logger.warn(name + " is not implemented yet. Using '" + UNSUPERVISED+ "' as default...");
        return MLImplementationType.UNSUPERVISED;
    }

    /**
     * @param clazz the core ML algorithm class
     * @param mlType the implementation type
     * @return the ML algorithm
     * @throws UnsupportedMLImplementationException Exception
     */
    public static AMLAlgorithm createMLAlgorithm(Class<? extends ACoreMLAlgorithm> clazz, MLImplementationType mlType) throws UnsupportedMLImplementationException {

        switch (mlType) {
            case SUPERVISED_BATCH:
                return new SupervisedMLAlgorithm(clazz);
            case UNSUPERVISED:
                return new UnsupervisedMLAlgorithm(clazz);
            case SUPERVISED_ACTIVE:
                return new ActiveMLAlgorithm(clazz);
            default:
                throw new UnsupportedMLImplementationException(clazz.getName());
        }

    }

}
