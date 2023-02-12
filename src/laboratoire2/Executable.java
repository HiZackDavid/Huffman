package laboratoire2;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Executable {

	public static void main(String[] args) {
		/**
		 * For more information, visit :
		 * https://commons.apache.org/proper/commons-cli/apidocs/org/apache/commons/cli/package-summary.html
		 */
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();

		options.addOption(Option.builder("huff").desc("Use Huffman algorithm").build());
		options.addOption(Option.builder("lzw").desc("Use LZW algorithm").build());
		options.addOption(Option.builder("opt").desc("Use an optimized algorithm").build());
		options.addOption(Option.builder("c").hasArg(true).argName("INFILE OUTFILE")
				.desc("Compress the specified inFile and save it in outFile").build());
		options.addOption(Option.builder("d").hasArg(true).argName("INFILE OUTFILE")
				.desc("Decompress the specified inFile and save it in outFile").build());
		options.addOption(Option.builder("h").longOpt("help").hasArg(false).desc("Shows this help").build());

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("h")) {
				String syntax = "Compress -[huff|lzw] -[cdh] inFile outFile";
				String header = "Options :";
				String footer = "";
				HelpFormatter formatter = new HelpFormatter();
				formatter.setOptionComparator(null);
				formatter.printHelp(syntax, header, options, footer);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
