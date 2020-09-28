package oss.member.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JsonCmd {

    private ObjectMapper mapper;

	@Bean("jsonMapper")
	public void initObjectMapper() {
		mapper = new ObjectMapper();
		
		/** Json 포멧 강제화 해제 (Unrecognized field 처리) */
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}
	
	/**
     * 문자열 jsonnode 변환
     * @param strJson
     * @return
     */
    public JsonNode stringToJson(String strJson) {
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(strJson);
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
        return actualObj;
    }

    /**
     * json 문자열 변환
     * @param json
     * @return
     */
    public String jsonToString(JsonNode json) {
        String actualString = null;
        try {
            actualString = mapper.writeValueAsString(json);
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
        return actualString;
    }

    /**
     * 문자열 맵 변환
     * @param strJson
     * @return
     */
    public HashMap<String, String> stringToHashmap(String strJson) {
        return getHashMap(stringToJson(strJson));
    }
    
    /**
     * 문자열 맵 변환
     * @param strJson
     * @return
     */
    public HashMap<String, Object> stringToHashmapObj(String strJson) {
        return getHashMapObj(stringToJson(strJson));
    }

    /**
     * json 값 반환
     * @param json
     * @param key
     * @return
     */
    public String getText(JsonNode json, String key) {
        if (json != null && json.has(key)) return json.get(key).textValue();
        else return "";
    }

    /**
     * json 맵 반환
     * @param json
     * @return
     */
    public HashMap<String, Object> getHashMapObj(JsonNode json) {

        HashMap<String, Object> hm = new HashMap<String, Object>();

        if (!(json instanceof ObjectNode)) { return null; }

        ObjectNode objNode = (ObjectNode) json;
        Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode value = entry.getValue();
            hm.put(entry.getKey(), value.isTextual() ? value.textValue() : value.asText());
        }
        return hm;
    }
    /**
     * json 맵 반환
     * @param json
     * @return
     */
    public HashMap<String, String> getHashMap(JsonNode json) {

        HashMap<String, String> hm = new HashMap<String, String>();

        if (!(json instanceof ObjectNode)) { return null; }

        ObjectNode objNode = (ObjectNode) json;
        Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode value = entry.getValue();
            hm.put(entry.getKey(), value.isTextual() ? value.textValue() : value.asText());
        }
        return hm;
    }

    /**
     * json 맵 float  반환
     * @param json
     * @return
     */
    public HashMap<String, Float> getHashMapFloat(JsonNode json) {

        HashMap<String, Float> hm = new HashMap<String, Float>();

        if (!(json instanceof ObjectNode)) { return null; }

        ObjectNode objNode = (ObjectNode) json;
        Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode value = entry.getValue();
            hm.put(entry.getKey(), Float.parseFloat(value.isTextual() ? value.textValue() : value.asText()) );
        }
        return hm;
    }

    public List<Object> stringToList(String strJson) {
    	try {
    		return mapper.readValue( strJson, new TypeReference<List<Object>>(){});
    	} catch (Exception e) {
    		log.error(e.getMessage());
			return null;
		}
    }

    /**
     * json 맵 float 반환
     * @param strJson
     * @return
     */
    public HashMap<String, Float> stringToHashMapFloat(String strJson) {
        return getHashMapFloat(stringToJson(strJson));
    }

    /**
     * json 값 설정
     * @param json
     * @param key
     * @param value
     */
    public void setValue(JsonNode json, String key, String value) {
        ((ObjectNode)json).put(key, value);
    }


    /**
     * 맵 json문자열 변환
     * @param hm
     * @return
     */
    public String hashmapToJsonString(HashMap<String, String> hm) {
        String rslt = "";
        try {
            rslt = mapper.writeValueAsString(hm);
        } catch (Exception e) {
        	log.error(e.getMessage());
            rslt = "";
        }
        return rslt;
    }

    /**
     * 맵 json 변환
     * @param hm
     * @return
     */
    public JsonNode hashmapToJsonNode(HashMap<String, String> hm) {
        JsonNode jsonNode = mapper.convertValue(hm, JsonNode.class);
        return jsonNode;
    }
    
    /**
     * Object to json문자열 변환
     * @param hm
     * @return
     */
    public String objToJsonString(Object obj) {
        String rslt = "";
        try {
            rslt = mapper.writeValueAsString(obj);
        } catch (Exception e) {
        	log.error(e.getMessage());
            rslt = "";
        }
        return rslt;
    }
    
    /**
     * byte array to model
     * @param hm
     * @return
     */
    public Object bytesToObj(byte[] bytes, Class<?> clazz) {
        try {
        	return mapper.readValue(bytes, clazz);

        } catch (Exception e) {
        	log.error(e.getMessage());
        	return null;
        }
    }
    
    /**
     * jsonstr to model
     * @param hm
     * @return
     */
    public Object jsonStringToObj(String jsonStr, Class<?> clazz) {
        try {
        	return mapper.readValue(jsonStr, clazz);

        } catch (Exception e) {
        	log.error(e.getMessage());
        	return null;
        }
    }
    
    /**
     * Object to byte array 변환
     * @param hm
     * @return
     */
    public byte[] objToBytes(Object obj) {
		if (ObjectUtils.isEmpty(obj)) {
			return null;
		}
		
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
        	log.error(e.getMessage());
            return null;
        }
    }
    
    public String pretty(String src) {
    	try {
			return mapper.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			return src;
		}
    }
    
    public Map<String,String> objToMap(Object obj){
    	try {
    		return mapper.convertValue(obj, Map.class);
    	}catch(Exception e) {
    		return null;
    	}
    }
    
    public Map<String,Object> objToObjMap(Object obj){
    	try {
    		return mapper.convertValue(obj, Map.class);
    	}catch(Exception e) {
    		return null;
    	}
    }
    
    public static boolean isJSONValid(String jsonInString ) {
        try {
           final ObjectMapper mapper = new ObjectMapper();
           mapper.readTree(jsonInString);
           return true;
        } catch (Exception e) {
           return false;
        }
      }

}

