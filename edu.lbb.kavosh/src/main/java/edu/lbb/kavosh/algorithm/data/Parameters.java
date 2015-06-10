/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 6, 2011
 **/
package edu.lbb.kavosh.algorithm.data;

import java.io.File;
import java.io.Serializable;

public class Parameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8661221468401944623L;
	private static Parameters instance;
	public final static String PLUGIN_NAME = "CytoKavosh";
	public final static String PLUGIN_DIR = File.separatorChar + "plugins";
	public final static String RESULT_DIR = File.separatorChar + "result";
	public final static char LIN_DIR_SEP = File.separatorChar;
	public final static String IN_FILE_NAME = "in.txt";
	public final static String KAVOSH_EXE_PATH = File.separatorChar + "Kavosh-SRC" + File.separatorChar + "Kavosh";
	public final static String KAVOSH_JAR_NAME = "kavosh-1.1.jar";
	public final static String VERSION = "1.1";

	private boolean interupted = false;
	private int motifSize = 3;
	private int randSize = 100;
	private int zScoreLimit = -1000;
	private int freqLimit = 0;

	/**
	 * initialization of the singleton if there is a precedent serialization,
	 * load it else, load default values
	 * 
	 * @return the class, default if no precedent, the loaded else
	 */
	public static Parameters getInstance() {
		if (instance == null) {
			instance = new Parameters();
			return instance;
		} else
			return instance;
	}

	public boolean isInterrupted() {
		return interupted;
	}

	public void interrupt(boolean interupted) {
		this.interupted = interupted;
	}

	public void setDefault() {
		motifSize = 3;
		randSize = 0;
		zScoreLimit = -1000;
		freqLimit = 0;
	}

	public int getzScoreLimit() {
		return zScoreLimit;
	}

	public int getFreqLimit() {
		return freqLimit;
	}

	public int getRandSize() {
		return randSize;
	}

	public int getMotifSize() {
		return motifSize;
	}

	public void setMotifSize(int motifSize) {
		this.motifSize = motifSize;
	}

	public void setRandSize(int randSize) {
		this.randSize = randSize;
	}

	public void setzScoreLimit(int zScoreLimit) {
		this.zScoreLimit = zScoreLimit;
	}

	public void setFreqLimit(int freqLimit) {
		this.freqLimit = freqLimit;
	}

}
