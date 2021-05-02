package part2;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * @author danma
 *
 */
public class FitnessFunction extends GPFitnessFunction {

	private static final long serialVersionUID = -6232688679641087425L;
	private static final double MIN_ERROR = 0.01;
	private Object[] empty = new Object[0];
	public A2Part2 mainClass;
	
	FitnessFunction(A2Part2 mainClass) {
		this.mainClass = mainClass;
	}
	
	/**
	 * Custom evaluate method that used the x data from main to determine the error and
	 * return a double repressenting the current formulas fitness (Lower is better)
	 */
	@Override
	protected double evaluate(IGPProgram a_subject) {
		double errTotal = 0.0;
		for(int i = 0;i<A2Part2.INPUT_SIZE;i++) {
			this.mainClass.variable.set(this.mainClass.x[i]);
			double result = a_subject.execute_double(0, this.empty);
			errTotal += Math.abs(result - this.mainClass.y[i]);
		}
		return errTotal<FitnessFunction.MIN_ERROR ? 0 : errTotal;
	}

}
