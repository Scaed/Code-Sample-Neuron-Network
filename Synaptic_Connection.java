package model;

import java.util.*;

/**
 * This represents a typical connection from one node to another. It can model both excitatory and inhibitory connections.
 * It correctly handles the case where the source is a Spiking_Node where excitatory and inhibitory neurotransmitters are modeled separately.
 * @author Scaed
 *
 */
public class Synaptic_Connection extends Connection{
	
	private Node _receiver;

	public Synaptic_Connection(Node src, Node rcv, Double wgt){
		_source = src;
		_receiver = rcv;
		_weight[0] = wgt;
		_prev_weight_chg[0] = 0.0;
		_presynaptc_input[0] = 0.0;
		_prev_signal_sent[0] = 0.0;
	}
	
	/**
	 * This method advances the connection by one time step. It selects the signal source based on the class of the source (Spiking_Node or not) and the sign of the weight parameter (excitatory or inhibitory).
	 * Presynaptic input is added to the signal from the source before being multiplied by the weight. Then the final signal is added to the input of the receiver.
	 */
	public double send(){
		double signal;
		if(_source instanceof Spiking_Node){
			if(_weight[0] < 0){
				signal = ((Spiking_Node)_source).gaba_output();
			}
			else{
				signal = ((Spiking_Node)_source).glut_output();
			}
		}
		else{
			signal = _source.output();
		}
		
		signal += _presynaptc_input[0];
		_presynaptc_input[0] = 0.0;
		if(signal < 0.0){
			signal = 0.0;
		}
		
		_receiver.raise_input_by(signal * _weight[0]);
		_prev_signal_sent[0] = signal * _weight[0];
		
		return signal;
	}
	
	/**
	 * There is no set() method for the receiver as it should not be changed once assigned in the constructor method.
	 * @return
	 */
	public Node get_reciever(){
		return _receiver;
	}

	/**
	 * This is a static function that probabilistically creates a set of Synaptic_Connections between a group of source nodes and a group of receiver nodes
	 * with the weight drawn from a Gaussian distribution of chosen mean and standard deviation. 
	 * @param sources the group of source nodes
	 * @param receivers the group of receiver nodes
	 * @param mean the mean weight of the connections created
	 * @param stdv the standard deviation of the connections created
	 * @param prob the probability that connection will be created between a given source/receiver pair  
	 * @return an array of new connections between sources and receivers
	 */
	public static Synaptic_Connection[] connect(Node[] sources, Node[] receivers, double mean, double stdv, double prob){
		ArrayList<Synaptic_Connection> cons = new ArrayList<Synaptic_Connection>();
		Random rng = new Random();
		for(Node s : sources){
			for(Node r : receivers){
				if(rng.nextDouble() < prob){
					double weight = (rng.nextGaussian() + mean) * stdv;
					cons.add(new Synaptic_Connection(s, r, weight));
				}
			}
		}
		Synaptic_Connection[] out = (Synaptic_Connection[]) cons.toArray();
		return out;
	}
}
