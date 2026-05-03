package com.tobutilities;

import java.lang.reflect.Field;

public final class TestUtils
{
	private TestUtils()
	{
	}

	public static void setField(Object target, String fieldName, Object value)
	{
		Field field = findField(target.getClass(), fieldName);
		field.setAccessible(true);
		try
		{
			field.set(target, value);
		}
		catch (IllegalAccessException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private static Field findField(Class<?> type, String fieldName)
	{
		Class<?> current = type;
		while (current != null)
		{
			try
			{
				return current.getDeclaredField(fieldName);
			}
			catch (NoSuchFieldException ignored)
			{
				current = current.getSuperclass();
			}
		}

		throw new IllegalArgumentException("Missing field: " + fieldName);
	}
}
