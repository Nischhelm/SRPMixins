package srpmixins.config;

import srpmixins.SRPMixins;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Inspired by fonnymunkey RLMixins, altered
public class EarlyConfigReader {
	private static File configFile = null;
	private static String configBooleanString = null;
	private static String configIntString = null;
	//private static String configStringArrayString = null; //TODO WIP

	public static boolean getBoolean(String name, boolean defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configBooleanString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					//All lines starting with "B:"
					configBooleanString = stream.filter(s -> s.trim().startsWith("B:")).collect(Collectors.joining());
				} catch (Exception e) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + e);
				}
			} else configBooleanString = "";
		}

		if (configBooleanString.contains("B:\"" + name + "\"="))
			return configBooleanString.contains("B:\"" + name + "\"=true");
		//If config is not generated yet or missing entries, we use the default value that would be written into it
		else return defaultValue;
	}

	public static int getInt(String name, int defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configIntString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configIntString = stream.filter(s -> s.trim().startsWith("I:")).collect(Collectors.joining());
				} catch (Exception ex) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			} else configIntString = "";
		}

		if (configIntString.contains("I:\"" + name + "\"=")) {
			int index = configIntString.indexOf("I:\"" + name + "\"=");
			try {
				Matcher matcher = Pattern.compile("(\\d+)").matcher(configIntString.substring(index));
				matcher.find();
				return Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				SRPMixins.LOGGER.error(SRPMixins.NAME + ": Failed to parse int config "+ name + ", " + e);
				return 0;
			}
		}
		//If config is not generated yet or missing entries, we use the default value that will get written into it right after this
		else return defaultValue;
	}

	/* WIP
	public static boolean stringListIsDisabled(String name, String[] defaultValue) {
		if (configFile == null) configFile = new File("config", SRPMixins.MODID + ".cfg");

		if (configStringArrayString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try {
					List<String> lines = Files.readAllLines(configFile.toPath());
					List<String> actualLines = new ArrayList<>();
					boolean inStringList = false;
					for(String line : lines){
						if(line.trim().startsWith("S:"))
							inStringList = true;
						if(inStringList) actualLines.add(line);
						if(line.trim().endsWith(">"))
							inStringList = false;
					}

					configStringArrayString = stream.map(s -> s.replace()).collect(Collectors.joining());
					configStringArrayString = stream.filter(s -> s.trim().startsWith("S:") && s.trim().endsWith("<")).collect(Collectors.joining());
				} catch (Exception ex) {
					SRPMixins.LOGGER.error("Failed to parse " + SRPMixins.NAME + " config: " + ex);
				}
			} else configStringArrayString = "";
		}

		if (configStringArrayString.contains("S:\"" + name + "\"=")) {
			int index = configStringArrayString.indexOf("I:\"" + name + "\"=");
			try {
				Matcher matcher = Pattern.compile("(\\d+)").matcher(configIntString.substring(index));
				matcher.find();
				return Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				SRPMixins.LOGGER.error(SRPMixins.NAME + ": Failed to parse int config "+ name + ", " + e);
				return 0;
			}
		}
		//If config is not generated yet or missing entries, we use the default value that will get written into it right after this
		else return defaultValue.length == 0;
	}*/
}