package org.bigDataScript.cluster.host;

import org.bigDataScript.cluster.Cluster;

/**
 * Local host information
 *
 * @author pcingola@mcgill.ca
 */
public class HostSsh extends Host {

	HostHealth health;

	public HostSsh(Cluster cluster, String hostName) {
		super(cluster, hostName);

		// Set basic parameters
		resources.setCpus(1);
		health = new HostHealth(this);	
		health.setAlive(true);
	}

	public HostHealth getHealth() {
		return health;
	}

	@Override
	public boolean isAlive() {
		return getHealth().isAlive();
	}

}