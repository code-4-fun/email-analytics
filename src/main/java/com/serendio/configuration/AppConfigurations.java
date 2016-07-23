package com.serendio.configuration;

public class AppConfigurations {
	private static String PASSWORD = null;
	private static String INPUT_PATH = null;
	private static String INPUT_DATASET_TYPE = null;
	private static boolean SENTIMENT_ANALYSIS = false;
	private static boolean TOPIC_EXTRACTION = false;

	public static boolean isSENTIMENT_ANALYSIS() {
		return SENTIMENT_ANALYSIS;
	}

	public static void setSENTIMENT_ANALYSIS(boolean sENTIMENT_ANALYSIS) {
		SENTIMENT_ANALYSIS = sENTIMENT_ANALYSIS;
	}

	public static boolean isTOPIC_EXTRACTION() {
		return TOPIC_EXTRACTION;
	}

	public static void setTOPIC_EXTRACTION(boolean tOPIC_EXTRACTION) {
		TOPIC_EXTRACTION = tOPIC_EXTRACTION;
	}

	public static enum EmailDatasetType {
		MBOX, EML, PST;
	}

	public String getINPUT_PATH() {
		return INPUT_PATH;
	}

	public void setINPUT_PATH(String File_PATH) {
		INPUT_PATH = File_PATH;
	}

	public void setDatasetType(EmailDatasetType type) {
		INPUT_DATASET_TYPE = type.toString();
	}

	public String getDatasetType() {
		return INPUT_DATASET_TYPE;
	}

	public static void setPassword(String password) {
		PASSWORD = password;
	}

	public static String getPassword() {
		return PASSWORD;
	}

}
