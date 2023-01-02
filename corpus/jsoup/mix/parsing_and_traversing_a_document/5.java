package org.jsoup.select;

import static org.jsoup.internal.Normalizer.normalize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.parser.TokenQueue;

public class PseudoQueryParser extends CustomQueryParser {
	
	public PseudoQueryParser(AbstractQueryParser _parser){
		parser = _parser;
	}
	

    public PseudoQueryParser() {

    }


    protected void findElements() {
	
		if (tq.matchChomp(":lt("))
		    indexLessThan();
		else if (tq.matchChomp(":gt("))
		    indexGreaterThan();
		else if (tq.matchChomp(":eq("))
		    indexEquals();
		else if (tq.matches(":has("))
		    has();
		else if (tq.matches(":contains("))
		    contains(false);
		else if (tq.matches(":containsOwn("))
		    contains(true);
		else if (tq.matches(":containsData("))
		    containsData();
		else if (tq.matches(":matches("))
		    matches(false);
		else if (tq.matches(":matchesOwn("))
		    matches(true);
		else if (tq.matches(":not("))
		    not();
		else if (tq.matchChomp(":matchText"))
		    evals.add(new Evaluator.MatchText());
		else // unhandled
			parser.findElements();
	}
 

    // pseudo selectors :lt, :gt, :eq
    private void indexLessThan() {
        evals.add(new Evaluator.IndexLessThan(consumeIndex()));
    }

    private void indexGreaterThan() {
        evals.add(new Evaluator.IndexGreaterThan(consumeIndex()));
    }

    private void indexEquals() {
        evals.add(new Evaluator.IndexEquals(consumeIndex()));
    }
    

    private int consumeIndex() {
        String indexS = tq.chompTo(")").trim();
        Validate.isTrue(StringUtil.isNumeric(indexS), "Index must be numeric");
        return Integer.parseInt(indexS);
    }

    // pseudo selector :has(el)
    private void has() {
        tq.consume(":has");
        String subQuery = tq.chompBalanced('(', ')');
        Validate.notEmpty(subQuery, ":has(el) subselect must not be empty");
        evals.add(new StructuralEvaluator.Has(parse(subQuery)));
    }

    // pseudo selector :contains(text), containsOwn(text)
    private void contains(boolean own) {
        tq.consume(own ? ":containsOwn" : ":contains");
        String searchText = TokenQueue.unescape(tq.chompBalanced('(', ')'));
        Validate.notEmpty(searchText, ":contains(text) query must not be empty");
        if (own)
            evals.add(new Evaluator.ContainsOwnText(searchText));
        else
            evals.add(new Evaluator.ContainsText(searchText));
    }

    // pseudo selector :containsData(data)
    private void containsData() {
        tq.consume(":containsData");
        String searchText = TokenQueue.unescape(tq.chompBalanced('(', ')'));
        Validate.notEmpty(searchText, ":containsData(text) query must not be empty");
        evals.add(new Evaluator.ContainsData(searchText));
    }

    // :matches(regex), matchesOwn(regex)
    private void matches(boolean own) {
        tq.consume(own ? ":matchesOwn" : ":matches");
        String regex = tq.chompBalanced('(', ')'); // don't unescape, as regex bits will be escaped
        Validate.notEmpty(regex, ":matches(regex) query must not be empty");

        if (own)
            evals.add(new Evaluator.MatchesOwn(Pattern.compile(regex)));
        else
            evals.add(new Evaluator.Matches(Pattern.compile(regex)));
    }

    // :not(selector)
    private void not() {
        tq.consume(":not");
        String subQuery = tq.chompBalanced('(', ')');
        Validate.notEmpty(subQuery, ":not(selector) subselect must not be empty");

        evals.add(new StructuralEvaluator.Not(parse(subQuery)));
    }
}

