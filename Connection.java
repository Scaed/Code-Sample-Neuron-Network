package model;

/**
 * Abstract class for connections from a Node and some receiver. Contains the core getter and setter functions.
 * Many parameters are stored in size 1 arrays so that changes made to a given instance affect all the instances of the same connection.
 * @author Scaed
 *
 */
public abstract class Connection {
	
	protected Node _source;
	protected Double[] _weight = new Double[1];
	protected Double[] _prev_weight_chg = new Double[1];
	protected Double[] _presynaptc_input = new Double[1];
	protected Double[] _prev_signal_sent = new Double[1];
	
	public void set_weight(Double wgt){
		_prev_weight_chg[0] = wgt - _weight[0];
		_weight[0] = wgt;
		return;
	}
	
	/**
	 * Increase the weight factor by d. To reduce the weight factor use a negative value for d.
	 * @param d
	 */
	public void raise_weight(Double d){
		_prev_weight_chg[0] = d;
		_weight[0] += d;
		return;
	}
	
	public Double get_weight(){
		return _weight[0];
	}
	
	/**
	 * Increase the presynaptic input to this connection by d. To reduce the presynaptic input use a negative value for d.
	 * See the documentation of Presynaptic_Connection.java for details about presynaptic input.
	 * @param d
	 */
	public void raise_presyn_input(double d){
		_presynaptc_input[0] += d;
		return;
	}
	
	/**
	 * Set the presynaptic input to this connection to p_input.
	 * See the documentation of Presynaptic_Connection.java for details about presynaptic input.
	 * @param p_input
	 */
	public void set_presyn_input(double p_input){
		_presynaptc_input[0] = p_input;
		return;
	}
	
	/**
	 * See the documentation of Presynaptic_Connection.java for details about presynaptic input.
	 * @return the current presynaptic input to this connection.
	 */
	public Double get_presyn_input(){
		return _presynaptc_input[0];
	}
	
	/**
	 * There is no set() method for the source as it should not be changed once assigned in the constructor method.
	 * @return
	 */
	public Node get_source(){
		return _source;
	}
	
	public Double get_prev_wgt_chg(){
		return _prev_weight_chg[0];
	}
	
	/**
	 * send() must be defined in each subclass. This is a dummy function to foster consistent naming within the interfaces of the subclasses.
	 * @return the input from _source
	 */
	public double send(){
		System.out.println("Alert! Your model is using a superclass function that does not have access to vital parameter values. You must define the function send() in the subclasses where the parameter vaules are accessable.");
		_prev_signal_sent[0] = _source.output();
		return _source.output();
	}
}
