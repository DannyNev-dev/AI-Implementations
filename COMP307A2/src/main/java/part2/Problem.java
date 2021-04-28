package part2;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;


public class Problem extends GPProblem{
	
	private GPConfiguration config;
	private Variable variable;
	
	public Problem( GPConfiguration config, Variable variable) throws InvalidConfigurationException {
		super(config);
		this.config = config;
		this.variable = variable;
	}
	
	@Override
	public GPGenotype create() throws InvalidConfigurationException {
		CommandGene[][] nodeSets = { { 
				variable,	//x 
				new Terminal(this.config, CommandGene.DoubleClass, 2.0d, 10.0d, true), //const
				new Pow(this.config, CommandGene.DoubleClass),	//functions
				new Multiply(this.config, CommandGene.DoubleClass), 
				new Add(this.config, CommandGene.DoubleClass),
				new Divide(this.config, CommandGene.DoubleClass), 
				new Subtract(this.config, CommandGene.DoubleClass)} };
		return GPGenotype.randomInitialGenotype(config, new Class[] {CommandGene.DoubleClass}, new Class[][] {{},}, nodeSets, 20, true);
	}

}
