package part1;
import java.util.Arrays;

/**
 * @author danma
 *
 */
public class NeuralNetwork {
    public final double[][] hidden_layer_weights;
    public final double[][] output_layer_weights;
    private final int num_inputs;
    private final int num_hidden;
    private final int num_outputs;
    private final double learning_rate;

    public NeuralNetwork(int num_inputs, int num_hidden, int num_outputs, double[][] initial_hidden_layer_weights, double[][] initial_output_layer_weights, double learning_rate) {
        //Initialise the network
        this.num_inputs = num_inputs;
        this.num_hidden = num_hidden;
        this.num_outputs = num_outputs;

        this.hidden_layer_weights = initial_hidden_layer_weights;
        this.output_layer_weights = initial_output_layer_weights;

        this.learning_rate = learning_rate;
    }

    /**
     * @param input nodes output value
     * @return	result of sigmoid activation function using input
     */
    public double sigmoid(double input) { 
        double ePowZ = Math.exp(-input);
        return 1/(1+ePowZ);
    }
    /**
     * Uses the current NN weights to find the outputs of the given inputs
     * @param inputs we want to find the output results for
     * @return	hidden layer outputs + output layer outputs
     */
    public double[][] forward_pass(double[] inputs) {
        double[] hidden_layer_outputs = new double[num_hidden];
        for (int i = 0; i < num_hidden; i++) {
            double weighted_sum = 0.0;
            for(int j = 0;j < this.num_inputs;j++) {
            	weighted_sum+=this.hidden_layer_weights[j][i]*inputs[j];
            }
            double output = sigmoid(weighted_sum);
            hidden_layer_outputs[i] = output;
        }
        double[] output_layer_outputs = new double[num_outputs];
        for (int i = 0; i < num_outputs; i++) {
            double weighted_sum = 0;
            for(int j = 0;j < this.num_hidden;j++) {
            	weighted_sum+=this.output_layer_weights[j][i]*hidden_layer_outputs[j];
            }
            double output = sigmoid(weighted_sum);
            output_layer_outputs[i] = output;
        }
        return new double[][]{hidden_layer_outputs, output_layer_outputs};
    }
    /**
     * calculates beneficial weight changes for each weight in the NN
     * 
     * @param inputs
     * @param hidden_layer_outputs
     * @param output_layer_outputs
     * @param desired_outputs
     * @return
     */
    public double[][][] backward_propagate_error(double[] inputs, double[] hidden_layer_outputs,
                                                 double[] output_layer_outputs, int desired_outputs) {
        double[] output_layer_betas = new double[num_outputs];
        for(int i = 0; i<num_outputs;i++) {
        	if(i==desired_outputs) {
        		output_layer_betas[i] = 1-output_layer_outputs[i];
        	}else {
        		output_layer_betas[i] = 0-output_layer_outputs[i];
        	}
        }
        double[] hidden_layer_betas = new double[num_hidden];
        for(int i = 0;i<num_hidden;i++) {
        	double beta = 0.0;
        	for(int j = 0;j<num_outputs;j++) {
        		beta += output_layer_weights[i][j]*output_layer_outputs[j]*(1-output_layer_outputs[j])*output_layer_betas[j];
        	}
        	hidden_layer_betas[i] = beta;
        }
        double[][] delta_output_layer_weights = new double[num_hidden][num_outputs];
        for(int i = 0;i<num_hidden;i++) {
        	for(int j = 0;j<num_outputs;j++) {
        		delta_output_layer_weights[i][j] = this.learning_rate*hidden_layer_outputs[i]*output_layer_outputs[j]*(1 - output_layer_outputs[j])*output_layer_betas[j];
        	}
        }
        double[][] delta_hidden_layer_weights = new double[num_inputs][num_hidden];
        for(int i = 0;i<num_inputs;i++) {
        	for(int j = 0;j<num_hidden;j++) {
        		delta_hidden_layer_weights[i][j] = this.learning_rate*inputs[i]*hidden_layer_outputs[j]*(1 - hidden_layer_outputs[j])*hidden_layer_betas[j];
        	}
        }
        return new double[][][]{delta_output_layer_weights, delta_hidden_layer_weights};
    }
    /**
     * Updates our NN weights using the weight changes from the BP method
     * @param delta_output_layer_weights
     * @param delta_hidden_layer_weights
     */
    public void update_weights(double[][] delta_output_layer_weights, double[][] delta_hidden_layer_weights) {
    	
    	for(int i = 0;i<num_inputs;i++) {
    		for(int j = 0;j<num_hidden;j++) {
    			this.hidden_layer_weights[i][j] += delta_hidden_layer_weights[i][j];
    		}
    	}
    	for(int i = 0;i<num_hidden;i++) {
    		for(int j = 0;j<num_outputs;j++) {
    			this.output_layer_weights[i][j] += delta_output_layer_weights[i][j];
    		}
    	}
    }
    /**
     * Online learning training method using feedforward pass and backpropogation
     * @param instances
     * @param desired_outputs
     * @param epochs
     */
    public void train(double[][] instances, int[] desired_outputs, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("epoch = " + epoch);
            int[] predictions = new int[instances.length];
            for (int i = 0; i < instances.length; i++) {
                double[] instance = instances[i];
                double[][] outputs = forward_pass(instance);
                double[][][] delta_weights = backward_propagate_error(instance, outputs[0], outputs[1], desired_outputs[i]);
                predictions[i] = findMaxClass(outputs);
                update_weights(delta_weights[0], delta_weights[1]);
            }
            System.out.println(calcAccuracy(desired_outputs,predictions));
        }
    }
    /**
     * returns a double representing the percentage of predictions that were correct
     * @param desired_outputs
     * @param predictions
     * @return
     */
    public double calcAccuracy(int[] desired_outputs, int[] predictions) {
    	double count = 0;            
        for(int i = 0;i<desired_outputs.length;i++) {
        	if(desired_outputs[i]==predictions[i]) {
        		count++;
        	}
        }
        return count/desired_outputs.length;
    }
    
    /**
     * @param list of output node values
     * @return index of output node with the highest value
     */
    public int findMaxClass(double[][] outputs) {
    	double max = Double.NEGATIVE_INFINITY;
        int predicted_class = -1;
        for(int j = 0;j<outputs[1].length;j++) {
        	if(outputs[1][j]>max) {
        		predicted_class = j;
        		max = outputs[1][j];
        	}
        }
        return predicted_class;
    }
    /**
     * Predicts the class label of given instances using the NN without learning
     * @param instances
     * @return	list of predictions
     */
    public int[] predict(double[][] instances) {
        int[] predictions = new int[instances.length];
        for (int i = 0; i < instances.length; i++) {
            double[] instance = instances[i];
            double[][] outputs = forward_pass(instance);
            predictions[i] = findMaxClass(outputs);
        }     
        return predictions;
    }

}
