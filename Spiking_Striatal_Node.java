package model;

/**
 * An implementation of Spiking_Node with parameters that replicate the behavior of a striatal neuron.
 * Striatal neurons have no exceptionally irregular characteristics.
 * @author Scaed
 *
 */
public class Spiking_Striatal_Node extends Spiking_Node {
	
	protected static final double _mp_spike_threshold = 40.0;
	protected static final double _u_spike_threshold_factor = 0.0;
	protected static final double _mp_rest = -80.0;
	protected static final double _threshold = -25.0;
	protected static final double _reset = -55.0;
	protected static final double _u_spike_add = 150.0;
	protected static final double _k = 1.0;
	protected static final double _C = 50.0;
	protected static final double _a = 0.01;
	protected static final double _b = -20.0;
	protected static final double _noise_stdv = 0.0;

	public Spiking_Striatal_Node(){
		super();
		_membrane_potential[0] = _mp_rest;
	}
		
	/**
	 * This is the standard step function from the Spiking_Node parent class.
	 */
	public void step(){
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
	}
	
	public void reset_node(){
		super.reset_node();
		_membrane_potential[0] = _mp_rest;
	}
}
