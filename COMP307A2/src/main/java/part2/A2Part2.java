/**
 * 
 */
package part2;

import java.io.InputStreamReader;
import java.util.Scanner;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Variable;

/**
 * @author danma
 *
 */
@SuppressWarnings("unused")
public class A2Part2 {
	
	public static final int INPUT_SIZE = 20;
	private static final int MAX_EVOLUTIONS = 100;
	private static final int maxIterations = 10;
	
	private GPConfiguration config;
	private Problem problem;
	public Variable variable;
	public double[]x = new double[INPUT_SIZE];
	public double[]y = new double[INPUT_SIZE];
	
	public A2Part2(String fileName) {
		Scanner scan = new Scanner(A2Part2.class.getResourceAsStream(fileName));
		scan.nextLine();
		scan.nextLine();
		for (int i = 0; scan.hasNextDouble(); i++) {
			this.x[i] = scan.nextDouble();
			this.y[i] = scan.nextDouble();
		}
	}
	/**
	 * A method that sets up a GPConfiguration
	 * @return A GPConfiguration object that is configured
	 * @throws InvalidConfigurationException
	 */
	public GPConfiguration setupConfig() throws InvalidConfigurationException {
		GPConfiguration newConfig = new GPConfiguration();
			newConfig.setCrossoverProb(0.9f);
			newConfig.setMutationProb(0.05f);
			newConfig.setReproductionProb(0.05f);
			newConfig.setPopulationSize(1000);
			newConfig.setFitnessFunction(new FitnessFunction(this));
			newConfig.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
			newConfig.setMaxCrossoverDepth(8);
			newConfig.setMaxInitDepth(4);
			newConfig.setStrictProgramCreation(true);
		return newConfig;
	}
	/**
	 * This method creates our problem and evolves MAX_EVOLUTION times 
	 * then outputs the best solution/formula
	 * @throws InvalidConfigurationException
	 */
	public void evolveFormula() throws InvalidConfigurationException {
		GPGenotype gp = this.problem.create();
		gp.setVerboseOutput(true);
		double bestSolution = 100.0;
		int count = 0;
		while(bestSolution!=0 && count<A2Part2.maxIterations) {
			count++;
			gp.evolve(A2Part2.MAX_EVOLUTIONS);
			bestSolution = gp.getAllTimeBest().getFitnessValue();
		}
		gp.outputSolution(gp.getAllTimeBest());
		
	}
	
	
	/**
	 * @param args
	 * @throws InvalidConfigurationException 
	 */
	public static void main(String[] args) throws Exception {
		A2Part2 main = new A2Part2("regression.txt");
		main.config = main.setupConfig();
		main.variable = Variable.create(main.config, "x", CommandGene.DoubleClass);
		main.problem = new Problem(main.config,main.variable);
		main.evolveFormula();
	}
	

}
