package com.meum.classifier.bias;

import com.meum.classifier.TreeNode;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;

public abstract class BiasModifier implements EvolutionObserver<TreeNode> {
    protected PopulationData<? extends TreeNode> populationData;

    public void populationUpdate(PopulationData<? extends TreeNode> populationData) {
        this.populationData = populationData;
    }
}
