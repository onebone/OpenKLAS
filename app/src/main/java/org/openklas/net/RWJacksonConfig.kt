package org.openklas.net

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