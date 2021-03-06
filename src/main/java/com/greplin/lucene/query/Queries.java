// Copyright 2010 Greplin, Inc. All Rights Reserved.
package com.greplin.lucene.query;

import com.greplin.lucene.analysis.Terms;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

/**
 * Pre-rolled query formulas.
 */
public final class Queries {

  /** Not instantiable. */
  private Queries() { }


  /**
   * Returns a query that matches documents that match all of the
   * given sub-queries.
   * @param subQueries queries to match all of.
   * @return a query that matches documents matching all sub-queries.
   */
  public static BooleanQuery and(final Query ... subQueries) {
    BooleanQueryBuilder builder = new BooleanQueryBuilder();
    for (Query subQuery : subQueries) {
      builder.must(subQuery);
    }
    return builder.build();
  }


  /**
   * Returns a query that matches documents that match any of the
   * given sub-queries.
   * @param subQueries queries to match all of.
   * @return a query that matches documents matching all sub-queries.
   */
  public static BooleanQuery or(final Query... subQueries) {
    BooleanQueryBuilder builder = new BooleanQueryBuilder();
    for (Query subQuery : subQueries) {
      builder.should(subQuery);
    }
    return builder.build();
  }



  /**
   * Returns a query that will not match documents that match any of the
   * given sub-queries.
   * @param subQueries queries to match none of.
   * @return a query that doesn't match documents matching all sub-queries.
   */
  public static BooleanQuery not(final Query... subQueries) {
    BooleanQueryBuilder builder = new BooleanQueryBuilder();
    for (Query subQuery : subQueries) {
      builder.mustNot(subQuery);
    }
    return builder.build();
  }


  /**
   * Returns a PhraseQuery generated by the passed string.
   * @param analyzer - the analyzer used to tokenize the index
   * @param field - the field to match on
   * @param query - The un-tokenized string
   * @return a PhraseQuery
   */
  public static PhraseQuery phraseFor(
      final Analyzer analyzer, final String field, final String query) {
    Term[] terms = Terms.termsFor(analyzer, field, query);
    if (terms == null) {
      return null;
    }
    PhraseQuery phrase = new PhraseQuery();
    for (Term term : terms) {
      phrase.add(term);
    }
    return phrase;
  }


  /**
   * Returns a version of the given query that always matches with the
   * given score.
   * @param query the query
   * @param score the desired score
   * @return the constant score query
   */
  public static ConstantScoreQuery constantScore(
      final Query query, final float score) {
    ConstantScoreQuery result = new ConstantScoreQuery(query);
    result.setBoost(score);
    return result;
  }

}
