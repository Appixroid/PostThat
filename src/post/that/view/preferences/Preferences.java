package post.that.view.preferences;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import post.that.utils.LocalPaths;

public class Preferences
{
	private static final String COLLECTION_STRINGIFY_SEPARATOR = ";";

	private static final Preferences instance = new Preferences();

	private Map<String, String> preferences = new HashMap<String, String>();

	public Preferences()
	{
		this.preferences = new HashMap<String, String>();
		this.parsePreferences();
	}

	public String getString(String key)
	{
		return this.getString(key, "");
	}

	public String getString(String key, String defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return value;
		}
	}

	public void setString(String key, String value)
	{
		this.preferences.put(key, value);
	}

	public int getInt(String key)
	{
		return this.getInt(key, 0);
	}

	public int getInt(String key, int defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Integer.parseInt(value);
		}
	}

	public void setInt(String key, int value)
	{
		this.preferences.put(key, Integer.toString(value));
	}

	public long getLong(String key)
	{
		return this.getLong(key, 0);
	}

	public long getLong(String key, long defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Long.parseLong(value);
		}
	}

	public void setLong(String key, long value)
	{
		this.preferences.put(key, Long.toString(value));
	}

	public float getFloat(String key)
	{
		return this.getFloat(key, 0);
	}

	public float getFloat(String key, float defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Float.parseFloat(value);
		}
	}

	public void setFloat(String key, float value)
	{
		this.preferences.put(key, Float.toString(value));
	}

	public double getDouble(String key)
	{
		return this.getDouble(key, 0);
	}

	public double getDouble(String key, double defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Double.parseDouble(value);
		}
	}

	public void setDouble(String key, double value)
	{
		this.preferences.put(key, Double.toString(value));
	}

	public byte getByte(String key)
	{
		return this.getByte(key, Byte.parseByte(""));
	}

	public byte getByte(String key, byte defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Byte.parseByte(value);
		}
	}

	public void setByte(String key, byte value)
	{
		this.preferences.put(key, Byte.toString(value));
	}

	public boolean getBoolean(String key)
	{
		return this.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue)
	{
		String value = this.preferences.get(key);
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return Boolean.parseBoolean(value);
		}
	}

	public void setBoolean(String key, boolean value)
	{
		this.preferences.put(key, Boolean.toString(value));
	}

	public Collection<String> getCollection(String key)
	{
		return this.getCollection(key, Collections.emptyList());
	}

	public Collection<String> getCollection(String key, Collection<String> defaultValue)
	{
		String values = this.preferences.get(key);

		if(values != null)
		{
			return Arrays.asList(values.split(Preferences.COLLECTION_STRINGIFY_SEPARATOR));
		}
		else
		{
			return defaultValue;
		}
	}

	public <T> Collection<T> getCollection(String key, Function<String, T> mapper)
	{
		return this.getCollection(key, mapper, Collections.emptyList());
	}

	public <T> Collection<T> getCollection(String key, Function<String, T> mapper, Collection<T> defaultValue)
	{
		String values = this.preferences.get(key);
		if(values != null)
		{
			Collection<T> list = new ArrayList<T>();
			for(String value : values.split(Preferences.COLLECTION_STRINGIFY_SEPARATOR))
			{
				list.add(mapper.apply(value));
			}

			return list;
		}
		else
		{
			return defaultValue;
		}
	}

	public void setCollection(String key, Object... collections)
	{
		this.setCollection(key, Arrays.asList(collections));
	}

	public void setCollection(String key, Collection<?> collections)
	{
		this.preferences.put(key, collections.stream().map(item -> {
			return item.toString();
		}).collect(Collectors.joining(Preferences.COLLECTION_STRINGIFY_SEPARATOR)));
	}

	public boolean save()
	{
		try
		{
			Files.writeString(LocalPaths.CONFIG_FILE.toLocalFile().toPath(), this.getPreferencesFileContent());
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}

	private void parsePreferences()
	{
		try
		{
			List<String> lines = Files.readAllLines(LocalPaths.CONFIG_FILE.toLocalFile().toPath());
			for(String line : lines)
			{
				int separator = line.indexOf('=');
				String key = line.substring(0, separator);
				String value = line.substring(separator + 1, line.length());
				this.preferences.put(key, value);
			}
		}
		catch(IOException e)
		{
		}
	}

	private String getPreferencesFileContent()
	{
		String preferencesContent = "";
		for(Entry<String, String> preference : this.preferences.entrySet())
		{
			preferencesContent += preference.getKey() + "=" + preference.getValue() + "\n";
		}
		return preferencesContent;
	}

	public static Preferences getInstance()
	{
		return Preferences.instance;
	}
}
