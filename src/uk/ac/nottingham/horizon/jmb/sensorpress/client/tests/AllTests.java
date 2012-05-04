package uk.ac.nottingham.horizon.jmb.sensorpress.client.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	SpXMLRpcClientTest.class,
	SpSelectionClientImplTest.class, 
	SpInsertionClientImplTest.class,
	//SpReplicationClientTest.class })
})
public class AllTests {

}
