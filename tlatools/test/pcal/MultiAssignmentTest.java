/*******************************************************************************
 * Copyright (c) 2018 Microsoft Research. All rights reserved. 
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
package pcal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tlc2.output.EC;

public class MultiAssignmentTest extends PCalModelCheckerTestCase {

	public MultiAssignmentTest() {
		super("MultiAssignment", "pcal", new String[] {"-wf", "-termination"});
	}

	@Test
	public void testSpec() {
		assertTrue(recorder.recordedWithStringValue(EC.TLC_INIT_GENERATED1, "1"));
		assertTrue(recorder.recordedWithStringValues(EC.TLC_CHECKING_TEMPORAL_PROPS, "complete", "27"));
		assertTrue(recorder.recorded(EC.TLC_FINISHED));
		assertFalse(recorder.recorded(EC.GENERAL));
		assertTrue(recorder.recordedWithStringValues(EC.TLC_STATS, "56", "27", "0"));
		assertTrue(recorder.recordedWithStringValue(EC.TLC_SEARCH_DEPTH, "7"));
		assertCoverage("  line 37, col 18 to line 38, col 56 of module MultiAssignment: 27\n" +
				"  line 39, col 18 to line 39, col 44 of module MultiAssignment: 27\n" +
				"  line 42, col 15 to line 42, col 45 of module MultiAssignment: 27\n" +
				"  line 47, col 15 to line 47, col 48 of module MultiAssignment: 27\n" +
				"  line 48, col 25 to line 48, col 34 of module MultiAssignment: 0\n" +
				"  line 54, col 70 to line 54, col 73 of module MultiAssignment: 0");
	}
}
/*
C:\lamport\tla\pluscal>java -mx1000m -cp "c:/lamport/tla/newtools/tla2-inria-workspace/tla2-inria/tlatools/class" tlc2.TLC -cleanup MultiAssignment.tla         
TLC2 Version 2.05 of 18 May 2012
Running in Model-Checking mode.
Parsing file MultiAssignment.tla
Parsing file C:\lamport\tla\newtools\tla2-inria-workspace\tla2-inria\tlatools\class\tla2sany\StandardModules\Naturals.tla
Parsing file C:\lamport\tla\newtools\tla2-inria-workspace\tla2-inria\tlatools\class\tla2sany\StandardModules\TLC.tla
Parsing file C:\lamport\tla\newtools\tla2-inria-workspace\tla2-inria\tlatools\class\tla2sany\StandardModules\Sequences.tla
Semantic processing of module Naturals
Semantic processing of module Sequences
Semantic processing of module TLC
Semantic processing of module MultiAssignment
Starting... (2012-08-10 17:38:18)
Implied-temporal checking--satisfiability problem has 1 branches.
Computing initial states...
Finished computing initial states: 1 distinct state generated.
Checking temporal properties for the complete state space...
Model checking completed. No error has been found.
  Estimates of the probability that TLC did not check all reachable states
  because two distinct states had the same fingerprint:
  calculated (optimistic):  val = 4.2E-17
  based on the actual fingerprints:  val = 1.4E-17
56 states generated, 27 distinct states found, 0 states left on queue.
The depth of the complete state graph search is 7.
Finished. (2012-08-10 17:38:18)
*/