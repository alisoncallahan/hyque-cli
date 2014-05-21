package org.semanticscience.hyque.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpException;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.SystemTriples;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HyQueSPINRuleExecutor {

	// create OntModel
	private OntModel hyqueSPINModel = null;
	private OntModel spinOnlyModel = null;
	
	public HyQueSPINRuleExecutor(){
		hyqueSPINModel  = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		spinOnlyModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
	}

	public Model executeSPINRulesOnFile(File rules, File hypothesis,
			String inputFormat, String ruleInputFormat) {

		Model evaluationModel = null;
		Model hypModel = null;
		
		FileInputStream rulesFIS = null;
		FileInputStream rulesFIS2 = null;
		FileInputStream hypFIS = null;

		try {

			// add system triples to hyqueSPINModel
			hyqueSPINModel.add(SystemTriples.getVocabularyModel());
			
			rulesFIS = new FileInputStream(rules);
			rulesFIS2 = new FileInputStream(rules);
			hyqueSPINModel.read(rulesFIS, null, ruleInputFormat);
			spinOnlyModel.read(rulesFIS2, null, ruleInputFormat);

			hypModel = ModelFactory.createDefaultModel();
			hypFIS = new FileInputStream(hypothesis);

			hypModel.read(hypFIS, null, inputFormat);

			hyqueSPINModel.addSubModel(hypModel);

			evaluationModel = ModelFactory.createDefaultModel();
			hyqueSPINModel.addSubModel(evaluationModel);
			
			// Initialize system functions and templates
			SPINModuleRegistry.get().init();

			// register locally defined functions
			SPINModuleRegistry.get().registerAll(hyqueSPINModel, null);

			// run SPIN inferences
			try {
			SPINInferences.run(hyqueSPINModel, evaluationModel, null, null,
					false, null);
			} catch (Exception e){
				e.printStackTrace();
			}
			
			evaluationModel.add(hypModel);

			// remove input and evaluation models from hyqueSPINModel
			hyqueSPINModel.removeSubModel(hypModel);
			hyqueSPINModel.removeSubModel(evaluationModel);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rulesFIS.close();
				rulesFIS2.close();
				hypFIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return evaluationModel;
	}

}
