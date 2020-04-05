package tasks;

import model.*;

public class Misc_Testing {

	public static void main(String[] args) {
		cell_test();
		System.out.println();
		connection_test();
	}
	
	private static void cell_test(){
		Spiking_Pyramidal_Node p_cell = new Spiking_Pyramidal_Node();
		Spiking_Thalamic_Node t_cell = new Spiking_Thalamic_Node();
		Spiking_Striatal_Node s_cell = new Spiking_Striatal_Node();
		Spiking_GP_Node gp_cell = new Spiking_GP_Node();
		
		for(int i = 0; i < 200; i++){
			//Input is reset after every step so it must be set again each time
			p_cell.set_input_to(1000.0);
			t_cell.set_input_to(1000.0);
			s_cell.set_input_to(1000.0);
			gp_cell.set_input_to(1000.0);
			
			p_cell.step();
			t_cell.step();
			s_cell.step();
			gp_cell.step();
		}
		
		System.out.println("Number of spikes by a pyramidal neuron after 200ms of 1000mA excitatory input: " + p_cell.DEBUG_get_spike_times().size());
		System.out.println("Number of spikes by a thalamic neuron after 200ms of 1000mA excitatory input: " + t_cell.DEBUG_get_spike_times().size());
		System.out.println("Number of spikes by a striatal neuron after 200ms of 1000mA excitatory input: " + s_cell.DEBUG_get_spike_times().size());
		System.out.println("Number of spikes by a globus pallidus neuron after 200ms of 1000mA excitatory input: " + gp_cell.DEBUG_get_spike_times().size());
		return;
	}
	
	private static void connection_test(){
		
		Spiking_Pyramidal_Node p_cell_1 = new Spiking_Pyramidal_Node();
		for(int i = 0; i < 200; i++){
			p_cell_1.step();
		}
		System.out.println("Number of spikes by a pyramidal neuron after 200ms of no input: " + p_cell_1.DEBUG_get_spike_times().size());
		
		p_cell_1.reset_node();
		Spiking_Pyramidal_Node p_cell_2 = new Spiking_Pyramidal_Node();
		Synaptic_Connection s_conn = new Synaptic_Connection(p_cell_2, p_cell_1, 50.0); //Create a connection from cell 2 to cell 1
		for(int i = 0; i < 200; i++){
			p_cell_2.set_input_to(1000.0);
			p_cell_1.step();
			p_cell_2.step();
			s_conn.send();
		}
		System.out.println("Number of spikes by a pyramidal neuron after 200ms of excitatory input from an active neuron: " + p_cell_1.DEBUG_get_spike_times().size());
		
		p_cell_1.reset_node();
		p_cell_2.reset_node();
		Spiking_Pyramidal_Node p_cell_3 = new Spiking_Pyramidal_Node();
		Presynaptic_Connection p_conn = new Presynaptic_Connection(p_cell_3, s_conn, -0.8); //Create a presynaptic connection from cell 3 to the previous connection
		for(int i = 0; i < 200; i++){
			p_cell_2.set_input_to(1000.0);
			p_cell_3.set_input_to(1000.0);
			p_cell_1.step();
			p_cell_2.step();
			p_cell_3.step();
			p_conn.send();
			s_conn.send();
		}
		System.out.println("Number of spikes by a pyramidal neuron after 200ms of excitatory input from an active neuron presynaptically inhibited by another active cell: " + p_cell_1.DEBUG_get_spike_times().size());
		
		return;
	}
}
