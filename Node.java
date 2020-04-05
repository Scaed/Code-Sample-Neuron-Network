package model;

/**
 * The most basic form of a node in a neural network. It implements no spiking ability and merely outputs a signal that is the sum of it's input signals.
 * Parameters are stored in size 1 arrays so that changes made to a given instance affect all the instances of the same node.
 * @author Scaed
 *
 */
public class Node {
	
	protected final Double[] _output = new Double[1]; //output sent to other nodes
	protected final Double[] _input = new Double[1]; //input from other nodes
	
	public Node(){
		_input[0] = 0.0;
		_output[0] = 0.0;
	}

	/**
	 * Often neural networks are run many times. This method resets the node between runs.
	 */
	public void reset_node(){
		_input[0] = 0.0;
		_output[0] = 0.0;
	}
	
	public Double input(){
		return _input[0];
	}
	
	public void set_input_to(Double d){
		_input[0] = d;
		return;
	}
	
	/**
	 * Increase the input by d. To reduce the weight factor use a negative value for d.
	 * @param d
	 */
	public void raise_input_by(Double d){
		_input[0] += d;
		return;
	}
	
	public Double output(){
		return _output[0];
	}
	
	/**
	 * This method advances the node by a single time step.
	 */
	public void step(){
		_output[0] = _input[0];
		return;
	}
	
	public void set_output(Double d){
		_output[0] = d;
		return;
	}
}
