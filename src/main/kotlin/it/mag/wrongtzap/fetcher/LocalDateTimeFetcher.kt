package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsScalar
import graphql.GraphQLContext
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DgsScalar(name = "LocalDateTime")
class LocalDateTimeFetcher: Coercing<LocalDateTime, String> {

    private val formatter = DateTimeFormatter.ISO_DATE_TIME  // Use ISO 8601 format

    override fun serialize(dataFetcherResult: Any): String {
        if (dataFetcherResult is LocalDateTime) {
            return dataFetcherResult.format(formatter)  // Serialize LocalDateTime to ISO 8601 String
        }
        throw CoercingSerializeException("Expected a LocalDateTime object.")
    }

    // This method parses input values (like arguments) into LocalDateTime
    override fun parseValue(input: Any): LocalDateTime {
        try {
            return LocalDateTime.parse(input.toString(), formatter)  // Parse input value (usually from a mutation)
        } catch (e: Exception) {
            throw CoercingParseValueException("Unable to parse value as LocalDateTime: $input")
        }
    }

    // This method parses literal values (used in queries) into LocalDateTime
    override fun parseLiteral(input: Any): LocalDateTime {
        if (input is StringValue) {
            try {
                return LocalDateTime.parse(input.value, formatter)  // Parse StringValue literal
            } catch (e: Exception) {
                throw CoercingParseLiteralException("Unable to parse literal as LocalDateTime: ${input.value}")
            }
        }
        throw CoercingParseLiteralException("Expected a StringValue for LocalDateTime, but got: $input")
    }
}