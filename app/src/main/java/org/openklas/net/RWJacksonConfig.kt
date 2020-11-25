package org.openklas.net

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
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

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature

open class RWJacksonConfig {
	private val _mapperFeatureMap: MutableMap<MapperFeature, Boolean> = mutableMapOf()
	private val _serializationFeatureMap: MutableMap<SerializationFeature, Boolean> = mutableMapOf()
	private val _deserializationFeatureMap: MutableMap<DeserializationFeature, Boolean> =
		mutableMapOf()
	private val _jsonParserMap: MutableMap<JsonParser.Feature, Boolean> = mutableMapOf()

	init {
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	}

	fun configure(feature: MapperFeature, state: Boolean) =
		_mapperFeatureMap.apply { _mapperFeatureMap[feature] = state }

	fun configure(feature: SerializationFeature, state: Boolean) =
		_serializationFeatureMap.apply { _serializationFeatureMap[feature] = state }

	fun configure(feature: JsonParser.Feature, state: Boolean) =
		_jsonParserMap.apply { _jsonParserMap[feature] = state }

	fun configure(feature: DeserializationFeature, state: Boolean) =
		_deserializationFeatureMap.apply { _deserializationFeatureMap[feature] = state }

	fun getMapperFeaturePairs(): List<Pair<MapperFeature, Boolean>> =
		_mapperFeatureMap.entries.map { it.key to it.value }

	fun getSerialzationFeaturePairs(): List<Pair<SerializationFeature, Boolean>> =
		_serializationFeatureMap.entries.map { it.key to it.value }

	fun getDeserialzationFeaturePairs(): List<Pair<DeserializationFeature, Boolean>> =
		_deserializationFeatureMap.entries.map { it.key to it.value }

	fun getJsonParserPairs(): List<Pair<JsonParser.Feature, Boolean>> =
		_jsonParserMap.entries.map { it.key to it.value }

	fun clear() {
		_mapperFeatureMap.clear()
		_serializationFeatureMap.clear()
		_deserializationFeatureMap.clear()
		_jsonParserMap.clear()
	}
}
