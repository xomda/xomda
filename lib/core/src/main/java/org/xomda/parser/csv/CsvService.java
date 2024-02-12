package org.xomda.parser.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.xomda.core.config.Configuration;
import org.xomda.core.util.Extensions;
import org.xomda.parser.InternalParseContext;
import org.xomda.parser.Parser;

/**
 * The CSV Service reads CSV files into parsed objects (proxies).
 */
public class CsvService implements Parser {

	public static final String DEFAULT_CSV_DELIMITER = ";";

	private static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.DEFAULT.builder()
			.setDelimiter(DEFAULT_CSV_DELIMITER)
			.setSkipHeaderRecord(true)
			.setIgnoreEmptyLines(false).build();

	/**
	 * Parse a CSV file into objects
	 */
	@Override
	public <T> List<T> parse(final String[] filenames, final Configuration config) throws IOException {
		// First create the parse context
		final InternalParseContext context = new InternalParseContext(config);

		// Parse all dependencies (so they end up in the cache)
		List<T> dependencies = process(config.getDependentModels(), context);

		// parse the files themselves
		process(filenames, context);

		// run all the deferred actions (de-referencing)
		context.runDeferred();

		// subtract the dependency models from the result
		// (to avoid confusing the end-user)
		List<T> result = new ArrayList<>(context.getObjects());
		result.removeAll(dependencies);

		return result;
	}

	private <T> List<T> process(final String[] filenames, final InternalParseContext context) throws IOException {
		Configuration configuration = context.getConfig();
		CsvSchema globalSchema = null;
		int count = 0;

		// parse each file
		for (final String filename : filenames) {
			getLogger().info("Parsing {}", filename);

			count = 0;

			final File absoluteFile = new File(filename).getAbsoluteFile();
			if (!absoluteFile.exists()) {
				throw new FileNotFoundException("Unable to read CSV document: " + absoluteFile);
			}

			try (
					final Reader reader = new FileReader(absoluteFile);
					final CSVParser parser = DEFAULT_CSV_FORMAT.parse(reader)
			) {
				// init the extensions
				Extensions.init(context);

				final Iterator<CSVRecord> it = parser.iterator();

				// init the schema and let the extensions know
				final CsvSchema schema = CsvSchema.load(it, context);

				// check if the schemas are compatible
				if (null != globalSchema && !schema.isCompatible(globalSchema)) {
					throw new IllegalArgumentException("Incompatible CSV Schema's detected. (" + filename + ")");
				}

				// assign the global schema
				if (globalSchema == null) {
					globalSchema = schema;
				}

				// feed the schema to the extensions
				Extensions.process(configuration, schema);

				// read the schema
				CSVRecord record;

				// read the rest
				getLogger().info("Reading model definitions");
				while (it.hasNext() && null != (record = it.next())) {
					if (isEmpty(record)) {
						continue;
					}
					final CsvObject obj = schema.readObject(record, context);
					if (null == obj) {
						getLogger().error("Couldn't parse {}", record.toList());
						continue;
					} else {
						count++;
					}

					getLogger().debug("Read {}", (Object[]) obj.getClasses());

					// feed it to the extensions
					Extensions.process(configuration, obj);

					// add to processed cache
					context.add(obj);
				}
			}

			getLogger().info("Parsed {} objects.", count);
		}

		return context.getObjects();
	}

	/**
	 * Returns whether a record is empty, or whether each of its cells is blank.
	 */
	static boolean isEmpty(final CSVRecord record) {
		return null == record || record.size() == 0 || record.stream().allMatch(String::isBlank)
				|| record.get(0).startsWith("#");
	}

}
