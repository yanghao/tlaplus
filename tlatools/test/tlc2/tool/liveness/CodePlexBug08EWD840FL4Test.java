/*******************************************************************************
 * Copyright (c) 2015 Microsoft Research. All rights reserved. 
 *
 * The MIT License (MIT)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Contributors:
 *   Markus Alexander Kuppe - initial API and implementation
 ******************************************************************************/

package tlc2.tool.liveness;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import tlc2.output.EC;

/**
 * see http://tlaplus.codeplex.com/workitem/8
 */
public class CodePlexBug08EWD840FL4Test extends ModelCheckerTestCase {

	public CodePlexBug08EWD840FL4Test() {
		super("EWD840MC4", "CodePlexBug08");
	}
	
	@Test
	public void testSpec() {
		// ModelChecker has finished and generated the expected amount of states
		assertTrue(recorder.recorded(EC.TLC_FINISHED));
		assertTrue(recorder.recordedWithStringValues(EC.TLC_STATS, "15986", "1566", "0"));
		assertFalse(recorder.recorded(EC.GENERAL));
		
		// Assert it has found the temporal violation and also a counter example
		assertTrue(recorder.recorded(EC.TLC_TEMPORAL_PROPERTY_VIOLATED));
		assertTrue(recorder.recorded(EC.TLC_COUNTER_EXAMPLE));
		
		assertNodeAndPtrSizes(135540L, 23456L);
		
		// Assert the error trace
		assertTrue(recorder.recorded(EC.TLC_STATE_PRINT2));
		final List<String> expectedTrace = new ArrayList<String>();
		expectedTrace.add("/\\ tpos = 0\n"
				   + "/\\ active = (0 :> FALSE @@ 1 :> FALSE @@ 2 :> FALSE @@ 3 :> TRUE)\n"
				   + "/\\ tcolor = \"black\"\n"
				   + "/\\ color = (0 :> \"white\" @@ 1 :> \"white\" @@ 2 :> \"white\" @@ 3 :> \"white\")");
		expectedTrace.add("/\\ tpos = 3\n"
				   + "/\\ active = (0 :> FALSE @@ 1 :> FALSE @@ 2 :> FALSE @@ 3 :> TRUE)\n"
				   + "/\\ tcolor = \"white\"\n"
				   + "/\\ color = (0 :> \"white\" @@ 1 :> \"white\" @@ 2 :> \"white\" @@ 3 :> \"white\")");
		assertTraceWith(recorder.getRecords(EC.TLC_STATE_PRINT2), expectedTrace);
		
		// state 3 is stuttering
		assertStuttering(3);

	assertCoverage("  line 32, col 6 to line 32, col 16 of module EWD840: 448\n" +
		"  line 33, col 6 to line 33, col 22 of module EWD840: 448\n" +
		"  line 34, col 6 to line 34, col 21 of module EWD840: 448\n" +
		"  line 35, col 6 to line 35, col 43 of module EWD840: 448\n" +
		"  line 49, col 6 to line 49, col 16 of module EWD840: 2030\n" +
		"  line 50, col 6 to line 50, col 61 of module EWD840: 2030\n" +
		"  line 51, col 6 to line 51, col 21 of module EWD840: 2030\n" +
		"  line 52, col 6 to line 52, col 43 of module EWD840: 2030\n" +
		"  line 63, col 12 to line 63, col 48 of module EWD840: 9363\n" +
		"  line 64, col 12 to line 64, col 68 of module EWD840: 9363\n" +
		"  line 65, col 16 to line 65, col 31 of module EWD840: 0\n" +
		"  line 70, col 6 to line 70, col 43 of module EWD840: 3121\n" +
		"  line 71, col 16 to line 71, col 38 of module EWD840: 0");
	}
}
