package com.jorisaerts.omda.csv;

import com.jorisaerts.omda.util.ModelContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class CsvService {

    private static final Logger logger = LogManager.getLogger(CsvService.class);

    public static final String DEFAULT_CSV_DELIMITER = ";";

    private static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(DEFAULT_CSV_DELIMITER)
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(false)
            .build();


    public <T> T read(final String filename) throws IOException {
        logger.info("Reading CSV model: {}", filename);
        return read(filename, new ModelContext());
    }

    public <T> T read(final String filename, final ModelContext context) throws IOException {
        try (
                final Reader in = new FileReader(filename);
                final CSVParser parser = DEFAULT_CSV_FORMAT.parse(in)
        ) {
            final Iterator<CSVRecord> it = parser.iterator();
            final CsvSchema schema = CsvSchema.load(it, context);

            // read the schema
            CSVRecord record;
            int count = 0;

            final Deque<CsvObject> stack = new ArrayDeque<CsvObject>();

            // read the rest
            logger.info("Parsing models");

            while (it.hasNext() && null != (record = it.next())) {
                if (isEmpty(record)) continue;
                final String name = record.get(0);
                final CsvObject obj = schema.readObject(record);


                if (null == obj) {
                    logger.error("Couldn't parse {}", record.toList());
                    continue;
                } else count++;

                schema.addObject(obj, stack);
            }

            logger.info("Parsed {} objects.", count);

            final T result = (T) stack.getLast().getProxy();
            return result;
        }
    }


    static boolean isEmpty(final CSVRecord record) {
        return null == record || record.size() == 0 || record.stream().allMatch(String::isBlank) || record.get(0).startsWith("#");
    }

}
