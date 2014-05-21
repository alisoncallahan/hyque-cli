package org.semanticscience.hyque.main;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HyQueSPINEvaluatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		String[] args = new String[] { "-inputFormat", "N3",
				"-inputHypothesis",
				"/home/alison/indigo_tbc_workspace/HyQue/aging_hypothesis0903.n3", "-outputDir",
				"/home/alison/temp/",
				"-spinRulesFile", "/home/alison/indigo_tbc_workspace/HyQue/hyque-wormbase-rules.spin.rdf" };
		HyQueSPINEvaluator.main(args);
	}

}
