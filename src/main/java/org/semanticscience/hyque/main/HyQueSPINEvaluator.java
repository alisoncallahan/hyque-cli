package org.semanticscience.hyque.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.semanticscience.hyque.lib.HyQueSPINRuleExecutor;

import com.hp.hpl.jena.rdf.model.Model;

public class HyQueSPINEvaluator {

	public static void main(String[] args){
		
		Options options = createOptions();
		CommandLineParser parser = createCliParser();
		File input = null;
		File spinRules = null;
		File output = null;
		
		String inputSyntax = null;
		String spinFileSyntax = null;
			
		try {
			CommandLine cmd = parser.parse(options, args);
			if(cmd.hasOption("help")){
				printUsage();
				System.exit(1);
			}
			
			if(cmd.hasOption("inputHypothesis")){
				input = new File(cmd.getOptionValue("inputHypothesis"));
			} else {
				System.out.println("Specify an input file or directory");
				System.exit(1);
			}
			
			if(cmd.hasOption("spinRulesFile")){
				spinRules = new File(cmd.getOptionValue("spinRulesFile"));
			} else {
				System.out.println("Specify a SPIN rules file");
				System.exit(1);
			}
			
			if(cmd.hasOption("outputDir")){
				output = new File(cmd.getOptionValue("outputDir"));
			} else {
				System.out.println("Specify an output directory");
				System.exit(1);
			}
			
			if(cmd.hasOption("inputFormat")){
				inputSyntax = cmd.getOptionValue("inputFormat");
			} else {
				System.out.println("Specify an input RDF syntax");
				System.exit(1);
			}
			
			if(cmd.hasOption("spinFileFormat")){
				spinFileSyntax = cmd.getOptionValue("spinFileFormat");
			} else {
				System.out.println("Specify the SPIN file RDF syntax");
				System.exit(1);
			}
			
			if(input.isDirectory()){
				File[] filesList = input.listFiles();
				for (int i = 0; i < filesList.length; i++) {
					File f = filesList[i];
					String filename = f.getName();

					System.out.println("HyQue is evaluating "+filename+"... ");
					HyQueSPINRuleExecutor executor = new HyQueSPINRuleExecutor();
					Model evaluation = executor.executeSPINRulesOnFile(spinRules, f, inputSyntax, spinFileSyntax);
					String id = filename.substring(0,filename.indexOf("."));
					String outputName = id+"_evaluation.rdf";
					File outputFile = new File(output.getAbsolutePath()+"/"+outputName);
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(outputFile);
						evaluation.write(fos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							fos.close();
							evaluation.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					System.out.println("... done!");
				}//for
			} else {
				String filename = input.getName();

				System.out.println("HyQue is evaluating "+filename+"... ");
				HyQueSPINRuleExecutor executor = new HyQueSPINRuleExecutor();
				Model evaluation = executor.executeSPINRulesOnFile(spinRules, input, inputSyntax, spinFileSyntax);
				String id = filename.substring(0, filename.indexOf("."));
				String outputName = id+"_evaluation.rdf";
				File outputFile = new File(output.getAbsolutePath()+"/"+outputName);
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(outputFile);
					evaluation.write(fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						fos.close();
						evaluation.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("... done!");
			}
		} catch (ParseException e) {
			System.out.println("Unable to parse specified options.");
			printUsage();
			System.exit(1);
		}
	}
	
	@SuppressWarnings("static-access")
	public static Options createOptions(){
		Options o = new Options();
		Option help = new Option("help", false, "Print this message");
		Option inputFile = OptionBuilder.withArgName("inputFile")
				.hasArg()
				.withDescription("Input hypothesis file or directory")
				.isRequired()
				.create("inputHypothesis");
		Option spinRules = OptionBuilder.withArgName("spinRulesFile")
				.hasArg()
				.withDescription("SPIN rules file")
				.isRequired()
				.create("spinRulesFile");
		Option outputDirectory = OptionBuilder.withArgName("outputDir")
				.hasArg()
				.withDescription("Directory to store output hypothesis evaluations")
				.isRequired()
				.create("outputDir");
		Option inputFormat = OptionBuilder.withArgName("inputFormat")
				.hasArg()
				.withDescription("RDF format of input hypotheses")
				.isRequired()
				.create("inputFormat");
		Option spinFileFormat = OptionBuilder.withArgName("spinFileFormat")
				.hasArg()
				.withDescription("RDF format of input SPIN file")
				.isRequired()
				.create("spinFileFormat");
		
		o.addOption(help);
		o.addOption(inputFile);
		o.addOption(spinRules);
		o.addOption(outputDirectory);
		o.addOption(inputFormat);
		o.addOption(spinFileFormat);
		return o;
	}
	
	private static CommandLineParser createCliParser(){
		return new GnuParser();
	}
	
	private static void printUsage(){
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("hyqueSPINEvaluator [OPTIONS]", createOptions());
	};
}
