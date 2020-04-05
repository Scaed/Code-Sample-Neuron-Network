package model;

/**
 * An implementation of Spiking_Node with parameters that replicate the behavior of a thalamic neuron.
 * Thalamic neurons have an active and inactive mode and exhibit different behavior in each mode. 
 * Thus the _b parameter is replaced with _b_1 and _b_2 and the _b_threshold parameter has been added.
 * The step() method is also modified to capture these two modes.
 * @author Scaed
 *
 */
public class Spiking_Thalamic_Node extends Spiking_Node {
	
	protected static final double _mp_spike_threshold = 35.0;
	protected static final double _u_spike_threshold_factor = 0.1;
	protected static final double _mp_rest = -60.0;
	protected static final double _mp_rest_u = -65.0;
	protected static final double _threshold = -50.0;
	protected static final double _reset = -60.0;
	protected static final double _u_spike_add = 10.0;
	protected static final double _k = 1.6;
	protected static final double _C = 200.0;
	protected static final double _a = 0.01;
	protected static final double _b_1 = 15.0;
	protected static final double _b_2 = 0.0; 
	protected static final double _b_threshold = -65.0;
	protected static final double _noise_stdv = 0.0;

	public Spiking_Thalamic_Node(){
		super();
		_membrane_potential[0] = _mp_rest;
	}
		
	/**
	 * This method has been modified from the standard version presented in the parent class Spiking_Neuron. If the membrane potential is above _b_threshold,
	 * the neuron is in active mode and _b_2 is used. Otherwise _b_1 is used.
	 */
	public void step(){
		double _err = _rng.nextGaussian() * _noise_stdv;
		double u_change;
		if(_membrane_potential[0] <= _b_threshold){
			u_change = _a * (_b_1 * (_membrane_potential[0] - _mp_rest_u) - _u[0]);
		}
		else{
			u_change = _a * (_b_2 * (_membrane_potential[0] - _mp_rest_u) - _u[0]);
		}
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
	}
	
	public void reset_node(){
		super.reset_node();
		_membrane_potential[0] = _mp_rest;
	}
}
