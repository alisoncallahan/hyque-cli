package org.semanticscience.hyque;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.hyque.lib.HyQueSPINRuleExecutor;

import com.hp.hpl.jena.rdf.model.Model;

public class HyQueSPINRuleExecutorTest {

	private static File inputFile = null;
	private static File spinRules = null;
	private static File outputDir = null;
	private static String syntax = null;
	private static String spinSyntax = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		inputFile = new File("/home/alison/Downloads/Hypothesis1_Sunitinib2.n3");
		//inputFile = new File("/home/alison/Desktop/misc/aging_hypotheses/aging_hypothesis_0667.n3");
		spinRules = new File("/home/alison/workspace_test/Test/hyque_cardiotoxicity_merged_updated_7_3_14.ttl");
		//spinRules = new File("/home/alison/indigo_tbc_workspace/HyQue/hyque-wormbase-rules.spin.rdf");
		outputDir = new File("/home/alison/tmp/");
		syntax = "N3";
		spinSyntax = "TTL";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		inputFile = null;
		outputDir = null;
		syntax = null;
	}

	@Test
	public void test() {
		HyQueSPINRuleExecutor executor = new HyQueSPINRuleExecutor();
		Model evaluation = executor.executeSPINRulesOnFile(spinRules, inputFile, syntax, spinSyntax);
		System.out.println(evaluation);
	}
}
