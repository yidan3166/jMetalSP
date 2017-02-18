package org.uma.jmetalsp.spark.util;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Antonio J. Nebro on 26/06/14.
 */
public class SparkSolutionListEvaluator<S extends Solution<?>> implements SolutionListEvaluator<S>, Serializable {
  static JavaSparkContext sparkContext ;

  public SparkSolutionListEvaluator(JavaSparkContext sparkContext) {
    this.sparkContext = sparkContext ;
  }

  @Override public List<S> evaluate(List<S> solutionList, final Problem<S> problem)
      throws JMetalException {

    JavaRDD<S> solutionsToEvaluate = sparkContext.parallelize(solutionList);

    solutionsToEvaluate.foreach(solution -> problem.evaluate(solution));
    /*
    JavaRDD<S> evaluatedSolutions =
        solutionsToEvaluate.map(solution -> {
          S sol = (S)solution.copy() ;
          problem.evaluate(sol);
          return sol;
        });

    List<S> newPopulation = evaluatedSolutions.collect();

    List<S> resultSolutionSet = new ArrayList<>(solutionSet.size()) ;
    for (Solution solution : newPopulation) {
      resultSolutionSet.add((S)solution) ;
    }
  */
    return solutionsToEvaluate.collect();
  }

  @Override
  public void shutdown() {
  }
}
