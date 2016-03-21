package kr.osci.ide.codegen.utils;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kr.osci.ide.codegen.generator.Column;

/**
 * <pre>
 * JDBC Type 식별 기능을 제공하는 편리 함수 제공 클래스
 * </pre>
 * 
 * @author BongJin Kwon
 *
 */
public class JdbcTypeFactory {

	private static final int DELIMITIER_OF_INTEGER = 9;
	private static final String TYPE_BIGDECIMAL = "java.math.BigDecimal";
	private static final String TYPE_URL = "java.net.URL";
	private static final String TYPE_SQL_ARRAY = "java.sql.Array";
	private static final String TYPE_PRIMITIVE_BYTE_ARRAY = "byte[]";
	private static final String TYPE_OBJECT_STRING = "String";
	private static final String TYPE_SQL_TIMESTAMP = "java.sql.Timestamp";
	private static final String TYPE_SQL_TIME = "java.sql.Time";
	private static final String TYPE_SQL_DATE = "java.util.Date";
	private static final String TYPE_OBJECT_FLOAT = "Float";
	private static final String TYPE_PRIMITIVE_SHORT = "short";
	private static final String TYPE_PRIMITIVE_BYTE = "byte";
	private static final String TYPE_PRIMITIVE_LONG = "long";
	private static final String TYPE_PRIMITIVE_INT = "int";
	private static final String TYPE_PRIMITIVE_DOUBLE = "double";
	private static final String TYPE_OBJECT_INTEGER = "Integer";
	private static final String TYPE_OBJECT_LONG = "Long";
	private static final String TYPE_OBJECT_DOUBLE = "Double";
	private static final Map<Integer, String> JDBC_TYPES;

	static {
		JDBC_TYPES = Collections.unmodifiableMap(initializeTypeMap());
	}

	public static String resolveColumnType(Column column) {
		String selectedType = null;
		// 실제 타입에 관계 없이 Jdbc type이 모두 BigDecimal로 처리되는 문제
		// TODO 다른 데이터베이스에서도 동일하게 처리되는가?
		if (Types.DECIMAL == column.getJdbcType()) {
			selectedType = resolveNumberType(column);
		} else {
			selectedType = JDBC_TYPES.get(column.getJdbcType());
		}

		// 미지원 타입인 경우, Object로 타입 명시
		if (selectedType == null) {
			selectedType = "Object";
		}

		return selectedType;
	}

	private static String resolveNumberType(Column column) {
		if (column.getScale() > 0) {
			// TODO Scale 수에 따라 float과 구분해야 할까?
			//return TYPE_OBJECT_DOUBLE;
			return TYPE_PRIMITIVE_DOUBLE;
		} else/* 정수형 */{
			// TODO 현재는 9자리로 타입 구분. 보다 정확한 구분 필요?
			if (column.getPrecision() > DELIMITIER_OF_INTEGER) {
				//return TYPE_OBJECT_LONG;
				return TYPE_PRIMITIVE_LONG;
			} else {
				return TYPE_PRIMITIVE_INT;
			}
		}
	}

	private static HashMap<Integer, String> initializeTypeMap() {
		HashMap<Integer, String> typeMap = new HashMap<Integer, String>();

		// TODO primitive 타입을 쓸건지, 아니면 객체 형을 쓸건지. jdk 5에서는 호환되는지
		typeMap.put(Types.TINYINT, TYPE_PRIMITIVE_BYTE);
		typeMap.put(Types.SMALLINT, TYPE_PRIMITIVE_SHORT);
		//typeMap.put(Types.INTEGER, TYPE_OBJECT_INTEGER);
		typeMap.put(Types.INTEGER, TYPE_PRIMITIVE_INT);
		typeMap.put(Types.BIGINT, TYPE_OBJECT_LONG);
		typeMap.put(Types.FLOAT, TYPE_OBJECT_FLOAT);
		typeMap.put(Types.REAL, TYPE_OBJECT_FLOAT);
		typeMap.put(Types.DOUBLE, TYPE_OBJECT_DOUBLE);
		typeMap.put(Types.DECIMAL, TYPE_OBJECT_LONG);
		typeMap.put(Types.DATE, TYPE_SQL_DATE);
		typeMap.put(Types.TIME, TYPE_SQL_TIME);
		//typeMap.put(Types.TIMESTAMP, TYPE_SQL_TIMESTAMP);
		typeMap.put(Types.TIMESTAMP, TYPE_SQL_DATE);
		typeMap.put(Types.CLOB, TYPE_OBJECT_STRING);
		typeMap.put(Types.BLOB, TYPE_PRIMITIVE_BYTE_ARRAY);
		typeMap.put(Types.VARBINARY, TYPE_PRIMITIVE_BYTE_ARRAY);
		typeMap.put(Types.VARCHAR, TYPE_OBJECT_STRING);
		typeMap.put(Types.LONGNVARCHAR, TYPE_OBJECT_STRING);
		typeMap.put(Types.CHAR, TYPE_OBJECT_STRING);
		typeMap.put(Types.ARRAY, TYPE_SQL_ARRAY);
		typeMap.put(Types.DATALINK, TYPE_URL);

		typeMap.put(Types.DECIMAL, TYPE_BIGDECIMAL);

		return typeMap;
	}

}
