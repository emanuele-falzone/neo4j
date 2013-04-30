/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.symbols

import org.junit.Test
import org.scalatest.Assertions

class CypherTypeTest extends Assertions {
  @Test def collections_should_be_typed_correctly() {
    val value = Seq(Seq("Text"))
    val typ = new CollectionType(new CollectionType(StringType()))

    assert(CypherType.fromJava(value) === typ)
  }

  @Test
  def testTypeMerge() {
    assertCorrectTypeMerging(NumberType(), NumberType(), NumberType())
    assertCorrectTypeMerging(NumberType(), ScalarType(), ScalarType())
    assertCorrectTypeMerging(NumberType(), StringType(), ScalarType())
    assertCorrectTypeMerging(NumberType(), AnyCollectionType(), AnyType())
    assertCorrectTypeMerging(LongType(), DoubleType(), NumberType())
    assertCorrectTypeMerging(MapType(), DoubleType(), ScalarType())
  }

  def assertCorrectTypeMerging(a: CypherType, b: CypherType, result: CypherType) {
    val simpleMergedType: CypherType = a mergeWith b
    assert(simpleMergedType === result)
    val collectionMergedType: CypherType = (new CollectionType(a)) mergeWith (new CollectionType(b))
    assert(collectionMergedType === (new CollectionType(result)))
  }
}