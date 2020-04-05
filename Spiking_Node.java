package model;

import java.util.*;

/**
 * An extension of Node that implements the framework for representing a spiking neuron using the Izhekivitch simple model.
 * Many specific parameter values are determined by the specific kind of neuron being modeled and so this class is left as an abstract.
 * Those parameter and the function that uses them are included in this abstract class as comments to foster consistent naming within subclasses.
 * Several other parameters are stored in size 1 arrays so that changes made to a given instance affect all the instances of the same connection.
 * The _gaba_alpha_curve and _glut_alpha_curve arrays may be confusing; see the calc_output method for more information on their function and purpose.
 * @author Scaed
 *
 */
public abstract class Spiking_Node extends Node{
	protected final Double[] _membrane_potential = new Double[1];
	protected final Double[] _u = new Double[1];
	protected final LinkedList<Integer> _spike_times = new LinkedList<Integer>();
	protected final int[] _t = new int[1];
	protected final Double[] _glut_output = new Double[1];
	protected final Double[] _gaba_output = new Double[1];
	
	protected static final double _min_alpha_value = 0.0000000001;
	//protected static final double _mp_spike_threshold;
	//protected static final double _u_spike_threshold_factor;
	//protected static final double _mp_rest;
	protected static final double _u_rest = 0.0;
	//protected static final double _threshold;
	//protected static final double _reset;
	//protected static final double _u_spike_add;
	//protected static final double _k;
	//protected static final double _C;
	//protected static final double _a;
	//protected static final double _b;
	//protected static final double _noise_stdv;
	protected static final int _max_act_durr = 747;
	
	protected static Random _rng = new Random();
	
	protected static final Double _lambda_glut = 60.0;
	protected static final Double _lambda_gaba = 30.0;
	public static Double[] _gaba_alpha_curve; 
	public static Double[] _glut_alpha_curve;

	public Spiking_Node(){
		if (_gaba_alpha_curve == null || _gaba_alpha_curve == null){
			calc_alpha_curve();
		}
		
		_u[0] = _u_rest;
		_spike_times.clear();
		_t[0] = 0;
		_glut_output[0] = 0.0;
		_gaba_output[0] = 0.0;
	}
	
	/**
	 * step() must be defined in each subclass. This is a dummy function to foster consistent naming within the interfaces of the subclasses.
	 * Although the values of the various parameters can vary, their use generally stays the same, as defined in Izhekivitch's work.
	 * Thus, for most neuron types this method can be copied into the subclasses where the values of the parameters can be accessed.
	 */
	/*public void step(){
		double _err = _rng.nextGaussian() * _noise_stdv;
		double u_change = _a * (_b * (_membrane_potential[0] - _mp_rest) - _u[0]); //diff 1
		double mp_change = ((_k * (_membrane_potential[0] - _mp_rest) * (_membrane_potential[0] - _threshold)) - _u[0] + _input[0] + _err) / _C;
		
		_input[0] = 0.0;
		_glut_output[0] = 0.0;
		_gaba_output[0] = 0.0;
		_t[0]++;
		_membrane_potential[0] += mp_change;
		_u[0] += u_change;
				
		if(_membrane_potential[0] >= (_mp_spike_threshold + (_u_spike_threshold_factor * _u[0]))){
			_spike_times.addFirst(_t[0]);
			_membrane_potential[0] = _reset - (_u_spike_threshold_factor * _u[0]);
			_u[0] += _u_spike_add;
		}

		calc_output();
	}*/
	
	/**
	 * Often neural networks are run many times. This method resets the node between trials.
	 */
	public void reset_node(){
		_input[0] = 0.0;
		_glut_output[0] = 0.0;
		_gaba_output[0] = 0.0;
		_u[0] = _u_rest;
		_spike_times.clear();
		_t[0] = 0;
	}
	
	public Double glut_output(){
		return _glut_output[0];
	}
	
	public Double gaba_output(){
		return _gaba_output[0];
	}
	
	public Double membrane_potential(){
		return _membrane_potential[0];
	}
	
	/**
	 * When a neuron spikes neurotransmitter is released. There are two types of neurotransmitter modeled: glutamate (excitatory) and GABA (inhibitory).
	 * The concentration of neurotransmitter released by a spike decays depending on its type independent of remnant concentrations from previous spikes.
	 * The alpha curve models the decay of neurotransmitter from a single spike. The concentration of neurotransmitter at a given point of time can be found by summing the points of the curve corresponding to the times since recent spikes.
	 */
	protected void calc_output(){
		boolean glut_done = false;
		boolean gaba_done = false;
		for(int s : _spike_times){
			int alpha_curve_idx = (_t[0] - s) + 1;
			if(!glut_done){
				if(alpha_curve_idx >= _glut_alpha_curve.length){
					glut_done = true;
				}
				else if((_glut_alpha_curve[alpha_curve_idx] < _min_alpha_value) && (alpha_curve_idx > _lambda_glut)){
					glut_done = true;
				}
				else{
					_glut_output[0] += _glut_alpha_curve[alpha_curve_idx];
				}
			}
			if(!gaba_done){	
				if(alpha_curve_idx >= _gaba_alpha_curve.length){
					gaba_done = true;
				}
				else if((_gaba_alpha_curve[alpha_curve_idx] < _min_alpha_value) && (alpha_curve_idx > _lambda_gaba)){
					gaba_done = true;
				}
				else{
					_gaba_output[0] += _gaba_alpha_curve[alpha_curve_idx];
				}
			}
			if(glut_done && gaba_done){
				break;
			}
		}
	}
	
	/**
	 * The alpha curves are calculated once at the start of an experiment and shared between all instances rather that having to be repeatedly calculated throughout the runs.
	 */
	protected static void calc_alpha_curve(){
		_glut_alpha_curve = null;
		_gaba_alpha_curve = null;
		_glut_alpha_curve = new Double[_max_act_durr * (int)Math.ceil(_lambda_glut)];
		_gaba_alpha_curve = new Double[_max_act_durr * (int)Math.ceil(_lambda_gaba)];
		for(int i = 0; i < _glut_alpha_curve.length; i++){
			_glut_alpha_curve[i] = ((double)i / _lambda_glut) * Math.exp(1 - (double)i / _lambda_glut);
		}
		for(int i = 0; i < _gaba_alpha_curve.length; i++){
			_gaba_alpha_curve[i] = ((double)i / _lambda_gaba) * Math.exp(1 - (double)i / _lambda_gaba);
		}
	}
	
	/**
	 * This method is prefixed with DEBUG as it should not need to be used in the collection of data
	 * @param d
	 */
	public void DEBUG_set_glut_output(double d){
		_glut_output[0] = d;
		return;
	}
	
	/**
	 * This method is prefixed with DEBUG as it should not need to be used in the collection of data
	 * @param d
	 */
	public void DEBUG_set_gaba_output(double d){
		_gaba_output[0] = d;
		return;
	}
	
	/**
	 * @return true if the node underwent a spike during the most recent time step.
	 */
	public boolean did_spike(){
		if(_spike_times.size() > 0 && _spike_times.getFirst() == _t[0]){
			return true;
		}
		else{
			return false;
		}
	}
	
	public LinkedList<Integer> DEBUG_get_spike_times(){
		return _spike_times;
	}
}
