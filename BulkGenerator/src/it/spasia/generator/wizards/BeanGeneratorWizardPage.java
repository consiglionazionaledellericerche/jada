package it.spasia.generator.wizards;

import org.eclipse.jface.wizard.WizardPage;

import it.spasia.generator.model.GeneratorBean;

/**
 * Provvede funzionalit� comuni per le pagine del wizard
 * 
 * @author Marco Spasiano
 * @version 1.0 [8-Aug-2006]
 */
public abstract class BeanGeneratorWizardPage extends WizardPage implements IBeanGeneratorWizardPage {
	protected GeneratorBean bean;

	protected BeanGeneratorWizardPage(String pageName, GeneratorBean bean) {
		super(pageName);
		this.bean = bean;
	}

	protected void initializeNextPage(GeneratorBean bean) {
		IBeanGeneratorWizardPage page = (IBeanGeneratorWizardPage) getNextPage();
		if (page != null) {
			page.initializePage();
		}
	}
	
	/** return false se text equals null or blank */
	public static boolean isTextEmpty(String text) {
		if (text == null) return true;
		if (text.trim().length() == 0) return true;
		return false;
	}
	
	/** return false se text equals null or blank */
	public static boolean isTextNonEmpty(String text) {
		return !isTextEmpty(text);
	}
	
}
